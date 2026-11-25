package com.bavaria.pedidos.domain;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")
public class Administrador extends Usuario {

    @Column(length = 100)
    private String departamento;

    protected Administrador() {
    }

    public Administrador(String nombre, String email, String passwordHash, String departamento) {
        super(nombre, email, passwordHash, UsuarioType.ADMIN);
        this.departamento = departamento;
    }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }
}
