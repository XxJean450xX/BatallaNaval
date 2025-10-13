package Modelo;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase base abstracta para todos los tipos de barcos.
 * Gestiona el tamaño, tipo, orientación y el estado de impacto del barco.
 */
public abstract class Barco {
    protected final int tamano;
    protected final CasillaEstado tipoBarco;
    protected boolean horizontal;
    
    private int impactosRecibidos;
    private final List<Point> coordenadas;

    /**
     * Constructor base para un barco.
     * @param tamano La longitud del barco.
     * @param tipoBarco El tipo de barco (ej: SUBMARINO, CRUCERO), usado para el estado de la casilla.
     */
    public Barco(int tamano, CasillaEstado tipoBarco) {
        this.tamano = tamano;
        this.tipoBarco = tipoBarco;
        this.horizontal = true;
        this.impactosRecibidos = 0; 
        this.coordenadas = new ArrayList<>(); 
    }

    /**
     * Registra un impacto en el barco.
     */
    public void recibirImpacto() {
        this.impactosRecibidos++;
    }
    
    /**
     * Verifica si el barco está hundido comparando los impactos con su tamaño.
     * @return {@code true} si {@code impactosRecibidos} es mayor o igual a {@code tamano}.
     */
    public boolean estaHundido() {
        return this.impactosRecibidos >= this.tamano;
    }
    
    /**
     * Añade una coordenada al registro interno del barco (usado por el {@code Tablero} al colocarlo).
     * @param r Fila de la coordenada.
     * @param c Columna de la coordenada.
     */
    public void agregarCoordenada(int r, int c) {
        this.coordenadas.add(new Point(r, c));
    }
    
    /**
     * Verifica si el barco ocupa la coordenada especificada.
     * @param r Fila.
     * @param c Columna.
     * @return {@code true} si el barco contiene esa coordenada.
     */
    public boolean contieneCoordenada(int r, int c) {
        return this.coordenadas.contains(new Point(r, c)); 
    }

    /**
     * Obtiene el tamaño del barco.
     * @return El tamaño.
     */
    public int getTamano() { return tamano; }
    
    /**
     * Obtiene el tipo de barco.
     * @return El {@code CasillaEstado} que representa el tipo de barco.
     */
    public CasillaEstado getTipoBarco() { return tipoBarco; }
    
    /**
     * Obtiene la orientación actual del barco.
     * @return {@code true} si es horizontal, {@code false} si es vertical.
     */
    public boolean getHorizontal() { return horizontal; }
    
    /**
     * Establece la orientación del barco.
     * @param estado {@code true} para horizontal, {@code false} para vertical.
     */
    public void setHorizontal(boolean estado) { this.horizontal = estado; }
}