package Controlador;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import Modelo.Ataque;
import Modelo.AtaqueAlazar;
import Modelo.AtaqueBomba;
import Modelo.AtaqueNormal;
import Modelo.AtaqueNuclear;
import Modelo.CasillaEstado;
import Modelo.Tablero;
import Vista.TableroPanel;
import Vista.VistaPrincipal;

/**
 * Controlador que gestiona la interacción del jugador con el tablero oponente
 * para realizar ataques. Implementa la interfaz {@code ActionListener} para
 * el selector de ataque y {@code MouseListener} para el clic en las casillas.
 */
public class ControladorAtaque implements ActionListener, MouseListener {

    private final Tablero tableroOponente;    
    private final TableroPanel panelOponente;    
    private final VistaPrincipal vista;
    private Ataque estrategiaAtaqueActual;

    private final ControladorTurnos turnoManager;
    
    private boolean bombaUsada = false;
    private boolean nuclearUsada = false;
    private boolean alAzarUsado = false;
    
    /**
     * Constructor del controlador de ataque.
     * * @param tableroOponente El modelo del tablero del oponente (IA) a atacar.
     * @param vista La vista principal de la aplicación.
     * @param turnoManager El gestor de turnos de la partida.
     */
    public ControladorAtaque(Tablero tableroOponente, VistaPrincipal vista, ControladorTurnos turnoManager) {
        this.tableroOponente = tableroOponente;
        this.vista = vista;
        this.panelOponente = vista.getPanelOponente();        
        this.turnoManager = turnoManager;
        this.estrategiaAtaqueActual = new AtaqueNormal();    
    }
    
    /**
     * Reinicia el estado de uso de los ataques especiales a {@code false} y
     * establece la estrategia actual a {@code AtaqueNormal}.
     */
    public void resetearUsos() {
        this.bombaUsada = false;
        this.nuclearUsada = false;
        this.alAzarUsado = false;
        this.estrategiaAtaqueActual = new AtaqueNormal();
    }

    /**
     * Maneja el evento de selección del tipo de ataque en el {@code JComboBox}.
     * * @param e El evento de acción.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if ("seleccionar_ataque".equals(e.getActionCommand())) {
            @SuppressWarnings("unchecked")
            JComboBox<String> cmb = (JComboBox<String>) e.getSource();
            String seleccion = (String) cmb.getSelectedItem();
            
            if ("Bomba 3x3 Cruz".equals(seleccion) && bombaUsada) {
                JOptionPane.showMessageDialog(vista, "El Ataque Bomba (3x3 Cruz) ya fue utilizado.", "Límite Alcanzado", JOptionPane.WARNING_MESSAGE);
                cmb.setSelectedItem("Ataque Normal (1x1)");
                estrategiaAtaqueActual = new AtaqueNormal();
                return;
            } else if ("Nuclear 5x5 Cruz".equals(seleccion) && nuclearUsada) {
                JOptionPane.showMessageDialog(vista, "El Ataque Nuclear (5x5 Cruz) ya fue utilizado.", "Límite Alcanzado", JOptionPane.WARNING_MESSAGE);
                cmb.setSelectedItem("Ataque Normal (1x1)");
                estrategiaAtaqueActual = new AtaqueNormal();
                return;
            } else if ("Triple Aleatorio".equals(seleccion) && alAzarUsado) {
                JOptionPane.showMessageDialog(vista, "El Ataque Triple Aleatorio ya fue utilizado.", "Límite Alcanzado", JOptionPane.WARNING_MESSAGE);
                cmb.setSelectedItem("Ataque Normal (1x1)");
                estrategiaAtaqueActual = new AtaqueNormal();
                return;
            }
            
            switch (seleccion) {
                case "Ataque Normal (1x1)": estrategiaAtaqueActual = new AtaqueNormal(); break;
                case "Bomba 3x3 Cruz": estrategiaAtaqueActual = new AtaqueBomba(); break;
                case "Nuclear 5x5 Cruz": estrategiaAtaqueActual = new AtaqueNuclear(); break;
                case "Triple Aleatorio": estrategiaAtaqueActual = new AtaqueAlazar(); break;
                default: estrategiaAtaqueActual = new AtaqueNormal();    
            }
            System.out.println("Estrategia: " + estrategiaAtaqueActual.getClass().getSimpleName() + " seleccionada.");
        }
    }

    /**
     * Maneja el evento de clic del ratón sobre una casilla del tablero oponente.
     * Solo permite ataques si es el turno del jugador y la casilla no ha sido atacada.
     * * @param e El evento del ratón.
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        if (!turnoManager.esTurnoJugador()) {
            return;        
        }

        if (SwingUtilities.isLeftMouseButton(e) && e.getSource() instanceof JButton) {
            JButton botonClickeado = (JButton) e.getSource();
            if (botonClickeado.getParent() != panelOponente) return;        
            
            String[] coords = botonClickeado.getName().split(",");
            int fila = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);
            
            CasillaEstado estadoActual = tableroOponente.getEstadoCasilla(fila, col);

            if(estadoActual != CasillaEstado.ATACADO && estadoActual != CasillaEstado.IMPACTADO) {
                
                boolean esAtaqueEspecial = !(estrategiaAtaqueActual instanceof AtaqueNormal);

                int dimension = tableroOponente.getDimension();
                
                List<Point> casillasObjetivo = estrategiaAtaqueActual.calcularCasillasAtaque(fila, col, dimension);
                
                boolean impacto = tableroOponente.recibirAtaque(casillasObjetivo);
                
                if (esAtaqueEspecial) {
                    if (estrategiaAtaqueActual instanceof AtaqueBomba) {
                        bombaUsada = true;
                    } else if (estrategiaAtaqueActual instanceof AtaqueNuclear) {
                        nuclearUsada = true;
                    } else if (estrategiaAtaqueActual instanceof AtaqueAlazar) {
                        alAzarUsado = true;
                    }
                    
                    vista.getCmbTipoAtaque().setSelectedItem("Ataque Normal (1x1)");
                    estrategiaAtaqueActual = new AtaqueNormal();
                }

                vista.actualizarTableros(null, tableroOponente);
                
                if (impacto) {
                    System.out.println("¡IMPACTO!");
                } else {
                    System.out.println("AGUA.");
                }
                
                vista.actualizarPuntaje(tableroOponente.contadorBarcosCaidos());
                
                if (!turnoManager.verificarFinDeJuego()) {
                    turnoManager.cambiarTurno(impacto, "Jugador");
                } else {
                    System.out.println("¡Juego terminado por victoria del Jugador!");
                }
                
            } else {
                System.out.println("Casilla ya atacada o impactada. Elige otra casilla.");
            }
        }
    }

    /** No implementado. */
    @Override public void mousePressed(MouseEvent e) {}
    /** No implementado. */
    @Override public void mouseReleased(MouseEvent e) {}
    /** No implementado. */
    @Override public void mouseEntered(MouseEvent e) {}
    /** No implementado. */
    @Override public void mouseExited(MouseEvent e) {}
}