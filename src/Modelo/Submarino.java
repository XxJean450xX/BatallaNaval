package Modelo;

/**
 * Representa el barco de tipo Submarino.
 */
public class Submarino extends Barco {
	/**
     * Constructor para el Submarino.
     * El Submarino tiene un tamaño fijo de 2 y su tipo es {@code CasillaEstado.SUBMARINO}.
     */
	public Submarino() {
        super(2, CasillaEstado.SUBMARINO);
    }
}