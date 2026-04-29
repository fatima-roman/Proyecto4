package principal;

import gestor.FechaInvalidaException;
import gestor.GestorReservas;
import gestor.Reserva;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        GestorReservas gestor = new GestorReservas("src/gestor/gestion.txt");
        gestor.cargarDesdeFichero();

        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();

            try {
                opcion = Integer.parseInt(sc.nextLine());

                if (opcion == 1) {
                    System.out.print("Codigo: ");
                    String codigo = sc.nextLine();

                    System.out.print("Nombre cliente: ");
                    String nombre = sc.nextLine();

                    System.out.print("Numero de habitacion: ");
                    int habitacion = Integer.parseInt(sc.nextLine());

                    System.out.print("Fecha de entrada (dd/mm/aaaa): ");
                    LocalDate entrada = Reserva.convertirFecha(sc.nextLine());

                    System.out.print("Fecha de salida (dd/mm/aaaa): ");
                    LocalDate salida = Reserva.convertirFecha(sc.nextLine());

                    Reserva r = new Reserva(codigo, nombre, habitacion, entrada, salida);

                    if (gestor.anadirReserva(r)) {
                        System.out.println("Reserva anadida correctamente.");
                    } else {
                        System.out.println("No se pudo anadir la reserva.");
                    }

                } else if (opcion == 2) {
                    System.out.print("Codigo a buscar: ");
                    String codigo = sc.nextLine();

                    Reserva r = gestor.buscarPorCodigo(codigo);

                    if (r != null) {
                        System.out.println(r);
                    } else {
                        System.out.println("No existe esa reserva.");
                    }

                } else if (opcion == 3) {
                    gestor.listarReservas();

                } else if (opcion == 4) {
                    System.out.print("Codigo a modificar: ");
                    String codigo = sc.nextLine();

                    Reserva existente = gestor.buscarPorCodigo(codigo);

                    if (existente != null) {
                        System.out.print("Nueva fecha de entrada (dd/mm/aaaa): ");
                        LocalDate entrada = Reserva.convertirFecha(sc.nextLine());

                        System.out.print("Nueva fecha de salida (dd/mm/aaaa): ");
                        LocalDate salida = Reserva.convertirFecha(sc.nextLine());

                        if (gestor.modificarReserva(codigo, entrada, salida)) {
                            System.out.println("Reserva modificada.");
                        } else {
                            System.out.println("No se pudo modificar.");
                        }
                    } else {
                        System.out.println("No existe esa reserva.");
                    }

                } else if (opcion == 5) {
                    System.out.print("Codigo a eliminar: ");
                    String codigo = sc.nextLine();

                    if (gestor.eliminarReserva(codigo)) {
                        System.out.println("Reserva eliminada.");
                    } else {
                        System.out.println("No existe esa reserva.");
                    }

                } else if (opcion == 6) {
                    if (gestor.guardarEnFichero()) {
                        System.out.println("Reservas guardadas.");
                    } else {
                        System.out.println("No se pudieron guardar.");
                    }

                } else if (opcion == 0) {
                    gestor.guardarEnFichero();
                    System.out.println("Saliendo del sistema...");
                } else {
                    System.out.println("Opcion no valida.");
                }

            } catch (NumberFormatException e) {
                System.out.println("Debes introducir un numero valido.");
            } catch (FechaInvalidaException e) {
                System.out.println(e.getMessage());
            } catch (Exception e) {
                System.out.println("Datos incorrectos.");
            }
        }

        sc.close();
    }

    public static void mostrarMenu() {
        System.out.println("\n1. Añadir reserva"
                + "\n2. Buscar reserva"
                + "\n3. Listar reservas"
                + "\n4. Modificar reserva"
                + "\n5. Eliminar reserva"
                + "\n6. Guardar"
                + "\n0. Salir"
                + "\nElige una opcion: ");
    }
}