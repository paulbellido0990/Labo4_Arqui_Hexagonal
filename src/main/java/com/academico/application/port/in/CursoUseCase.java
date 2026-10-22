
package com.academico.application.port.in;

import com.academico.domain.model.Curso;
import java.util.List;

public interface CursoUseCase {

    void registrar(Curso curso);

    List<Curso> listar();

    boolean actualizar(Curso curso);

    boolean eliminar(int id);
}
