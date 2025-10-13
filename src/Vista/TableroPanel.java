package Vista;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.LineBorder; 

import Modelo.Barco;
import Modelo.CasillaEstado;
import Modelo.Tablero;

/**
 * Componente {@code JPanel} que representa visualmente el tablero de juego (cuadrícula).
 * Contiene botones (casillas) que reflejan el estado del modelo ({@code Tablero}).
 */
public class TableroPanel extends JPanel {

    private final int dimension;
    private final JButton[][] casillas;
    private Tablero modelo;
    private final List<JButton> casillasPrevisualizadas;

    private final boolean esTableroJugador;

    /**
     * Crea un nuevo {@code TableroPanel}.
     * @param dimension La dimensión (N x N) del tablero.
     * @param esTableroJugador {@code true} si es el tablero del jugador (muestra barcos), 
     * {@code false} si es el tablero del oponente (vista oculta).
     */
    public TableroPanel(int dimension, boolean esTableroJugador) {
        this.dimension = dimension;
        this.casillas = new JButton[dimension][dimension];
        this.casillasPrevisualizadas = new ArrayList<>();
        this.esTableroJugador = esTableroJugador;

        setLayout(new GridLayout(dimension, dimension, 2, 2)); 
        
        inicializarCasillas();
    }

    /**
     * Establece el modelo de datos ({@code Tablero}) que esta vista representará.
     * @param modelo El objeto {@code Tablero}.
     */
    public void setModelo(Tablero modelo) {
        this.modelo = modelo;
    }

    /**
     * Inicializa o restablece todos los botones de la cuadrícula a su estado inicial de {@code AGUA}.
     */
    public void inicializarCasillas() {
        removeAll(); 
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                JButton casilla = new JButton();
                casilla.setBackground(CasillaEstado.AGUA.getColor());
                casilla.setName(i + "," + j);
                casilla.setBorder(new LineBorder(Color.BLACK, 1));
                casillas[i][j] = casilla;
                add(casilla);
            }
        }
        revalidate(); 
        repaint();  
    }
    
    /**
     * Remueve un {@code MouseListener} específico de todas las casillas.
     * @param listener El {@code MouseListener} a remover.
     */
    public void removerMouseListenerCasillas(MouseListener listener) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                casillas[i][j].removeMouseListener(listener);
            }
        }
    }

    /**
     * Actualiza la vista del tablero basándose en el estado del modelo.
     * Aplica la lógica de visibilidad (jugador vs. oponente) para determinar el color de cada casilla.
     * @param tablero El objeto {@code Tablero} con los datos actualizados.
     */
    public void actualizarVista(Tablero tablero) {
        this.modelo = tablero; 
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                CasillaEstado estado;

                if (esTableroJugador) {
                    estado = tablero.getEstadoCasilla(i, j); 
                } else {
                    estado = tablero.getEstadoCasillaVistaOponente(i, j); 
                }

                casillas[i][j].setBackground(estado.getColor());
            }
        }
    }
    
    /**
     * Añade un {@code MouseListener} a todas las casillas del tablero.
     * @param listener El {@code MouseListener} a añadir.
     */
    public void agregarMouseListenerCasillas(MouseListener listener) {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                casillas[i][j].addMouseListener(listener);
            }
        }
    }

    /**
     * Muestra una previsualización de la colocación de un barco en el tablero
     * pintando temporalmente las casillas.
     * @param barco El barco a previsualizar (su tamaño y orientación).
     * @param fila Fila de la casilla inicial.
     * @param col Columna de la casilla inicial.
     */
    public void previsualizarBarco(Barco barco, int fila, int col) {
        limpiarPrevisualizacion();
        
        int tamano = barco.getTamano();
        Color colorPrevisualizacion = Color.LIGHT_GRAY;

        if (barco.getHorizontal()) {
            for (int i = 0; i < tamano && (col + i) < dimension; i++) {
                JButton casilla = casillas[fila][col + i];
                casillasPrevisualizadas.add(casilla);
                casilla.setBackground(colorPrevisualizacion);
            }
        } else { // VERTICAL
            for (int i = 0; i < tamano && (fila + i) < dimension; i++) {
                JButton casilla = casillas[fila + i][col];
                casillasPrevisualizadas.add(casilla);
                casilla.setBackground(colorPrevisualizacion);
            }
        }
    }

    /**
     * Elimina cualquier previsualización de barco restaurando el color original de las casillas.
     */
    public void limpiarPrevisualizacion() {
        if (modelo == null) return;

        for (JButton casilla : casillasPrevisualizadas) {
            String[] coords = casilla.getName().split(",");
            int fila = Integer.parseInt(coords[0]);
            int col = Integer.parseInt(coords[1]);
            
            CasillaEstado estado;
            // Se restaura el color usando la lógica de vista correcta (jugador/oponente)
            if (esTableroJugador) {
                estado = modelo.getEstadoCasilla(fila, col);
            } else {
                estado = modelo.getEstadoCasillaVistaOponente(fila, col);
            }
            
            casilla.setBackground(estado.getColor());
        }
        casillasPrevisualizadas.clear();
    }
    
    /**
     * Obtiene el modelo de datos ({@code Tablero}) actualmente asociado a este panel.
     * @return El objeto {@code Tablero}.
     */
    public Tablero getModelo() {
        return modelo;
    }
}