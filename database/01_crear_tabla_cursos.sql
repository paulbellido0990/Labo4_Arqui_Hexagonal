
-- Laboratorio 4: Arquitectura Hexagonal
-- Sistema de Gestion Academica
-- Tabla para la persistencia de cursos en PostgreSQL

CREATE TABLE IF NOT EXISTS cursos (
    id INTEGER PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    creditos INTEGER NOT NULL,
    CONSTRAINT chk_cursos_creditos
        CHECK (creditos > 0)
);

-- Verificar la estructura de la tabla
SELECT column_name, data_type, is_nullable
FROM information_schema.columns
WHERE table_schema = 'public'
  AND table_name = 'cursos'
ORDER BY ordinal_position;
