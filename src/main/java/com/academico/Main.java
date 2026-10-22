package com.academico;

import com.academico.application.port.in.CursoUseCase;
import com.academico.application.port.in.EstudianteUseCase;

import com.academico.application.service.CursoService;
import com.academico.application.service.EstudianteService;

import com.academico.domain.repository.CursoRepository;
import com.academico.domain.repository.EstudianteRepository;

import com.academico.infrastructure.adapter.out.persistence.CursoRepositoryJson;
import com.academico.infrastructure.adapter.out.persistence.EstudianteRepositoryPostgres;

import com.academico.presentation.CursoUI;
import com.academico.presentation.EstudianteUI;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // ==========================================
        // CASO 1: GESTION DE ESTUDIANTES
        // ==========================================

        // Adaptador de salida: PostgreSQL
        EstudianteRepository estudianteRepository =
                new EstudianteRepositoryPostgres();

        // Puerto de entrada y servicio
        EstudianteUseCase estudianteUseCase =
                new EstudianteService(estudianteRepository);

        // Adaptador de entrada: consola
        EstudianteUI estudianteUI =
                new EstudianteUI(estudianteUseCase);


        // ==========================================
        // CASO 2: GESTION DE CURSOS
        // ==========================================

        // Adaptador de salida: JSON
        CursoRepository cursoRepository =
                new CursoRepositoryJson();

        // Puerto de entrada y servicio
        CursoUseCase cursoUseCase =
                new CursoService(cursoRepository);

        // Adaptador de entrada: consola
        CursoUI cursoUI =
                new CursoUI(cursoUseCase);


        // ==========================================
        // MENU PRINCIPAL
        // ==========================================

        Scanner sc = new Scanner(System.in);

        int opcion;

        do {

            System.out.println(
                    "\n=== SISTEMA DE GESTION ACADEMICA ==="
            );

            System.out.println("1. Gestionar estudiantes");
            System.out.println("2. Gestionar cursos");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {

                case 1:
                    estudianteUI.mostrarMenu(sc);
                    break;

                case 2:
                    cursoUI.mostrarMenu(sc);
                    break;

                case 0:
                    System.out.println("Sistema finalizado.");
                    break;

                default:
                    System.out.println("Opcion no valida.");
                    break;
            }

        } while (opcion != 0);

        sc.close();
    }
}
