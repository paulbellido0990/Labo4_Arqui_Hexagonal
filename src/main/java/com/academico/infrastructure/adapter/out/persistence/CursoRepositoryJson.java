package com.academico.infrastructure.adapter.out.persistence;

import com.academico.domain.model.Curso;
import com.academico.domain.repository.CursoRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CursoRepositoryJson implements CursoRepository {

    private final String archivo = "data/cursos.json";
    private final Gson gson = new Gson();

    @Override
    public List<Curso> listar() {

        try (Reader reader = new FileReader(archivo)) {

            Type tipo = new TypeToken<List<Curso>>() {}.getType();

            List<Curso> cursos = gson.fromJson(reader, tipo);

            return cursos != null
                    ? cursos
                    : new ArrayList<>();

        } catch (IOException e) {

            System.out.println(
                "Error al leer cursos: " + e.getMessage()
            );

            return new ArrayList<>();
        }
    }

    @Override
    public void guardar(List<Curso> cursos) {

        try (Writer writer = new FileWriter(archivo)) {

            gson.toJson(cursos, writer);

        } catch (IOException e) {

            throw new IllegalStateException(
                "Error al guardar cursos en JSON", e
            );
        }
    }
}
