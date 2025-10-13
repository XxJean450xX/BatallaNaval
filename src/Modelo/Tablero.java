package Modelo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Representa el tablero de juego, ya sea el del jugador o el de la IA.
 * Gestiona la colocación de barcos, el estado de las casillas y la recepción de ataques.
 */
public class Tablero {
    private final int dimension;
    private final CasillaEstado[][] matriz;
    private final List<Barco> barcosColocados;
    
    private static final Map<CasillaEstado, Integer> LIMITES_POR_TIPO = new HashMap<>();
    static {
        LIMITES_POR_TIPO.put(CasillaEstado.SUBMARINO, 4); 
        LIMITES_POR_TIPO.put(CasillaEstado.DESTRUCTOR, 3); 
        LIMITES_POR_TIPO.put(CasillaEstado.CRUCERO, 2); 
        LIMITES_POR_TIPO.put(CasillaEstado.PORTAVIONES, 1); 
    }
    
    private static final int TOTAL_BARCOS_REQUERIDOS = 10;
    
    /**
     * Constructor del tablero. Inicializa la matriz con {@code CasillaEstado.AGUA}.
     * @param dimension La dimensión (N x N) del tablero.
     */
    public Tablero(int dimension) {
        this.dimension = dimension;
        this.matriz = new CasillaEstado[dimension][dimension];
        for (CasillaEstado[] fila : matriz) {
            Arrays.fill(fila, CasillaEstado.AGUA);
        }
        this.barcosColocados = new ArrayList<>();
    }

    /**
     * Intenta colocar un barco en la posición y orientación dadas.
     * Verifica límites de cantidad, colisión y frontera.
     * @param barco El objeto {@code Barco} a colocar.
     * @param fila Fila inicial.
     * @param col Columna inicial.
     * @return {@code true} si el barco se colocó con éxito, {@code false} en caso contrario.
     */
    public boolean colocarBarco(Barco barco, int fila, int col) {
        if (!puedeColocarBarco(barco.getTipoBarco())) {
            return false;
        }
        
        if (!esPosicionValida(barco, fila, col)) {
            return false;
        }

        if (barco.getHorizontal()) {
            for (int i = 0; i < barco.getTamano(); i++) {
                matriz[fila][col + i] = barco.getTipoBarco();
                barco.agregarCoordenada(fila, col + i);
            }
        } else { 
            for (int i = 0; i < barco.getTamano(); i++) {
                matriz[fila + i][col] = barco.getTipoBarco();
                barco.agregarCoordenada(fila + i, col);
            }
        }
        
        barcosColocados.add(barco); 
        
        return true;
    }
    
    /**
     * Cuenta cuántos barcos de un tipo específico han sido colocados.
     * @param tipoBarco El tipo de barco a contar.
     * @return La cantidad de barcos de ese tipo.
     */
    public int contarBarcosDeTipo(CasillaEstado tipoBarco) {
        int count = 0;
        for (Barco barco : barcosColocados) {
            if (barco.getTipoBarco() == tipoBarco) {
                count++;
            }
        }
        return count;
    }

    /**
     * Verifica si se puede colocar un barco más de un tipo dado,
     * comparando con los límites predefinidos.
     * @param tipoBarco El tipo de barco a verificar.
     * @return {@code true} si el límite no ha sido alcanzado, {@code false} en caso contrario.
     */
    public boolean puedeColocarBarco(CasillaEstado tipoBarco) {
        Integer limite = LIMITES_POR_TIPO.get(tipoBarco);
        if (limite == null) return false; 
        
        return contarBarcosDeTipo(tipoBarco) < limite;
    }

    /**
     * Verifica si el barco puede ser colocado en la posición dada sin salirse
     * de los límites del tablero ni colisionar con otros barcos.
     * @param barco El barco.
     * @param fila Fila inicial.
     * @param col Columna inicial.
     * @return {@code true} si la posición es válida.
     */
    private boolean esPosicionValida(Barco barco, int fila, int col) {
        if (barco.getHorizontal()) {
            if (col + barco.getTamano() > dimension) return false;
            for (int i = 0; i < barco.getTamano(); i++) {
                if (matriz[fila][col + i] != CasillaEstado.AGUA) return false;
            }
        } else { 
            if (fila + barco.getTamano() > dimension) return false;
            for (int i = 0; i < barco.getTamano(); i++) {
                if (matriz[fila + i][col] != CasillaEstado.AGUA) return false;
            }
        }
        return true;
    }
    
    /**
     * Verifica si una coordenada está dentro de los límites del tablero.
     * @param fila Fila.
     * @param col Columna.
     * @return {@code true} si la posición es válida.
     */
    public boolean esPosicionValida(int fila, int col) {
        return fila >= 0 && fila < dimension && col >= 0 && col < dimension;
    }

