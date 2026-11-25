package com.bavaria.pedidos.config;

import com.bavaria.pedidos.domain.*;
import com.bavaria.pedidos.repository.ProductoRepository;
import com.bavaria.pedidos.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.math.BigDecimal;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initData(UsuarioRepository usuarioRepository, ProductoRepository productoRepository) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                usuarioRepository.save(UsuarioFactory.crearUsuario("Juan", "juan@tienda.com", "123456", UsuarioType.CLIENTE, "Calle 50 # 42-10, Medellín"));
                usuarioRepository.save(UsuarioFactory.crearUsuario("Admin", "admin@bavaria.com", "admin123", UsuarioType.ADMIN, "Logística Central"));
                usuarioRepository.save(UsuarioFactory.crearUsuario("Proveedor1", "logistica@proveedor.com", "prov123", UsuarioType.PROVEEDOR, "Transportes Bavaria S.A.S."));
            }

            if (productoRepository.count() == 0) {
                productoRepository.save(new Producto("AGU-330", "Aguila lata 330ml", new BigDecimal("3500.00"), 500));
                productoRepository.save(new Producto("CLB-330", "Club Colombia lata 330ml", new BigDecimal("4200.00"), 350));
            }
        };
    }
}
