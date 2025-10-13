package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JOptionPane;    
import javax.swing.SwingUtilities;

import Modelo.Barco;
import Modelo.Submarino;
import Modelo.Destructor;
import Modelo.Crucero;
import Modelo.Portaviones;
import Modelo.Tablero;
import Modelo.CasillaEstado; 
import Vista.VistaPrincipal;

/**
 * Controlador que gestiona la colocación inicial de barcos en el tablero del jugador.
 * Implementa {@code ActionListener} para la selección de barcos y el inicio del juego,
 * y {@code MouseListener} para la interacción con el tablero del jugador.
 */
public class ControladorBarcos implements ActionListener, MouseListener {

    private final Tablero tableroJugador;
    private final Tablero tableroOponente;    
    private final VistaPrincipal vista;
    private Barco barcoParaColocar;
    private ControladorAtaque controladorAtaque;    

    private final ControladorTurnos turnoManager;    

    /**
     * Constructor del controlador de barcos.
     * * @param tableroJugador El modelo del tablero del jugador.
     * @param tableroOponente El modelo del tablero del oponente (IA).
     * @param vista La vista principal de la aplicación.
     * @param turnoManager El gestor de turnos de la partida.
     */
    public ControladorBarcos(Tablero tableroJugador, Tablero tableroOponente, VistaPrincipal vista, ControladorTurnos turnoManager) {
        this.tableroJugador = tableroJugador;
        this.tableroOponente = tableroOponente;
        this.vista = vista;
        this.barcoParaColocar = null;
        this.turnoManager = turnoManager;
    }
    
    /**
     * Establece la referencia al controlador de ataque, necesaria para iniciar la fase de juego.
     * * @param controladorAtaque Instancia del {@code ControladorAtaque}.
     */
    public void setControladorAtaque(ControladorAtaque controladorAtaque) {
        this.controladorAtaque = controladorAtaque;
    }

    /**
     * Maneja el evento de selección de tipo de barco y el inicio del juego.
     * * @param e El evento de acción.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        Barco nuevoBarco = null;

        switch (comando) {
            case "seleccionar_submarino":
                nuevoBarco = new Submarino();
                break;
            case "seleccionar_destructor":
                nuevoBarco = new Destructor();
                break;
            case "seleccionar_crucero":
                nuevoBarco = new Crucero();
                break;
            case "seleccionar_portaviones":
                nuevoBarco = new Portaviones();
                break;
                
            case "comenzar_juego":
                if (!tableroJugador.estanTodosLosBarcosColocados()) {
                    JOptionPane.showMessageDialog(vista,    
                        "¡Debes colocar los 10 barcos (4 Sub, 3 Dest, 2 Cruc, 1 Port) antes de comenzar!",    
                        "Faltan Barcos",    
                        JOptionPane.WARNING_MESSAGE);
                    return;    
                }
                
                if (controladorAtaque != null) {
                    vista.desactivarControladorColocacion(this);
                    vista.activarControladorAtaque(controladorAtaque);
                    vista.ocultarControlesColocacion();    
                    vista.mostrarControlesAtaque();        
                    
                    turnoManager.iniciarFaseAtaque();    
                    System.out.println("¡Fase de ataque iniciada!");
                    vista.getTimer().iniciar();
                }
                break;
        }
        
        if (nuevoBarco != null) {
            if (!tableroJugador.puedeColocarBarco(nuevoBarco.getTipoBarco())) {
                 int max = 0;
                 if (nuevoBarco.getTipoBarco() == CasillaEstado.SUBMARINO) max = 4;
                 else if (nuevoBarco.getTipoBarco() == CasillaEstado.DESTRUCTOR) max = 3;
                 else if (nuevoBarco.getTipoBarco() == CasillaEstado.CRUCERO) max = 2;
                 else if (nuevoBarco.getTipoBarco() == CasillaEstado.PORTAVIONES) max = 1;

                JOptionPane.showMessageDialog(vista,    
                    "Ya has colocado el límite de barcos de tipo " + nuevoBarco.getTipoBarco().name() + " (" + max + " colocados).",    
                    "Límite Alcanzado",    
                    JOptionPane.ERROR_MESSAGE);
                barcoParaColocar = null; 
            } else {
                this.barcoParaColocar = nuevoBarco;
            }
        }
    }

    /**
     * Maneja el evento de clic del ratón sobre una casilla del tablero del jugador.
     * El clic izquierdo intenta colocar el barco; el clic derecho rota el barco.
     * * @param e El evento del ratón.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        JButton botonClickeado = (JButton) e.getSource();
        String[] coords = botonClickeado.getName().split(",");
        int fila = Integer.parseInt(coords[0]);
        int col = Integer.parseInt(coords[1]);

        if (e.getButton() == java.awt.event.MouseEvent.BUTTON3 && barcoParaColocar != null) {
            barcoParaColocar.setHorizontal(!barcoParaColocar.getHorizontal());
            vista.getPanelJugador().previsualizarBarco(barcoParaColocar, fila, col);
        }
        else if (SwingUtilities.isLeftMouseButton(e) && barcoParaColocar != null) {
            boolean colocado = tableroJugador.colocarBarco(barcoParaColocar, fila, col);    
            if (colocado) {
                vista.getPanelJugador().limpiarPrevisualizacion();
                vista.actualizarTableros(tableroJugador, tableroOponente);
                barcoParaColocar = null;    
            } else {
                System.out.println("Posición no válida (colisión o fuera de límites).");
            }
        }
    }

    /**
     * Muestra la previsualización del barco al entrar el ratón en una casilla.
     * * @param e El evento del ratón.
     */
    @Override
    public void mouseEntered(MouseEvent e) {    
        if (barcoParaColocar != null) {
            JButton boton = (JButton) e.getSource();
            String[] coords = boton.getName().split(",");
            vista.getPanelJugador().previsualizarBarco(barcoParaColocar, Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
        }
    }
    
    /**
     * Limpia la previsualización al salir el ratón de la casilla.
     * * @param e El evento del ratón.
     */
    @Override
    public void mouseExited(MouseEvent e) { vista.getPanelJugador().limpiarPrevisualizacion(); }
    /** No implementado. */
    @Override
    public void mousePressed(MouseEvent e) { }    
    /** No implementado. */
    @Override
    public void mouseReleased(MouseEvent e) { }    
}