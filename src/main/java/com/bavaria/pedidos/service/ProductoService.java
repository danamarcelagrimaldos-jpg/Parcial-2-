package com.bavaria.pedidos.service;

import com.bavaria.pedidos.dto.ProductoResponse;
import com.bavaria.pedidos.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductoResponse> listarCatalogo() {
        return productoRepository.findAll().stream()
                .map(p -> new ProductoResponse(p.getId(), p.getSku(), p.getNombre(), p.getPrecioUnitario(), p.getStockDisponible()))
                .toList();
    }
}
