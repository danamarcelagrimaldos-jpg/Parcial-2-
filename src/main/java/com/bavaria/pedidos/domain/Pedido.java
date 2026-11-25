package com.bavaria.pedidos.domain;

import com.bavaria.pedidos.exception.TransicionEstadoInvalidaException;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pedidos", indexes = {
        @Index(name = "idx_pedido_cliente_estado", columnList = "cliente_id, estado")
})
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_aprobador_id")
    private Administrador administradorAprobador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_logistico_id")
    private Proveedor proveedorLogistico;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPedido estado = EstadoPedido.CREADO;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<DetallePedido> detalles = new ArrayList<>();

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;

    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion = LocalDateTime.now();

    protected Pedido() {
    }

    public Pedido(Cliente cliente) {
        this.cliente = cliente;
        this.estado = EstadoPedido.CREADO;
        this.fechaCreacion = LocalDateTime.now();
    }

    public void agregarDetalle(Producto producto, int cantidad) {
        DetallePedido detalle = new DetallePedido(this, producto, cantidad, producto.getPrecioUnitario());
        this.detalles.add(detalle);
        this.total = this.total.add(detalle.getSubtotal());
    }

    public void aprobar(Administrador admin) {
        if (this.estado != EstadoPedido.CREADO) {
            throw new TransicionEstadoInvalidaException("No se puede aprobar un pedido en estado: " + this.estado);
        }
        this.administradorAprobador = admin;
        this.estado = EstadoPedido.APROBADO;
    }

    public void empacar(Proveedor proveedor) {
        if (this.estado != EstadoPedido.APROBADO) {
            throw new TransicionEstadoInvalidaException("El pedido debe estar APROBADO antes de empacarse. Estado actual: " + this.estado);
        }
        this.proveedorLogistico = proveedor;
        this.estado = EstadoPedido.EMPAQUETADO;
    }

    public void enviar(Proveedor proveedor) {
        if (this.estado != EstadoPedido.EMPAQUETADO) {
            throw new TransicionEstadoInvalidaException("El pedido debe estar EMPAQUETADO antes de enviarse. Estado actual: " + this.estado);
        }
        this.proveedorLogistico = proveedor;
        this.estado = EstadoPedido.ENVIADO;
    }

    public void marcarEntregado() {
        if (this.estado != EstadoPedido.ENVIADO) {
            throw new TransicionEstadoInvalidaException("Solo se puede confirmar entrega de pedidos en estado ENVIADO. Estado actual: " + this.estado);
        }
        this.estado = EstadoPedido.ENTREGADO;
    }

    public Long getId() { return id; }
    public Cliente getCliente() { return cliente; }
    public Administrador getAdministradorAprobador() { return administradorAprobador; }
    public Proveedor getProveedorLogistico() { return proveedorLogistico; }
    public EstadoPedido getEstado() { return estado; }
    public List<DetallePedido> getDetalles() { return detalles; }
    public BigDecimal getTotal() { return total; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}
