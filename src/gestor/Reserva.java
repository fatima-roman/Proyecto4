package gestor;

import java.time.LocalDate;

public class Reserva implements Comparable<Reserva> {
    private String codReserva;
    private String nombreCliente;
    private int numHabitacion;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;

    /**
     * @param cod código de la reserva
     * @param nombre nombre del cliente
     * @param numHab número de habitación
     * @param fechaEnt fecha de entrada
     * @param fechaS fecha de salida
     */
    public Reserva(String cod, String nombre, int numHab, LocalDate fechaEnt, LocalDate fechaS) {
        setCodReserva(cod);
        setNombreCliente(nombre);
        setNumHabitacion(numHab);
        setFechaEntrada(fechaEnt);
        setFechaSalida(fechaS);
    }

    /**
     * Convierte una cadena con formato dd/mm/aaaa en un objeto LocalDate.
     * @param fecha cadena con la fecha a convertir
     * @return fecha convertida a LocalDate
     * @throws FechaInvalidaException si el formato no es correcto o la fecha no existe
     */
    public static LocalDate convertirFecha(String fecha) throws FechaInvalidaException {
        if (fecha == null || fecha.length() != 10 || fecha.charAt(2) != '/' || fecha.charAt(5) != '/') {
            throw new FechaInvalidaException("Formato incorrecto. Usa dd/mm/aaaa");
        }

        try {
            int dia = Integer.parseInt(fecha.substring(0, 2));
            int mes = Integer.parseInt(fecha.substring(3, 5));
            int ano = Integer.parseInt(fecha.substring(6, 10));
            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            throw new FechaInvalidaException("Fecha no válida.");
        }
    }

    public String getCodReserva() {
        return codReserva;
    }

    public void setCodReserva(String codReserva) {
        this.codReserva = codReserva;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public int getNumHabitacion() {
        return numHabitacion;
    }

    public void setNumHabitacion(int numHabitacion) {
        if (numHabitacion > 0) {
            this.numHabitacion = numHabitacion;
        } else {
            throw new IllegalArgumentException("El número de habitación debe ser mayor que 0.");
        }
    }

    public LocalDate getFechaEntrada() {
        return fechaEntrada;
    }

    public void setFechaEntrada(LocalDate fechaEntrada) {
        this.fechaEntrada = fechaEntrada;
    }

    public LocalDate getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDate fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    /**
     * Convierte una fecha LocalDate a una cadena con formato dd/mm/aaaa.
     * 
     * @param fecha fecha a convertir
     * @return cadena con el formato dd/mm/aaaa
     */
    public static String fechaAString(LocalDate fecha) {
        String dia = fecha.getDayOfMonth() < 10 ? "0" + fecha.getDayOfMonth() : "" + fecha.getDayOfMonth();
        String mes = fecha.getMonthValue() < 10 ? "0" + fecha.getMonthValue() : "" + fecha.getMonthValue();
        return dia + "/" + mes + "/" + fecha.getYear();
    }

    @Override
    public String toString() {
        return codReserva + " - " + nombreCliente + " - " + numHabitacion + " - "
                + fechaAString(fechaEntrada) + " - " + fechaAString(fechaSalida);
    }

    @Override
    public int hashCode() {
        return codReserva.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Reserva otra = (Reserva) obj;
        return codReserva.equals(otra.codReserva);
    }

    @Override
    public int compareTo(Reserva otra) {
        int compHabitacion = Integer.compare(this.numHabitacion, otra.numHabitacion);
        if (compHabitacion != 0) {
            return compHabitacion;
        }
        return this.fechaEntrada.compareTo(otra.fechaEntrada);
    }
}