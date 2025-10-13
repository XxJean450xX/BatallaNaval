package Modelo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de la estrategia de ataque tipo "Nuclear".
 * Ataca una gran cruz de 5x5 centrada en la casilla clickeada.
 */
public class AtaqueNuclear implements Ataque {
    /**
     * Calcula las casillas para un ataque nuclear en forma de cruz (9 puntos: 5x5).
     * @param fila Fila del clic inicial (centro).
     * @param col Columna del clic inicial (centro).
     * @param dimension Dimensión del tablero para control de límites.
     * @return Una lista de coordenadas que forman la cruz de 5x5.
     */
    @Override
    public List<Point> calcularCasillasAtaque(int fila, int col, int dimension) {
        List<Point> casillas = new ArrayList<>();
        int[] offsets = {-2, -1, 0, 1, 2}; 

        // Eje Vertical
        for (int dr : offsets) {
            int r = fila + dr;
            if (r >= 0 && r < dimension) {
                casillas.add(new Point(r, col)); 
            }
        }

        // Eje Horizontal (evitamos duplicar el centro (0,0) que ya fue añadido)
        for (int dc : offsets) {
            if (dc != 0) {
                int c = col + dc;
                if (c >= 0 && c < dimension) {
                    casillas.add(new Point(fila, c)); 
                }
            }
        }
        return casillas;
    }
}