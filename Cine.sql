-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS cine;
USE cine;

-- Crear tabla de directores
CREATE TABLE directores (
    id_director INT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    nacionalidad VARCHAR(50),
    fecha_nacimiento DATE
);

-- Crear tabla de películas
CREATE TABLE peliculas (
    codigo_pelicula VARCHAR(10) PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    anio_estreno YEAR NOT NULL,
    duracion_min INT NOT NULL,
    genero VARCHAR(50) NOT NULL,
    director_id INT,
    FOREIGN KEY (director_id) REFERENCES directores(id_director)
);

-- Insertar directores
INSERT INTO directores (id_director, nombre, nacionalidad, fecha_nacimiento)
VALUES
(1, 'Christopher Nolan', 'Británico', '1970-07-30'),
(2, 'Quentin Tarantino', 'Estadounidense', '1963-03-27'),
(3, 'Greta Gerwig', 'Estadounidense', '1983-08-04'),
(4, 'Bong Joon-ho', 'Coreano', '1969-09-14');

-- Insertar películas
INSERT INTO peliculas (codigo_pelicula, titulo, anio_estreno, duracion_min, genero, director_id)
VALUES
('MV201', 'The Batman Part II', 2025, 150, 'Acción', 1),
('MV202', 'Superman: Legacy', 2025, 135, 'Superhéroes', 2),
('MV203', 'Gladiator 2', 2025, 160, 'Épica', 3),
('MV204', 'Rebel Moon: Part Three', 2025, 145, 'Ciencia Ficción', 4);



