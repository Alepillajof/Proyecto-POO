-- ==========================================
-- BASE DE DATOS: GESTION EDUCATIVA
-- ==========================================

DROP DATABASE IF EXISTS gestion_educativa;
CREATE DATABASE gestion_educativa;
USE gestion_educativa;

-- ==========================================
-- TABLA USUARIOS
-- ==========================================

CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    rol ENUM('ADMIN','PROFESOR','ESTUDIANTE') NOT NULL
);

-- ==========================================
-- TABLA PROFESORES
-- ==========================================

CREATE TABLE profesores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    especialidad VARCHAR(100) NOT NULL,
    correo VARCHAR(100)
);

-- ==========================================
-- TABLA ESTUDIANTES
-- ==========================================

CREATE TABLE estudiantes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    cedula VARCHAR(10) UNIQUE NOT NULL,
    carrera VARCHAR(100) NOT NULL,
    nivel INT NOT NULL,
    correo VARCHAR(100)
);

-- ==========================================
-- TABLA REPORTES
-- ==========================================

CREATE TABLE reportes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descripcion TEXT,
    fecha DATE NOT NULL,
    usuario_id INT,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
);

-- ==========================================
-- USUARIOS DE PRUEBA
-- ==========================================

INSERT INTO usuarios
(nombre, apellido, correo, usuario, contrasena, rol)
VALUES
('Administrador','Sistema','admin@epn.edu.ec','admin','123456','ADMIN'),

('Carlos','Lopez','profesor@epn.edu.ec','profesor','123456','PROFESOR'),

('Maria','Perez','estudiante@epn.edu.ec','estudiante','123456','ESTUDIANTE');

-- ==========================================
-- PROFESORES DE PRUEBA
-- ==========================================

INSERT INTO profesores
(nombre, apellido, cedula, especialidad, correo)
VALUES
('Carlos','Lopez','1723456789','Programación','profesor@epn.edu.ec'),

('Ana','Vera','1712345678','Matemáticas','ana@epn.edu.ec');

-- ==========================================
-- ESTUDIANTES DE PRUEBA
-- ==========================================

INSERT INTO estudiantes
(nombre, apellido, cedula, carrera, nivel, correo)
VALUES
('Maria','Perez','1756789123','Desarrollo de Software',3,'estudiante@epn.edu.ec'),

('Luis','Mora','1765432198','Redes',2,'luis@epn.edu.ec');

-- ==========================================
-- REPORTES DE PRUEBA
-- ==========================================

INSERT INTO reportes
(titulo, descripcion, fecha, usuario_id)
VALUES
('Reporte Académico',
'Primer reporte del sistema',
CURDATE(),
1);