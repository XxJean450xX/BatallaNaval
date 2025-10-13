package Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import Modelo.Barco;
import Modelo.Crucero;
import Modelo.Destructor;
import Modelo.Submarino;

/**
 * Clase de pruebas unitarias para la clase {@link Barco}.
 * <p>
 * Se validan los comportamientos básicos de los barcos del juego, como:
 * <ul>
 *   <li>La detección de hundimiento tras impactos.</li>
 *   <li>El almacenamiento y verificación de coordenadas asignadas.</li>
 *   <li>El control de orientación (horizontal o vertical).</li>
 * </ul>
 * 
 * Estas pruebas garantizan el correcto funcionamiento de las clases hijas
 * {@link Submarino}, {@link Destructor} y {@link Crucero}.
 */
class BarcoTest {

    /**
     * Verifica que un barco se considere hundido solo después de recibir
     * tantos impactos como su tamaño.
     */
    @Test
    void testImpactosYHundimiento() {
        Barco crucero = new Crucero();
        assertFalse(crucero.estaHundido(), "El barco no debería estar hundido inicialmente");

        for (int i = 0; i < crucero.getTamano(); i++) {
            crucero.recibirImpacto();
        }
        assertTrue(crucero.estaHundido(), "El barco debería estar hundido tras recibir todos los impactos");
    }

    /**
     * Comprueba que las coordenadas agregadas a un barco se registren correctamente
     * y puedan ser verificadas con {@code contieneCoordenada()}.
     */
    @Test
    void testAgregarYVerificarCoordenadas() {
        Barco destructor = new Destructor();
        destructor.agregarCoordenada(2, 3);
        assertTrue(destructor.contieneCoordenada(2, 3), "El barco debería contener la coordenada agregada");
        assertFalse(destructor.contieneCoordenada(5, 5), "El barco no debería contener coordenadas no asignadas");
    }

    /**
     * Verifica que la orientación del barco (horizontal o vertical)
     * pueda cambiarse correctamente mediante el método {@code setHorizontal()}.
     */
    @Test
    void testOrientacion() {
        Barco sub = new Submarino();
        sub.setHorizontal(false);
        assertFalse(sub.getHorizontal(), "El barco debería tener orientación vertical");
    }
}
