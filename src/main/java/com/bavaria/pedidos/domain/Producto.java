package com.bavaria.pedidos.domain;

import com.bavaria.pedidos.exception.StockInsuficienteException;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "productos", indexes = {
        @Index(name = "idx_prod_sku", columnList = "sku", unique = true),
        @Index(name = "idx_prod_nombre", columnList = "nombre")
})
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String sku;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(name = "precio_unitario", nullable = false, precision = 12, scale = 2)
    private BigDecimal precioUnitario;

    @Column(name = "stock_disponible", nullable = false)
    private Integer stockDisponible;

    protected Producto() {
    }

    public Producto(String sku, String nombre, BigDecimal precioUnitario, Integer stockDisponible) {
        this.sku = sku;
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.stockDisponible = stockDisponible;
    }

    public void descontarStock(int cantidad) {
        if (cantidad <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero.");
        }
        if (this.stockDisponible < cantidad) {
            throw new StockInsuficienteException("Stock insuficiente para el producto: " + this.nombre +
                    ". Disponible: " + this.stockDisponible + ", solicitado: " + cantidad);
        }
        this.stockDisponible -= cantidad;
    }

    public Long getId() { return id; }
    public String getSku() { return sku; }
    public String getNombre() { return nombre; }
    public BigDecimal getPrecioUnitario() { return precioUnitario; }
    public Integer getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(Integer stockDisponible) { this.stockDisponible = stockDisponible; }
}
