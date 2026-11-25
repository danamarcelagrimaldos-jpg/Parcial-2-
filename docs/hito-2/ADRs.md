# Registros de Decisiones Arquitectónicas (ADRs) - Proyecto Bavaria

## ADR-001: Adopción de Arquitectura en Capas sobre Spring Boot
- **Estado:** Aceptada
- **Contexto y Problema:** El prototipo inicial en consola mezclaba entrada/salida (`System.out.println`), estado en memoria (`SystemManager` con `HashMap`) y reglas de negocio en las mismas clases de dominio, impidiendo escalabilidad web y pruebas unitarias.
- **Alternativas Evaluadas:** 1) Microservicios; 2) Arquitectura Hexagonal; 3) Arquitectura en Capas con Spring Boot.
- **Decisión Tomada:** Implementar una Arquitectura en Capas (`controller`, `service`, `repository`, `domain`, `dto`, `exception`) con inyección de dependencias por constructor.
- **Consecuencias:** Positivo: Alta cohesión, soporte nativo para `@Transactional` y facilidad de mantenimiento. Negativo: Requiere clases DTO adicionales para desacoplar las entidades JPA.

## ADR-002: Persistencia Relacional con PostgreSQL y Spring Data JPA
- **Estado:** Aceptada
- **Contexto y Problema:** El almacenamiento en `HashMap` en memoria provocaba pérdida de pedidos al reiniciar y no soportaba concurrencia multiusuario.
- **Alternativas Evaluadas:** 1) MongoDB; 2) MySQL 8; 3) PostgreSQL 16.
- **Decisión Tomada:** Seleccionar PostgreSQL 16 junto con Spring Data JPA, usando `@Enumerated(EnumType.STRING)`, `FetchType.LAZY` y bloqueo pesimista (`@Lock(LockModeType.PESSIMISTIC_WRITE)`) en inventario.
- **Consecuencias:** Positivo: Garantía ACID estricta e integridad referencial. Negativo: Requiere gestión controlada de esquema.

## ADR-003: Máquina de Estados Estricta y Manejo Centralizado de Excepciones HTTP
- **Estado:** Aceptada
- **Contexto y Problema:** En el código base no se validaban transiciones entre estados y los errores se imprimían en consola.
- **Alternativas Evaluadas:** 1) `try-catch` en cada endpoint; 2) Manejo global mediante `@RestControllerAdvice` y validaciones encapsuladas en la entidad `Pedido`.
- **Decisión Tomada:** Validar el flujo (`CREADO -> APROBADO -> EMPAQUETADO -> ENVIADO -> ENTREGADO`) en `Pedido` y retornar respuestas estándar RFC 7807 (`409 Conflict`, `422 Unprocessable Entity`, `404 Not Found`) vía `GlobalExceptionHandler`.
- **Consecuencias:** Positivo: Cero estados inconsistentes y respuestas HTTP uniformes. Negativo: Mayor cobertura requerida en pruebas de integración.
