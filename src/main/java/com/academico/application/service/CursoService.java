
package com.academico.application.service;

import com.academico.application.port.in.CursoUseCase;
import com.academico.domain.model.Curso;
import com.academico.domain.repository.CursoRepository;

import java.util.List;
import java.util.Objects;

public class CursoService implements CursoUseCase {

    private final CursoRepository repository;

    // Inyeccion de dependencias mediante constructor
    public CursoService(CursoRepository repository) {
        this.repository = Objects.requireNonNull(
                repository,
                "El repositorio de cursos no puede ser nulo"
        );
    }

    // CASO DE USO: Registrar un curso
    @Override
    public void registrar(Curso curso) {

        List<Curso> cursos = repository.listar();

        cursos.add(curso);

        repository.guardar(cursos);
    }

    // CASO DE USO: Listar cursos
    @Override
    public List<Curso> listar() {
        return repository.listar();
    }

    // CASO DE USO: Actualizar curso
    @Override
    public boolean actualizar(Curso curso) {

        List<Curso> cursos = repository.listar();

        for (Curso c : cursos) {

            if (c.getId() == curso.getId()) {

                c.setNombre(curso.getNombre());
                c.setCreditos(curso.getCreditos());

                repository.guardar(cursos);

                return true;
            }
        }

        return false;
    }

    // CASO DE USO: Eliminar curso
    @Override
    public boolean eliminar(int id) {

        List<Curso> cursos = repository.listar();

        boolean eliminado = cursos.removeIf(
                c -> c.getId() == id
        );

        if (eliminado) {
            repository.guardar(cursos);
        }

        return eliminado;
    }
}

