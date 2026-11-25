package com.bavaria.pedidos.dto;

import java.math.BigDecimal;

public record DetallePedidoResponse(
        Long productoId,
        String productoNombre,
        Integer cantidad,
        BigDecimal precioHistorico,
        BigDecimal subtotal
) {}
