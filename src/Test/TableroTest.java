package Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Point;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import Modelo.Barco;
import Modelo.CasillaEstado;
import Modelo.Crucero;
import Modelo.Destructor;
import Modelo.Portaviones;
import Modelo.Submarino;
import Modelo.Tablero;

/**
 * Clase de pruebas unitarias para {@link Tablero}.
 * <p>
 * Se validan los procesos principales del tablero, incluyendo:
 * <ul>
 *   <li>Inicialización y dimensiones.</li>
 *   <li>Colocación de barcos en diferentes orientaciones.</li>
 *   <li>Registro y resultado de ataques.</li>
 *   <li>Verificación de hundimiento y conteo de barcos destruidos.</li>
 *   <li>Restricciones de cantidad por tipo de barco.</li>
 *   <li>Vista del tablero desde la perspectiva del oponente.</li>
 * </ul>
 * 
 * Cada prueba garantiza que la lógica del modelo de juego funcione correctamente
 * antes de integrarse con las vistas y controladores.
 */
class TableroTest {

    /** Tablero usado en las pruebas */
    private Tablero tablero;

    /**
     * Inicializa un tablero nuevo de 10x10 antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        tablero = new Tablero(10);
    }

    /**
     * Verifica que el tablero se inicialice correctamente con todas las casillas
     * en estado de agua y la dimensión esperada.
     */
    @Test
    void testInicializacionTablero() {
        assertEquals(10, tablero.getDimension());
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                assertEquals(CasillaEstado.AGUA, tablero.getEstadoCasilla(i, j));
            }
        }
    }

    /**
     * Prueba la colocación de un barco horizontal y verifica que sus casillas
     * cambien al estado correspondiente.
     */
    @Test
    void testColocarBarcoHorizontal() {
        Barco submarino = new Submarino();
        boolean colocado = tablero.colocarBarco(submarino, 0, 0);
        assertTrue(colocado);
        for (int i = 0; i < submarino.getTamano(); i++) {
            assertEquals(CasillaEstado.SUBMARINO, tablero.getEstadoCasilla(0, i));
        }
    }

    /**
     * Prueba la colocación de un barco vertical y valida el estado de las casillas.
     */
    @Test
    void testColocarBarcoVertical() {
        Barco crucero = new Crucero();
        crucero.setHorizontal(false);
        boolean colocado = tablero.colocarBarco(crucero, 0, 0);
        assertTrue(colocado);
        for (int i = 0; i < crucero.getTamano(); i++) {
            assertEquals(CasillaEstado.CRUCERO, tablero.getEstadoCasilla(i, 0));
        }
    }

    /**
     * Verifica que no se puedan colocar barcos fuera de los límites del tablero.
     */
    @Test
    void testColocarBarcoFueraDeLimite() {
        Barco destructor = new Destructor();
        boolean colocado = tablero.colocarBarco(destructor, 9, 9);
        assertFalse(colocado);
    }

    /**
     * Comprueba que no se puedan colocar dos barcos en posiciones superpuestas.
     */
    @Test
    void testColocarBarcoSobreOtro() {
        Barco sub1 = new Submarino();
        Barco sub2 = new Submarino();
        assertTrue(tablero.colocarBarco(sub1, 0, 0));
        assertFalse(tablero.colocarBarco(sub2, 0, 0));
    }

    /**
     * Simula ataques al tablero y verifica los cambios de estado en las casillas
     * (impacto o agua).
     */
    @Test
    void testRecibirAtaqueImpactoYAgua() {
        Barco sub = new Submarino();
        tablero.colocarBarco(sub, 0, 0);

        boolean impacto = tablero.recibirAtaque(List.of(new Point(0, 0)));
        boolean fallo = tablero.recibirAtaque(List.of(new Point(5, 5)));

        assertTrue(impacto);
        assertFalse(fallo);

        assertEquals(CasillaEstado.IMPACTADO, tablero.getEstadoCasilla(0, 0));
        assertEquals(CasillaEstado.ATACADO, tablero.getEstadoCasilla(5, 5));
    }

    /**
     * Prueba el hundimiento de un barco al recibir todos los impactos
     * y valida que el tablero lo registre correctamente.
     */
    @Test
    void testBuscarBarcoPorCasillaYHundimiento() {
        Barco crucero = new Crucero();
        tablero.colocarBarco(crucero, 0, 0);

        tablero.recibirAtaque(List.of(new Point(0, 0), new Point(0, 1), new Point(0, 2)));

        assertTrue(crucero.estaHundido());
        assertTrue(tablero.estanTodosBarcosHundidos());
    }

    /**
     * Verifica que el contador de barcos hundidos se incremente correctamente.
     */
    @Test
    void testContadorBarcosCaidos() {
        Barco crucero = new Crucero();
        tablero.colocarBarco(crucero, 0, 0);
        tablero.recibirAtaque(List.of(new Point(0, 0), new Point(0, 1), new Point(0, 2)));
        assertEquals(1, tablero.contadorBarcosCaidos());
    }

    /**
     * Comprueba que se respete el límite máximo de barcos por tipo al colocarlos.
     */
    @Test
    void testPuedeColocarSegunLimite() {
        for (int i = 0; i < 4; i++) {
            assertTrue(tablero.puedeColocarBarco(CasillaEstado.SUBMARINO));
            tablero.colocarBarco(new Submarino(), i, 0);
        }
        assertFalse(tablero.puedeColocarBarco(CasillaEstado.SUBMARINO));
    }

    /**
     * Valida que la vista del oponente solo muestre información limitada,
     * y actualice el estado tras un ataque.
     */
    @Test
    void testVistaOponente() {
        Barco sub = new Submarino();
        tablero.colocarBarco(sub, 0, 0);

        assertEquals(CasillaEstado.AGUA, tablero.getEstadoCasillaVistaOponente(0, 0));

        tablero.recibirAtaque(List.of(new Point(0, 0)));
        assertEquals(CasillaEstado.IMPACTADO, tablero.getEstadoCasillaVistaOponente(0, 0));
    }

    /**
     * Verifica que el tablero reconozca cuando todos los barcos
     * requeridos han sido colocados.
     */
    @Test
    void testEstanTodosLosBarcosColocados() {
        for (int i = 0; i < 4; i++) tablero.colocarBarco(new Submarino(), i, 0);
        for (int i = 0; i < 3; i++) tablero.colocarBarco(new Destructor(), i, 2);
        for (int i = 0; i < 2; i++) tablero.colocarBarco(new Crucero(), i, 4);
        tablero.colocarBarco(new Portaviones(), 0, 6);

        assertTrue(tablero.estanTodosLosBarcosColocados());
    }
}
