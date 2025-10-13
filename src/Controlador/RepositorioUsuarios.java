package Controlador;

import Modelo.Usuario;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la gestión de objetos {@code Usuario}.
 * Abstrae la lógica de persistencia del controlador.
 */
public class RepositorioUsuarios {
    private final GestorPersistencia persistencia = new GestorPersistencia();

    /**
     * Obtiene todos los usuarios almacenados.
     * @return Lista de todos los objetos {@code Usuario}.
     */
    public List<Usuario> listar() {
        return persistencia.cargarUsuarios();
    }

    /**
     * Guarda la lista completa de usuarios.
     * @param usuarios La lista de usuarios a guardar.
     */
    public void guardarTodos(List<Usuario> usuarios) {
        persistencia.guardarUsuarios(usuarios);
    }

    /**
     * Busca un usuario por su nombre.
     * @param username El nombre de usuario a buscar.
     * @return Un {@code Optional} con el {@code Usuario} si se encuentra, o vacío si no.
     */
    public Optional<Usuario> buscarPorNombre(String username) {
        return listar().stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst();
    }

    /**
     * Añade un nuevo usuario al repositorio y lo guarda.
     * @param usuario El {@code Usuario} a añadir.
     */
    public void agregar(Usuario usuario) {
        List<Usuario> usuarios = listar();
        usuarios.add(usuario);
        guardarTodos(usuarios);
    }
}