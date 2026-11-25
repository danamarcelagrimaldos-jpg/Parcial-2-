package com.bavaria.pedidos.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("PROVEEDOR")
public class Proveedor extends Usuario {

    @Column(name = "empresa_transportadora", length = 120)
    private String empresaTransportadora;

    protected Proveedor() {
    }

    public Proveedor(String nombre, String email, String passwordHash, String empresaTransportadora) {
        super(nombre, email, passwordHash, UsuarioType.PROVEEDOR);
        this.empresaTransportadora = empresaTransportadora;
    }

    public String getEmpresaTransportadora() { return empresaTransportadora; }
    public void setEmpresaTransportadora(String empresaTransportadora) { this.empresaTransportadora = empresaTransportadora; }
}
