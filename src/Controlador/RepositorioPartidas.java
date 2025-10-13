package Controlador;

import Modelo.Partida;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repositorio para la gestión de objetos {@code Partida}.
 * Abstrae la lógica de persistencia del controlador.
 */
public class RepositorioPartidas {
    private final GestorPersistencia persistencia = new GestorPersistencia();

    /**
     * Obtiene todas las partidas almacenadas.
     * @return Lista de todas las {@code Partida}.
     */
    public List<Partida> listar() {
        return persistencia.cargarPartidas();
    }

    /**
     * Obtiene las partidas filtradas por un nombre de usuario específico.
     * @param username El nombre de usuario para filtrar.
     * @return Lista de {@code Partida} asociadas al usuario.
     */
    public List<Partida> listarPorUsuario(String username) {
        return listar().stream()
                .filter(p -> p.getUsername().equals(username))
                .collect(Collectors.toList());
    }

    /**
     * Añade una nueva partida al repositorio y la guarda.
     * @param partida La {@code Partida} a añadir.
     */
    public void agregar(Partida partida) {
        List<Partida> partidas = listar();
        partidas.add(partida);
        persistencia.guardarPartida(partida);
    }
}