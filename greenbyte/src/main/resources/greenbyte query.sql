create database greenbyte
CREATE USER IF NOT EXISTS'greenbyte'@'localhost'
  IDENTIFIED BY 'gbyte2026';

GRANT ALL PRIVILEGES ON greenbyte.* TO 'greenbyte'@'localhost';