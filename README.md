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
5. Si quieres cargar los 40 productos de muestra de las antiguas páginas de categorías, ejecuta `greenbyte/src/main/resources/catalogo-demo.sql` en MySQL después del primer arranque. El script agrega los productos que aún no existen por nombre y conserva los datos del catálogo existente.

## Cuentas y roles

El registro público crea cuentas de cliente (`U`). Para preparar cuentas de prueba desde MySQL, asigna `G` para gestor o `A` para administrador en el campo `acceso` de la tabla `user`. Inicia sesión de nuevo después del cambio.

- Administrador puede elegir Administrador, Gestor o Cliente.
- Gestor puede elegir Gestor o Cliente.
- Cliente entra directamente a la tienda.

El rol activo controla el acceso durante la sesión. Administración consulta usuarios en `/administrador`, Gestor mantiene el catálogo en `/gestor` y Cliente usa Manual Byte y la compra de demostración.

## Notas

- Las contraseñas nuevas se guardan con BCrypt. Las contraseñas antiguas en texto plano se convierten a BCrypt cuando el usuario inicia sesión correctamente por primera vez.
- El carrito y el proceso de compra son demostrativos: no se procesa ningún pago ni se registra una orden real.
- `ddl-auto=update` está pensado para desarrollo local; en producción conviene usar migraciones y una estrategia de esquema controlada.
