package Modelo;

/**
 * Representa el barco de tipo Portaviones.
 */
public class Portaviones extends Barco{
	/**
     * Constructor para el Portaviones.
     * El Portaviones tiene un tamaño fijo de 6 y su tipo es {@code CasillaEstado.PORTAVIONES}.
     */
	public Portaviones() {
        super(6, CasillaEstado.PORTAVIONES);
    }
}