package Modelo;

/**
 * Representa el barco de tipo Crucero.
 */
public class Crucero extends Barco{
	/**
     * Constructor para el Crucero.
     * El Crucero tiene un tamaño fijo de 4 y su tipo es {@code CasillaEstado.CRUCERO}.
     */
	public Crucero() {
        super(4, CasillaEstado.CRUCERO);
    }
}