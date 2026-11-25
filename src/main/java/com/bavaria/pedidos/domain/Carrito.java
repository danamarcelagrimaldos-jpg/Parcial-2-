package com.bavaria.pedidos.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Entity
@Table(name = "carritos")
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @OneToMany(mappedBy = "carrito", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ItemCarrito> items = new ArrayList<>();

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion = LocalDateTime.now();

    protected Carrito() {
    }

    public Carrito(Cliente cliente) {
        this.cliente = cliente;
    }

    public void agregarProducto(Producto producto, int cantidad) {
        Optional<ItemCarrito> existente = items.stream()
                .filter(i -> i.getProducto().getId().equals(producto.getId()))
                .findFirst();

        if (existente.isPresent()) {
            existente.get().incrementarCantidad(cantidad);
        } else {
            items.add(new ItemCarrito(this, producto, cantidad));
        }
        this.fechaActualizacion = LocalDateTime.now();
    }

    public void vaciar() {
        this.items.clear();
        this.fechaActualizacion = LocalDateTime.now();
    }

    public BigDecimal calcularTotal() {
        return items.stream()
                .map(ItemCarrito::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public List<ItemCarrito> getItems() { return items; }
}