    /**
     * Obtiene el estado actual de una casilla.
     * @param fila Fila.
     * @param col Columna.
     * @return El {@code CasillaEstado}.
     */
    public CasillaEstado getEstadoCasilla(int fila, int col) {
        return matriz[fila][col];
    }
    
    /**
     * Procesa una lista de ataques al tablero.
     * Actualiza el estado de las casillas y los impactos en los barcos.
     * @param casillasAtaque Lista de coordenadas a atacar.
     * @return {@code true} si al menos uno de los ataques resultó en impacto, {@code false} en caso contrario.
     */
    public boolean recibirAtaque(List<Point> casillasAtaque) {
        boolean impacto = false;
        
        for (Point p : casillasAtaque) {
            int r = p.x;
            int c = p.y;
            
            if (esPosicionValida(r, c)) {
                CasillaEstado estadoActual = matriz[r][c];
                
                if (esBarco(estadoActual)) {
                    Barco barcoImpactado = buscarBarcoPorCasilla(r, c);
                    
                    if (barcoImpactado != null) {
                        barcoImpactado.recibirImpacto(); 
                    }

                    matriz[r][c] = CasillaEstado.IMPACTADO; 
                    impacto = true;
                } 
                else if (estadoActual == CasillaEstado.AGUA) {
                    matriz[r][c] = CasillaEstado.ATACADO; 
                }
            }
        }
        return impacto;
    }

    /**
     * Busca el objeto {@code Barco} que ocupa una casilla específica.
     * @param r Fila.
     * @param c Columna.
     * @return El objeto {@code Barco} o {@code null} si no hay barco.
     */
    private Barco buscarBarcoPorCasilla(int r, int c) {
        for (Barco barco : barcosColocados) {
            if (barco.contieneCoordenada(r, c)) { 
                return barco;
            }
        }
        return null;
    }
    
    /**
     * Verifica si todos los barcos colocados en el tablero están hundidos.
     * @return {@code true} si no hay barcos o todos están hundidos.
     */
    public boolean estanTodosBarcosHundidos() {
        if (barcosColocados.isEmpty()) {
            return false; 
        }
        
        for (Barco barco : barcosColocados) {
            if (!barco.estaHundido()) { 
                return false;
            }
        }
        
        return true; 
    }
    
    /**
     * Cuenta la cantidad de barcos que han sido completamente hundidos.
     * @return La suma de barcos hundidos.
     */
    public int contadorBarcosCaidos() {
        int sum = 0;    
        
        for (Barco barco : barcosColocados) {
            if (barco.estaHundido()) {  
                sum++;
            }
        }
        return sum;
    }
    
    /**
     * Determina si un {@code CasillaEstado} corresponde a la presencia de un barco.
     * @param estado El estado a evaluar.
     * @return {@code true} si el estado es un tipo de barco.
     */
    private boolean esBarco(CasillaEstado estado) {
        return estado != CasillaEstado.AGUA && 
                estado != CasillaEstado.ATACADO && 
                estado != CasillaEstado.IMPACTADO;
    }
    
    /**
     * Devuelve el estado de la casilla adaptado para la vista del Oponente.
     * Oculta los barcos que no han sido impactados (los trata como {@code AGUA}).
     * @param fila Fila.
     * @param col Columna.
     * @return El {@code CasillaEstado} adaptado para la vista del oponente.
     */
    public CasillaEstado getEstadoCasillaVistaOponente(int fila, int col) {
        if (!esPosicionValida(fila, col)) {
            return CasillaEstado.AGUA; 
        }
        
        CasillaEstado estadoReal = matriz[fila][col];
        
        if (estadoReal == CasillaEstado.ATACADO) {
            return CasillaEstado.ATACADO; 
        }
        
        if (estadoReal == CasillaEstado.IMPACTADO) {
            return CasillaEstado.IMPACTADO; 
        }
        
        if (esBarco(estadoReal)) {
            return CasillaEstado.AGUA; 
        }
        
        return CasillaEstado.AGUA;
    }
    
    /**
     * Obtiene la matriz del tablero.
     * @return La matriz de estados.
     */
    public CasillaEstado[][] getMatriz() {
        return matriz;
    }

    /**
     * Verifica si se ha colocado la cantidad total requerida de barcos (10).
     * @return {@code true} si se colocaron 10 barcos.
     */
    public boolean estanTodosLosBarcosColocados() {
        return barcosColocados.size() == TOTAL_BARCOS_REQUERIDOS;
    }
    
    /**
     * Obtiene la dimensión del tablero (N).
     * @return La dimensión.
     */
    public int getDimension() {
        return dimension;
    }
}