package com.academico.application.service;

import com.academico.application.port.in.CursoUseCase;
import com.academico.domain.model.Curso;
import com.academico.domain.repository.CursoRepository;
import com.academico.infrastructure.adapter.out.persistence.CursoRepositoryJson;

import java.util.List;
import java.util.Objects;

public class CursoService implements CursoUseCase {

    private final CursoRepository repository;

    public CursoService(CursoRepository repository) {
        this.repository = Objects.requireNonNull(
                repository,
                "El repositorio de cursos no puede ser nulo"
        );
    }

    @Deprecated
    public CursoService() {
        this(new CursoRepositoryJson());
    }

    @Override
    public void registrar(Curso curso) {

        List<Curso> cursos = repository.listar();

        cursos.add(curso);

        repository.guardar(cursos);
    }

    @Override
    public List<Curso> listar() {
        return repository.listar();
    }

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
