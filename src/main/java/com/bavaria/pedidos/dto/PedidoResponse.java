package com.bavaria.pedidos.dto;

import com.bavaria.pedidos.domain.EstadoPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
        Long id,
        String clienteNombre,
        String adminAprobador,
        String proveedorLogistico,
        EstadoPedido estado,
        BigDecimal total,
        LocalDateTime fechaCreacion,
        List<DetallePedidoResponse> detalles
) {}
