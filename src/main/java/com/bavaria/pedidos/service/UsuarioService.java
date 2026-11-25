package com.bavaria.pedidos.service;

import com.bavaria.pedidos.domain.Usuario;
import com.bavaria.pedidos.domain.UsuarioFactory;
import com.bavaria.pedidos.dto.CrearUsuarioRequest;
import com.bavaria.pedidos.dto.UsuarioResponse;
import com.bavaria.pedidos.exception.ReglaNegocioException;
import com.bavaria.pedidos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public UsuarioResponse registrarUsuario(CrearUsuarioRequest request) {
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ReglaNegocioException("Ya existe un usuario registrado con el email: " + request.email());
        }
        Usuario nuevo = UsuarioFactory.crearUsuario(
                request.nombre(),
                request.email(),
                request.password(), // En producción aplicar BCryptPasswordEncoder
                request.rol(),
                request.datoExtra()
        );
        Usuario guardado = usuarioRepository.save(nuevo);
        return new UsuarioResponse(guardado.getId(), guardado.getNombre(), guardado.getEmail(), guardado.getRol());
    }

    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> new UsuarioResponse(u.getId(), u.getNombre(), u.getEmail(), u.getRol()))
                .toList();
    }
}
