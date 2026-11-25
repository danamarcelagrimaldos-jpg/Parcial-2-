package com.bavaria.pedidos.controller;

import com.bavaria.pedidos.dto.AgregarItemCarritoRequest;
import com.bavaria.pedidos.dto.PedidoResponse;
import com.bavaria.pedidos.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping("/carrito/{clienteId}/items")
    public ResponseEntity<Void> agregarAlCarrito(@PathVariable Long clienteId,
                                                 @Valid @RequestBody AgregarItemCarritoRequest request) {
        pedidoService.agregarProductoAlCarrito(clienteId, request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/cliente/{clienteId}/confirmar")
    public ResponseEntity<PedidoResponse> confirmarPedido(@PathVariable Long clienteId) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pedidoService.confirmarPedido(clienteId));
    }

    @PatchMapping("/{pedidoId}/aprobar")
    public ResponseEntity<PedidoResponse> aprobarPedido(@PathVariable Long pedidoId,
                                                        @RequestParam Long adminId) {
        return ResponseEntity.ok(pedidoService.aprobarPedido(pedidoId, adminId));
    }

    @PatchMapping("/{pedidoId}/empacar")
    public ResponseEntity<PedidoResponse> empacarPedido(@PathVariable Long pedidoId,
                                                        @RequestParam Long proveedorId) {
        return ResponseEntity.ok(pedidoService.empacarPedido(pedidoId, proveedorId));
    }

    @PatchMapping("/{pedidoId}/enviar")
    public ResponseEntity<PedidoResponse> enviarPedido(@PathVariable Long pedidoId,
                                                       @RequestParam Long proveedorId) {
        return ResponseEntity.ok(pedidoService.enviarPedido(pedidoId, proveedorId));
    }

    @PatchMapping("/{pedidoId}/entregar")
    public ResponseEntity<PedidoResponse> confirmarEntrega(@PathVariable Long pedidoId,
                                                           @RequestParam Long clienteId) {
        return ResponseEntity.ok(pedidoService.confirmarEntrega(pedidoId, clienteId));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PedidoResponse>> listarPorCliente(@PathVariable Long clienteId) {
        return ResponseEntity.ok(pedidoService.listarPedidosPorCliente(clienteId));
    }
}
