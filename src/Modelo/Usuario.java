package Modelo;

import java.io.Serializable;

/**
 * Clase que representa un usuario, utilizada para el login y registro. Es serializable.
 */
public class Usuario implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String username;
    private final String passwordHash; 

    /**
     * Constructor del usuario.
     * @param username El nombre de usuario.
     * @param passwordHash El hash de la contraseña (simulado).
     */
    public Usuario(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    /**
     * Obtiene el nombre de usuario.
     * @return El nombre de usuario.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Obtiene el hash de la contraseña.
     * @return El hash de la contraseña.
     */
    public String getPasswordHash() {
        return passwordHash;
    }
}