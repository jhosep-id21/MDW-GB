# GreenByte — Marcos de Desarrollo Web

Aplicación web de catálogo y gestión de productos para una tienda de plantas.
Desarrollada con Java, Spring Boot, Spring MVC, Thymeleaf, Bootstrap y MySQL.

## Estructura

- `greenbyte/`: aplicación Spring Boot.
  - `src/main/java`: controladores, modelos, repositorios y configuración.
  - `src/main/resources/templates`: vistas Thymeleaf y fragmentos reutilizables.
  - `src/main/resources/static`: CSS, JavaScript e imágenes.
  - `src/test`: pruebas automatizadas.

## Requisitos

- JDK 25 (el proyecto declara Java 25).
- MySQL 8 o compatible.
- Maven Wrapper incluido.

## Puesta en marcha

1. Crea la base de datos ejecutando `greenbyte/src/main/resources/greenbyte query.sql` en MySQL.
2. Configura las variables de entorno antes de iniciar la aplicación:

   **Windows PowerShell**
   ```powershell
   $env:DB_URL="jdbc:mysql://localhost:3306/greenbyte?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
   $env:DB_USERNAME="greenbyte"
   $env:DB_PASSWORD="tu contraseña"
   ```

   **Linux / macOS**
   ```bash
   export DB_URL="jdbc:mysql://localhost:3306/greenbyte?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true"
   export DB_USERNAME="greenbyte"
   export DB_PASSWORD="tu contraseña"
   ```

3. Desde la carpeta `greenbyte`, ejecuta:
   ```bash
   ./mvnw spring-boot:run
   ```
   En Windows:
   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

4. Abre `http://localhost:8080`.

## Configuración del administrador

El panel `/administrador` está restringido a sesiones con rol `ADMIN`.
Para asignar ese rol a una cuenta de prueba, actualiza el campo `acceso` a `A` en la tabla `user` desde MySQL. No existe un registro público de administradores.

## Notas

- Las contraseñas nuevas se guardan con BCrypt. Las contraseñas antiguas en texto plano se convierten a BCrypt cuando el usuario inicia sesión correctamente por primera vez.
- El carrito y el proceso de compra son demostrativos: no se procesa ningún pago ni se registra una orden real.
- `ddl-auto=update` está pensado para desarrollo local; en producción conviene usar migraciones y una estrategia de esquema controlada.
