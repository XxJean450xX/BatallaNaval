package Modelo;

/**
 * Representa el barco de tipo Destructor.
 */
public class Destructor extends Barco{
	/**
     * Constructor para el Destructor.
     * El Destructor tiene un tamaño fijo de 3 y su tipo es {@code CasillaEstado.DESTRUCTOR}.
     */
	public Destructor() {
        super(3, CasillaEstado.DESTRUCTOR);
    }
}