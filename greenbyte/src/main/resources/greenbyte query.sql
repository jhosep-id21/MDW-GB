CREATE DATABASE IF NOT EXISTS greenbyte
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Opcional: crea un usuario específico para la aplicación.
-- Cambia la contraseña antes de ejecutar este bloque.
CREATE USER IF NOT EXISTS 'greenbyte'@'localhost'
    IDENTIFIED BY 'change-this-password';

GRANT ALL PRIVILEGES ON greenbyte.* TO 'greenbyte'@'localhost';
FLUSH PRIVILEGES;
