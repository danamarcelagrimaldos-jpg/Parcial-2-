package com.bavaria.pedidos.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoEventListener {

    private static final Logger log = LoggerFactory.getLogger(PedidoEventListener.class);

    @EventListener
    public void onCambioEstadoPedido(CambioEstadoPedidoEvent event) {
        log.info("[NOTIFICACIÓN BAVARIA] Pedido #{} cambió a estado {}. Enviando correo a {}",
                event.pedidoId(), event.nuevoEstado(), event.clienteEmail());
    }
}
