package gestor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Crea un gestor de reservas asociado a un fichero concreto.
 * 
 * @param nombreFichero nombre del fichero de almacenamiento
 */
public class GestorReservas {
    private ArrayList<Reserva> reservas;
    private String nombreFichero;

    public GestorReservas(String nombreFichero) {
        reservas = new ArrayList<Reserva>();
        this.nombreFichero = nombreFichero;
    }

    /**
     * Carga las reservas almacenadas en el fichero asociado.
     */
    public void cargarDesdeFichero() {
        try (BufferedReader br = new BufferedReader(new FileReader(nombreFichero))) {
            String linea = br.readLine();

            while (linea != null) {
                if (!linea.trim().equals("")) {
                    String[] partes = linea.split(";");

                    if (partes.length == 5) {
                        String codigo = partes[0];
                        String nombreCliente = partes[1];
                        int numeroHabitacion = Integer.parseInt(partes[2]);
                        LocalDate fechaEntrada = Reserva.convertirFecha(partes[3]);
                        LocalDate fechaSalida = Reserva.convertirFecha(partes[4]);

                        Reserva r = new Reserva(codigo, nombreCliente, numeroHabitacion, fechaEntrada, fechaSalida);
                        reservas.add(r);
                    }
                }

                linea = br.readLine();
            }
        } catch (IOException e) {
            System.out.println("No existe el fichero o no se puede leer.");
        } catch (Exception e) {
            System.out.println("Error al cargar una reserva.");
        }
    }

    /**
     * Guarda todas las reservas actuales en el fichero asociado.
     * @return true si se guarda bien
     */
    public boolean guardarEnFichero() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreFichero))) {
            for (Reserva r : reservas) {
                bw.write(r.getCodReserva() + ";" + r.getNombreCliente() + ";" + r.getNumHabitacion() + ";"
                        + Reserva.fechaAString(r.getFechaEntrada()) + ";" + Reserva.fechaAString(r.getFechaSalida()));
                bw.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("No se pudo guardar el fichero.");
            return false;
        }
    }

    /**
     * Añade una nueva reserva al sistema si no existe otra con el mismo código
     * @param nueva reserva que se desea añadir
     * @return true si se añadió correctamente, false en caso contrario
     */
    public boolean anadirReserva(Reserva nueva) {
        if (buscarPorCodigo(nueva.getCodReserva()) != null) {
            return false;
        }

        if (!nueva.getFechaSalida().isAfter(nueva.getFechaEntrada())) {
            return false;
        }

        if (haySolape(nueva)) {
            return false;
        }

        reservas.add(nueva);
        return true;
    }

    /**
     * Busca una reserva por su código.
     * 
     * @param codigo código de la reserva
     * @return la reserva encontrada o null si no existe
     */
    public Reserva buscarPorCodigo(String codigo) {
        for (Reserva r : reservas) {
            if (r.getCodReserva().equals(codigo)) {
                return r;
            }
        }
        return null;
    }

    /**
     * Elimina una reserva identificada por su código.
     * 
     * @param codigo código de la reserva a eliminar
     * @return true si se eliminó correctamente, false si no existe
     */
    public boolean eliminarReserva(String codigo) {
        Reserva r = buscarPorCodigo(codigo);
        if (r != null) {
            return reservas.remove(r);
        }
        return false;
    }

    /**
     * Modifica las fechas de una reserva existente.
     * 
     * @param codigo código de la reserva a modificar
     * @param nuevaEntrada nueva fecha de entrada
     * @param nuevaSalida nueva fecha de salida
     * @return true si se modificó correctamente, false en caso contrario
     */
    public boolean modificarReserva(String codigo, LocalDate nuevaEntrada, LocalDate nuevaSalida) {
        Reserva r = buscarPorCodigo(codigo);

        if (r == null) {
            return false;
        }

        if (!nuevaSalida.isAfter(nuevaEntrada)) {
            return false;
        }

        Reserva copia = new Reserva(r.getCodReserva(), r.getNombreCliente(), r.getNumHabitacion(), nuevaEntrada, nuevaSalida);

        if (haySolapeExcluyendo(copia, codigo)) {
            return false;
        }

        r.setFechaEntrada(nuevaEntrada);
        r.setFechaSalida(nuevaSalida);
        return true;
    }

    public void listarReservas() {
        ArrayList<Reserva> copia = new ArrayList<Reserva>(reservas);
        Collections.sort(copia);

        for (Reserva r : copia) {
            System.out.println(r);
        }
    }

    private boolean haySolape(Reserva nueva) {
        for (Reserva actual : reservas) {
            if (actual.getNumHabitacion() == nueva.getNumHabitacion()) {
                if (seSolapan(actual, nueva)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean haySolapeExcluyendo(Reserva nueva, String codigoExcluido) {
        for (Reserva actual : reservas) {
            if (!actual.getCodReserva().equals(codigoExcluido)) {
                if (actual.getNumHabitacion() == nueva.getNumHabitacion()) {
                    if (seSolapan(actual, nueva)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean seSolapan(Reserva r1, Reserva r2) {
        return !r1.getFechaSalida().isBefore(r2.getFechaEntrada())
                && !r2.getFechaSalida().isBefore(r1.getFechaEntrada());
    }
}