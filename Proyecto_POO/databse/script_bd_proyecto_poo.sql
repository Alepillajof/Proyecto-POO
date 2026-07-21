CREATE DATABASE proyecto_poo;

USE proyecto_poo;

CREATE TABLE usuarios (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(150) UNIQUE NOT NULL,
    usuario VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL -- 'ESTUDIANTE' o 'PROFESOR'
);

CREATE TABLE estudiantes (
    id INT PRIMARY KEY,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    carrera VARCHAR(100),
    nivel INT,
    FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE profesores (
    id INT PRIMARY KEY,
    cedula VARCHAR(20) UNIQUE NOT NULL,
    especialidad VARCHAR(100),
    FOREIGN KEY (id) REFERENCES usuarios(id) ON DELETE CASCADE
);

CREATE TABLE asignaciones (
    id INT PRIMARY KEY AUTO_INCREMENT,
    profesor_id INT NOT NULL,
    estudiante_id INT NOT NULL,
    estado VARCHAR(20) DEFAULT 'PENDIENTE', 
    FOREIGN KEY (profesor_id) REFERENCES profesores(id),
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id)
);

CREATE TABLE reportes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    fecha DATE NOT NULL,
    usuario_id INT NOT NULL, -- ID del estudiante asignado[cite: 5]
    profesor_id INT NOT NULL, -- ID del profesor que emite[cite: 5]
    FOREIGN KEY (usuario_id) REFERENCES estudiantes(id),
    FOREIGN KEY (profesor_id) REFERENCES profesores(id)
);
