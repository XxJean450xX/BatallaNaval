package Modelo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Clase que representa una partida finalizada y contiene los datos necesarios
 * para el historial y la persistencia. Es serializable.
 */
public class Partida implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String username;
    private final boolean victoria;
    private final long tiempoTotalSegundos;
    private final long ataquesRealizados;
    private final Date fechaPartida; 

    /**
     * Constructor de la partida.
     * @param username El nombre del usuario.
     * @param victoria {@code true} si el usuario ganó, {@code false} si perdió.
     * @param tiempoTotalSegundos Duración total de la partida en segundos.
     * @param ataquesRealizados Número de ataques realizados por el usuario.
     */
    public Partida(String username, boolean victoria, long tiempoTotalSegundos, long ataquesRealizados) {
        this.username = username;
        this.victoria = victoria;
        this.tiempoTotalSegundos = tiempoTotalSegundos;
        this.ataquesRealizados = ataquesRealizados;
        this.fechaPartida = new Date();
    }

    /**
     * Obtiene el nombre de usuario.
     * @return El nombre de usuario.
     */
    public String getUsername() { return username; }
    
    /**
     * Indica si la partida fue una victoria.
     * @return {@code true} si ganó.
     */
    public boolean isVictoria() { return victoria; }
    
    /**
     * Obtiene la duración de la partida en segundos.
     * @return El tiempo total en segundos.
     */
    public long getTiempoTotalSegundos() { return tiempoTotalSegundos; }
    
    /**
     * Obtiene el número de ataques realizados por el usuario.
     * @return Los ataques realizados.
     */
    public long getAtaquesRealizados() { return ataquesRealizados; }

    /**
     * Formatea la duración de la partida en el formato "mm:ss".
     * @return La duración formateada.
     */
    public String getTiempoFormateado() {
        long minutes = tiempoTotalSegundos / 60;
        long seconds = tiempoTotalSegundos % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Formatea la fecha y hora en que se jugó la partida.
     * @return La fecha en formato "dd/MM/yyyy HH:mm".
     */
    public String getFechaFormateada() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return sdf.format(fechaPartida);
    }
}