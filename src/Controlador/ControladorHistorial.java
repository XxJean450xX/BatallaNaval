package Controlador;

import Modelo.Partida;
import java.util.List;

/**
 * Controlador para la gestión del historial de partidas, utilizando
 * el {@code RepositorioPartidas} para la persistencia.
 */
public class ControladorHistorial {
    private final RepositorioPartidas repoPartidas;

    /**
     * Constructor del controlador de historial.
     * * @param repoPartidas El repositorio de partidas.
     */
    public ControladorHistorial(RepositorioPartidas repoPartidas) {
        this.repoPartidas = repoPartidas;
    }

    /**
     * Obtiene la lista de partidas jugadas por un usuario específico.
     * * @param username El nombre de usuario.
     * @return Una lista de objetos {@code Partida}.
     */
    public List<Partida> obtenerHistorial(String username) {
        return repoPartidas.listarPorUsuario(username);
    }

    /**
     * Registra una partida finalizada en el repositorio.
     * @param partida instancia con todos los datos de la partida (tiempo, resultado, ataques, usuario, etc.)
     */
    public void registrarPartida(Partida partida) {
        if (partida != null) {
            repoPartidas.agregar(partida);
            System.out.println("[DEBUG] Partida registrada en historial: " + partida.getTiempoFormateado());
        }
    }
}