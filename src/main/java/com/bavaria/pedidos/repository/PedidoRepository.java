package com.bavaria.pedidos.repository;

import com.bavaria.pedidos.domain.EstadoPedido;
import com.bavaria.pedidos.domain.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    List<Pedido> findByClienteIdAndEstado(Long clienteId, EstadoPedido estado);
    List<Pedido> findByClienteId(Long clienteId);
}
