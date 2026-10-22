
package com.academico.infrastructure.adapter.out.persistence;

import com.academico.domain.model.Curso;
import com.academico.domain.repository.CursoRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CursoRepositoryPostgres implements CursoRepository {

    private final String url;
    private final String usuario;
    private final String password;

    // Configuracion de conexion a PostgreSQL
    public CursoRepositoryPostgres() {
        this(
            System.getenv().getOrDefault(
                "DB_URL",
                "jdbc:postgresql://localhost:5432/sistema_gestion_academica"
            ),
            System.getenv().getOrDefault("DB_USER", "postgres"),
            System.getenv("DB_PASSWORD")
        );
    }

    // Constructor para inyeccion de configuracion
    public CursoRepositoryPostgres(
            String url,
            String usuario,
            String password) {

        this.url = Objects.requireNonNull(url);
        this.usuario = Objects.requireNonNull(usuario);
        this.password = password;
    }

    // Establecer conexion JDBC
    private Connection conectar() throws SQLException {

        if (password == null || password.isBlank()) {
            throw new IllegalStateException(
                "Configura la variable de entorno DB_PASSWORD."
            );
        }

        return DriverManager.getConnection(
            url, usuario, password
        );
    }

    // CASO 1: Listar cursos desde PostgreSQL
    @Override
    public List<Curso> listar() {

        String sql = """
            SELECT id, nombre, creditos
            FROM cursos
            ORDER BY id
            """;

        List<Curso> cursos = new ArrayList<>();

        try (
            Connection con = conectar();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Curso curso = new Curso(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getInt("creditos")
                );

                cursos.add(curso);
            }

            return cursos;

        } catch (SQLException e) {

            throw new IllegalStateException(
                "Error al listar cursos desde PostgreSQL.", e
            );
        }
    }

    // CASO 2: Sincronizar cursos con PostgreSQL
    @Override
    public void guardar(List<Curso> cursos) {

        Objects.requireNonNull(
            cursos, "La lista de cursos no puede ser nula"
        );

        String upsert = """
            INSERT INTO cursos (id, nombre, creditos)
            VALUES (?, ?, ?)
            ON CONFLICT (id)
            DO UPDATE SET
                nombre = EXCLUDED.nombre,
                creditos = EXCLUDED.creditos
            """;

        String eliminarAusentes = """
            DELETE FROM cursos
            WHERE NOT (id = ANY (?))
            """;

        try (Connection con = conectar()) {

            con.setAutoCommit(false);

            try {
                // Evitar cambios concurrentes durante la sincronizacion
                try (Statement st = con.createStatement()) {
                    st.execute(
                        "LOCK TABLE cursos IN EXCLUSIVE MODE"
                    );
                }

                // Insertar o actualizar los cursos
                try (
                    PreparedStatement ps =
                        con.prepareStatement(upsert)
                ) {

                    for (Curso curso : cursos) {

                        ps.setInt(1, curso.getId());
                        ps.setString(2, curso.getNombre());
                        ps.setInt(3, curso.getCreditos());

                        ps.addBatch();
                    }

                    ps.executeBatch();
                }

                // Eliminar cursos que ya no estan en la lista
                Integer[] ids = cursos.stream()
                    .map(Curso::getId)
                    .toArray(Integer[]::new);

                Array idsSql = con.createArrayOf(
                    "integer", ids
                );

                try (
                    PreparedStatement ps =
                        con.prepareStatement(eliminarAusentes)
                ) {

                    ps.setArray(1, idsSql);
                    ps.executeUpdate();

                } finally {
                    idsSql.free();
                }

                // Confirmar todos los cambios
                con.commit();

            } catch (Exception e) {

                con.rollback();

                throw new IllegalStateException(
                    "Error al sincronizar cursos con PostgreSQL.",
                    e
                );
            }

        } catch (SQLException e) {

            throw new IllegalStateException(
                "Error de conexion con PostgreSQL.", e
            );
        }
    }
}
