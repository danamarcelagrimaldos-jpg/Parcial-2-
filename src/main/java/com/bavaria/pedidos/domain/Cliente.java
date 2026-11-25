package com.bavaria.pedidos.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {

    @Column(name = "direccion_entrega", length = 250)
    private String direccionEntrega;

    @OneToOne(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Carrito carrito;

    protected Cliente() {
    }

    public Cliente(String nombre, String email, String passwordHash, String direccionEntrega) {
        super(nombre, email, passwordHash, UsuarioType.CLIENTE);
        this.direccionEntrega = direccionEntrega;
        this.carrito = new Carrito(this);
    }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) { this.direccionEntrega = direccionEntrega; }
    public Carrito getCarrito() { return carrito; }
}
