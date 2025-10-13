package Modelo;

import java.awt.Color;

/**
 * Enum que representa todos los posibles estados de una casilla en el tablero,
 * incluyendo los tipos de barcos y los estados de ataque.
 * Cada estado tiene un color asociado para la representación visual.
 */
public enum CasillaEstado {
    // Estados del tablero
    AGUA    (new Color(89, 84, 67)),
    IMPACTADO  (new Color(125, 40, 25)),
    ATACADO   (new Color(38, 35, 26)),

    // Estados que representan la presencia de un tipo de barco específico
    PORTAVIONES (new Color(217, 156, 102)),
    DESTRUCTOR  (new Color(217, 117, 102)),
    CRUCERO     (new Color(74, 89, 87)),
    SUBMARINO   (new Color(86, 57, 110));

    private final Color color;

    /**
     * Constructor de {@code CasillaEstado}.
     * @param color El color asociado al estado de la casilla.
     */
    CasillaEstado(Color color) {
        this.color = color;
    }

    /**
     * Obtiene el color asociado al estado.
     * @return El objeto {@code Color}.
     */
    public Color getColor() {
        return color;
    }
}