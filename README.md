# Proyecto Bavaria S.A.S. — Backend Spring Boot (Parcial 2)

Sistema de gestión logística y pedidos B2B refactorizado bajo **Arquitectura en Capas**, principios **SOLID**, persistencia **Spring Data JPA**, control de concurrencia y manejo global de excepciones.

## Estructura del Proyecto
- `src/main/java/com/bavaria/pedidos/domain`: Entidades `@Entity` (`Usuario`, `Cliente`, `Administrador`, `Proveedor`, `Producto`, `Carrito`, `ItemCarrito`, `Pedido`, `DetallePedido`), `UsuarioFactory` y enumeraciones.
- `src/main/java/com/bavaria/pedidos/repository`: Interfaces `JpaRepository` con bloqueo pesimista e índices compuestos.
- `src/main/java/com/bavaria/pedidos/service`: Lógica de negocio transaccional (`@Transactional`) y publicación de eventos.
- `src/main/java/com/bavaria/pedidos/controller`: Endpoints RESTful (`/api/v1/usuarios`, `/api/v1/productos`, `/api/v1/pedidos`).
- `src/main/java/com/bavaria/pedidos/dto`: Objetos de transferencia de datos (`record` con validaciones Jakarta).
- `src/main/java/com/bavaria/pedidos/exception`: Manejo centralizado de errores con `@RestControllerAdvice` (HTTP 404, 409, 422).
- `docs/hito-2`: Diagramas C4/UML en formato Mermaid y Registros de Decisiones Arquitectónicas (`ADRs.md`).

## Ejecución Rápida
1. Abrir el proyecto como proyecto Maven en IntelliJ IDEA.
2. Ejecutar `ProyectoBavariaApplication.java`.
3. `DataSeeder` precarga automáticamente los usuarios (`Juan`, `Admin`, `Proveedor1`) y los productos (`Aguila lata 330ml`, `Club Colombia lata 330ml`).
