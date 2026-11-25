package com.bavaria.pedidos.event;

import com.bavaria.pedidos.domain.EstadoPedido;

public record CambioEstadoPedidoEvent(
        Long pedidoId,
        String clienteEmail,
        EstadoPedido nuevoEstado
) {}
