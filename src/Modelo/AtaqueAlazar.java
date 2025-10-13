package Modelo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Implementación de la estrategia de ataque que selecciona el punto inicial
 * y dos puntos aleatorios adicionales, atacando un total de tres casillas.
 */
public class AtaqueAlazar implements Ataque {
    /**
     * Calcula las casillas para un ataque triple aleatorio.
     * @param fila Fila del clic inicial.
     * @param col Columna del clic inicial.
     * @param dimension Dimensión del tablero.
     * @return Una lista de tres coordenadas: la inicial y dos aleatorias.
     */
    @Override
    public List<Point> calcularCasillasAtaque(int fila, int col, int dimension) {
        List<Point> casillas = new ArrayList<>();
        Random rand = new Random();
        
        casillas.add(new Point(fila, col));

        for (int i = 0; i < 2; i++) {
            int r = rand.nextInt(dimension);
            int c = rand.nextInt(dimension);
            casillas.add(new Point(r, c));
        }
        
        return casillas;
    }
}