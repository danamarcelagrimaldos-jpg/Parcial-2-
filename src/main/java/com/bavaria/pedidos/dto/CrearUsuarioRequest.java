package com.bavaria.pedidos.dto;

import com.bavaria.pedidos.domain.UsuarioType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CrearUsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El email es obligatorio") @Email(message = "Formato de email inválido") String email,
        @NotBlank(message = "La contraseña es requerida") String password,
        @NotNull(message = "El tipo de usuario es obligatorio") UsuarioType rol,
        String datoExtra
) {}
