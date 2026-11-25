package com.bavaria.pedidos.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalle_pedidos")
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_historico", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioHistorico;

    protected DetallePedido() {
    }

    public DetallePedido(Pedido pedido, Producto producto, Integer cantidad, BigDecimal precioHistorico) {
        this.pedido = pedido;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioHistorico = precioHistorico;
    }

    public BigDecimal getSubtotal() {
        return precioHistorico.multiply(BigDecimal.valueOf(cantidad));
    }

    public Long getId() { return id; }
    public Producto getProducto() { return producto; }
    public Integer getCantidad() { return cantidad; }
    public BigDecimal getPrecioHistorico() { return precioHistorico; }
}
