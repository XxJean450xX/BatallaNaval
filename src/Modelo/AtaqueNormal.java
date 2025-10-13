package Modelo;

import java.awt.Point;
import java.util.Collections;
import java.util.List;

/**
 * Implementación de la estrategia de ataque normal.
 * Solo ataca la casilla clickeada (1x1).
 */
public class AtaqueNormal implements Ataque {
    /**
     * Calcula las casillas para un ataque normal (solo el punto inicial).
     * @param fila Fila del clic inicial.
     * @param col Columna del clic inicial.
     * @param dimension Dimensión del tablero (no usada).
     * @return Una lista inmutable que contiene solo la coordenada inicial.
     */
    @Override
    public List<Point> calcularCasillasAtaque(int fila, int col, int dimension) {
        return Collections.singletonList(new Point(fila, col));
    }
}