package Modelo;

import java.awt.Point;
import java.util.List;

/**
 * Interfaz que define la estrategia para calcular las casillas a atacar
 * en el tablero. Permite implementar diferentes tipos de ataques (normal, bomba, etc.).
 */
public interface Ataque {
    /**
     * Calcula las coordenadas a atacar basándose en la coordenada inicial (fila, col).
     * @param fila Fila del clic inicial.
     * @param col Columna del clic inicial.
     * @param dimension Dimensión del tablero para evitar salirse de límites.
     * @return Una lista de objetos {@code Point} (coordenadas) que deben ser atacadas.
     */
    List<Point> calcularCasillasAtaque(int fila, int col, int dimension);
}