package Controlador;

import Modelo.Usuario;

/**
 * Controlador que gestiona la lógica de registro de nuevos usuarios.
 */
public class ControladorRegistro {
    private final RepositorioUsuarios repoUsuarios;

    /**
     * Constructor del controlador de registro.
     * * @param repoUsuarios El repositorio de usuarios para verificar y guardar.
     */
    public ControladorRegistro(RepositorioUsuarios repoUsuarios) {
        this.repoUsuarios = repoUsuarios;
    }

    /**
     * Intenta registrar un nuevo usuario con el nombre, contraseña y confirmación.
     * * @param username El nombre de usuario a registrar.
     * @param password La contraseña.
     * @param confirm La confirmación de la contraseña.
     * @return {@code true} si el registro fue exitoso (campos llenos, contraseñas coinciden, usuario no existe), {@code false} en caso contrario.
     */
    public boolean registrar(String username, String password, String confirm) {
        if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) return false;
        if (!password.equals(confirm)) return false;
        if (repoUsuarios.buscarPorNombre(username).isPresent()) return false;

        String hashSimulado = username + password;
        repoUsuarios.agregar(new Usuario(username, hashSimulado));
        return true;
    }
}