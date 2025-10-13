package Controlador;

import Modelo.Usuario;
import java.util.Optional;

/**
 * Controlador que gestiona la lógica de autenticación (login) de usuarios.
 */
public class ControladorLogin {
    private final RepositorioUsuarios repoUsuarios;

    /**
     * Constructor del controlador de login.
     * * @param repoUsuarios El repositorio de usuarios para la verificación.
     */
    public ControladorLogin(RepositorioUsuarios repoUsuarios) {
        this.repoUsuarios = repoUsuarios;
    }

    /**
     * Intenta autenticar a un usuario con su nombre y contraseña.
     * * @param username El nombre de usuario.
     * @param password La contraseña (en este caso, se usa para simular el hash).
     * @return Un {@code Optional} que contiene el {@code Usuario} si las credenciales son válidas, o vacío si no lo son.
     */
    public Optional<Usuario> intentarLogin(String username, String password) {
        String hashSimulado = username + password;
        return repoUsuarios.buscarPorNombre(username)
                .filter(u -> u.getPasswordHash().equals(hashSimulado));
    }
}