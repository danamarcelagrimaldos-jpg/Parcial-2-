package com.bavaria.pedidos.service;

import com.bavaria.pedidos.domain.*;
import com.bavaria.pedidos.dto.*;
import com.bavaria.pedidos.event.CambioEstadoPedidoEvent;
import com.bavaria.pedidos.exception.RecursoNoEncontradoException;
import com.bavaria.pedidos.exception.ReglaNegocioException;
import com.bavaria.pedidos.repository.PedidoRepository;
import com.bavaria.pedidos.repository.ProductoRepository;
import com.bavaria.pedidos.repository.UsuarioRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;
    private final ApplicationEventPublisher eventPublisher;

    public PedidoService(PedidoRepository pedidoRepository,
                         UsuarioRepository usuarioRepository,
                         ProductoRepository productoRepository,
                         ApplicationEventPublisher eventPublisher) {
        this.pedidoRepository = pedidoRepository;
        this.usuarioRepository = usuarioRepository;
        this.productoRepository = productoRepository;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public void agregarProductoAlCarrito(Long clienteId, AgregarItemCarritoRequest request) {
        Cliente cliente = obtenerCliente(clienteId);
        Producto producto = productoRepository.findById(request.productoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado con ID: " + request.productoId()));

        if (producto.getStockDisponible() < request.cantidad()) {
            throw new ReglaNegocioException("Stock insuficiente para agregar al carrito. Disponible: " + producto.getStockDisponible());
        }

        cliente.getCarrito().agregarProducto(producto, request.cantidad());
        usuarioRepository.save(cliente);
    }

    @Transactional
    public PedidoResponse confirmarPedido(Long clienteId) {
        Cliente cliente = obtenerCliente(clienteId);
        Carrito carrito = cliente.getCarrito();

        if (carrito.getItems().isEmpty()) {
            throw new ReglaNegocioException("No se puede confirmar un pedido porque el carrito está vacío.");
        }

        Pedido pedido = new Pedido(cliente);

        for (ItemCarrito item : carrito.getItems()) {
            Producto productoBloqueado = productoRepository.findByIdForUpdate(item.getProducto().getId())
                    .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado"));
            productoBloqueado.descontarStock(item.getCantidad());
            productoRepository.save(productoBloqueado);

            pedido.agregarDetalle(productoBloqueado, item.getCantidad());
        }

        carrito.vaciar();
        Pedido guardado = pedidoRepository.save(pedido);
        eventPublisher.publishEvent(new CambioEstadoPedidoEvent(guardado.getId(), cliente.getEmail(), guardado.getEstado()));

        return mapToResponse(guardado);
    }

    @Transactional
    public PedidoResponse aprobarPedido(Long pedidoId, Long adminId) {
        Pedido pedido = obtenerPedido(pedidoId);
        Administrador admin = obtenerAdmin(adminId);

        pedido.aprobar(admin);
        Pedido actualizado = pedidoRepository.save(pedido);
        eventPublisher.publishEvent(new CambioEstadoPedidoEvent(actualizado.getId(), actualizado.getCliente().getEmail(), actualizado.getEstado()));

        return mapToResponse(actualizado);
    }

    @Transactional
    public PedidoResponse empacarPedido(Long pedidoId, Long proveedorId) {
        Pedido pedido = obtenerPedido(pedidoId);
        Proveedor proveedor = obtenerProveedor(proveedorId);

        pedido.empacar(proveedor);
        Pedido actualizado = pedidoRepository.save(pedido);
        eventPublisher.publishEvent(new CambioEstadoPedidoEvent(actualizado.getId(), actualizado.getCliente().getEmail(), actualizado.getEstado()));

        return mapToResponse(actualizado);
    }

    @Transactional
    public PedidoResponse enviarPedido(Long pedidoId, Long proveedorId) {
        Pedido pedido = obtenerPedido(pedidoId);
        Proveedor proveedor = obtenerProveedor(proveedorId);

        pedido.enviar(proveedor);
        Pedido actualizado = pedidoRepository.save(pedido);
        eventPublisher.publishEvent(new CambioEstadoPedidoEvent(actualizado.getId(), actualizado.getCliente().getEmail(), actualizado.getEstado()));

        return mapToResponse(actualizado);
    }

    @Transactional
    public PedidoResponse confirmarEntrega(Long pedidoId, Long clienteId) {
        Pedido pedido = obtenerPedido(pedidoId);
        if (!pedido.getCliente().getId().equals(clienteId)) {
            throw new ReglaNegocioException("Solo el cliente propietario puede confirmar la entrega de este pedido.");
        }

        pedido.marcarEntregado();
        Pedido actualizado = pedidoRepository.save(pedido);
        eventPublisher.publishEvent(new CambioEstadoPedidoEvent(actualizado.getId(), actualizado.getCliente().getEmail(), actualizado.getEstado()));

        return mapToResponse(actualizado);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponse> listarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Cliente obtenerCliente(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Cliente no encontrado con ID: " + id));
        if (!(u instanceof Cliente c)) {
            throw new ReglaNegocioException("El usuario con ID " + id + " no tiene rol CLIENTE.");
        }
        return c;
    }

    private Administrador obtenerAdmin(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Administrador no encontrado con ID: " + id));
        if (!(u instanceof Administrador a)) {
            throw new ReglaNegocioException("El usuario con ID " + id + " no tiene rol ADMIN.");
        }
        return a;
    }

    private Proveedor obtenerProveedor(Long id) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Proveedor no encontrado con ID: " + id));
        if (!(u instanceof Proveedor p)) {
            throw new ReglaNegocioException("El usuario con ID " + id + " no tiene rol PROVEEDOR.");
        }
        return p;
    }

    private Pedido obtenerPedido(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Pedido no encontrado con ID: " + id));
    }

    private PedidoResponse mapToResponse(Pedido pedido) {
        List<DetallePedidoResponse> detalles = pedido.getDetalles().stream()
                .map(d -> new DetallePedidoResponse(
                        d.getProducto().getId(),
                        d.getProducto().getNombre(),
                        d.getCantidad(),
                        d.getPrecioHistorico(),
                        d.getSubtotal()
                )).toList();

        return new PedidoResponse(
                pedido.getId(),
                pedido.getCliente().getNombre(),
                pedido.getAdministradorAprobador() != null ? pedido.getAdministradorAprobador().getNombre() : null,
                pedido.getProveedorLogistico() != null ? pedido.getProveedorLogistico().getNombre() : null,
                pedido.getEstado(),
                pedido.getTotal(),
                pedido.getFechaCreacion(),
                detalles
        );
    }
}
