package com.bavaria.pedidos.dto;

import java.math.BigDecimal;

public record ProductoResponse(
        Long id,
        String sku,
        String nombre,
        BigDecimal precioUnitario,
        Integer stockDisponible
) {}
