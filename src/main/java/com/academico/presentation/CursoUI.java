package com.academico.presentation;

import com.academico.application.port.in.CursoUseCase;
import com.academico.domain.model.Curso;

import java.util.Objects;
import java.util.Scanner;

public class CursoUI {

    private final CursoUseCase service;

    public CursoUI(CursoUseCase service) {
        this.service = Objects.requireNonNull(
                service,
                "El caso de uso de cursos no puede ser nulo"
        );
    }

    public void mostrarMenu(Scanner sc) {

        int opcion;

        do {
            System.out.println("\n--- GESTION DE CURSOS ---");
            System.out.println("1. Registrar curso");
            System.out.println("2. Listar cursos");
            System.out.println("3. Actualizar curso");
            System.out.println("4. Eliminar curso");
            System.out.println("0. Regresar");
            System.out.print("Seleccione una opcion: ");

            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1 -> registrar(sc);
                case 2 -> listar();
                case 3 -> actualizar(sc);
                case 4 -> eliminar(sc);
                case 0 -> System.out.println(
                        "Regresando al menu principal..."
                );
                default -> System.out.println(
                        "Opcion no valida."
                );
            }

        } while (opcion != 0);
    }

    private void registrar(Scanner sc) {

        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Creditos: ");
        int creditos = sc.nextInt();
        sc.nextLine();

        Curso curso = new Curso(id, nombre, creditos);

        service.registrar(curso);

        System.out.println("Curso registrado correctamente.");
    }

    private void listar() {

        System.out.println("\n--- LISTADO DE CURSOS ---");

        if (service.listar().isEmpty()) {
            System.out.println("No hay cursos registrados.");
            return;
        }

        service.listar().forEach(c ->
                System.out.println(
                        c.getId() + " - " +
                        c.getNombre() + " - " +
                        c.getCreditos() + " creditos"
                )
        );
    }

    private void actualizar(Scanner sc) {

        System.out.print("ID del curso: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Nuevo nombre: ");
        String nombre = sc.nextLine();

        System.out.print("Nuevos creditos: ");
        int creditos = sc.nextInt();
        sc.nextLine();

        Curso curso = new Curso(id, nombre, creditos);

        boolean actualizado = service.actualizar(curso);

        System.out.println(
                actualizado
                        ? "Curso actualizado correctamente."
                        : "Curso no encontrado."
        );
    }

    private void eliminar(Scanner sc) {

        System.out.print("ID del curso a eliminar: ");
        int id = sc.nextInt();
        sc.nextLine();

        boolean eliminado = service.eliminar(id);

        System.out.println(
                eliminado
                        ? "Curso eliminado correctamente."
                        : "Curso no encontrado."
        );
    }
}
