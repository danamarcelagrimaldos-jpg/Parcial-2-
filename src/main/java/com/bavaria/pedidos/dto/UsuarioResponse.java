package com.bavaria.pedidos.dto;

import com.bavaria.pedidos.domain.UsuarioType;

public record UsuarioResponse(
        Long id,
        String nombre,
        String email,
        UsuarioType rol
) {}
