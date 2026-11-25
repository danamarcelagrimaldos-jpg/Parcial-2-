package com.bavaria.pedidos.domain;

public class UsuarioFactory {

    private UsuarioFactory() {
    }

    public static Usuario crearUsuario(String nombre, String email, String passwordHash, UsuarioType tipo, String datoExtra) {
        return switch (tipo) {
            case CLIENTE -> new Cliente(nombre, email, passwordHash, datoExtra);
            case ADMIN -> new Administrador(nombre, email, passwordHash, datoExtra);
            case PROVEEDOR -> new Proveedor(nombre, email, passwordHash, datoExtra);
        };
    }
}
