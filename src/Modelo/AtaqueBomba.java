package Modelo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la estrategia de ataque tipo "Bomba".
 * Ataca la casilla central y las cuatro casillas adyacentes formando una cruz (3x3).
 */
public class AtaqueBomba implements Ataque {
    /**
     * Calcula las casillas para un ataque de bomba en forma de cruz (5 puntos).
     * @param fila Fila del clic inicial (centro).
     * @param col Columna del clic inicial (centro).
     * @param dimension Dimensión del tablero para control de límites.
     * @return Una lista de coordenadas que forman la cruz.
     */
    @Override
    public List<Point> calcularCasillasAtaque(int fila, int col, int dimension) {
        List<Point> casillas = new ArrayList<>();
        // Centro (0,0) + Arriba (-1,0), Abajo (1,0), Izquierda (0,-1), Derecha (0,1)
        int[][] offset = {{0, 0}, {-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        for (int[] p : offset) {
            int r = fila + p[0];
            int c = col + p[1];
            if (r >= 0 && r < dimension && c >= 0 && c < dimension) {
                casillas.add(new Point(r, c));
            }
        }
        return casillas;
    }
}