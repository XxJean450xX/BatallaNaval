package Vista;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JComboBox;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder; 

import Controlador.ControladorAtaque;
import Controlador.ControladorBarcos;
import Controlador.ControladorMaquina;
import Controlador.ControladorTurnos;
import Controlador.TimeControlador;
import Main.main;
import Modelo.CasillaEstado;
import Modelo.Tablero;
import Modelo.Usuario;

import java.awt.Font;
import javax.swing.DefaultComboBoxModel;
import java.io.File; 
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/**
 * Interfaz gráfica principal ({@code JFrame}) del juego Batalla Naval.
 * Contiene los dos tableros (jugador y oponente), la información de la partida
 * y los controles para la colocación de barcos y los ataques.
 */
public class VistaPrincipal extends JFrame {

    // Style - PALETA MONOCROMÁTICA / SEPIA
    private final Color COLOR_FONDO_OSCURO = new Color(29, 33, 35); // Casi negro, semitransparente
    private final Color COLOR_MARCO_UI = new Color(173, 155, 120); // Tono sepia/militar
    private final Color COLOR_TEXTO_CLARO = new Color(255, 255, 255); // Blanco
    private final Font FUENTE_UI = new Font("Monospaced", Font.BOLD, 14); // Fuente militar/consola

    private final Border BORDE_UI = new LineBorder(COLOR_MARCO_UI, 2, true);
    private final EmptyBorder PADDING_BOTON = new EmptyBorder(5, 10, 5, 10);
    
    // Contenedor principal con la imagen de fondo
    private BackgroundPanel contentPane;
    private JLabel lblScore;

    //Tableros
    private TableroPanel panelJugador;
    private TableroPanel panelOponente;

    // Botones de Barcos
    private JButton btnSubmarino;
    private JButton btnDestructor;
    private JButton btnCrucero;
    private JButton btnPortaviones;
    private JButton btnComenzarJuego;
    
    // ComboBox para los ataques
    private JComboBox<String> cmbTipoAtaque;
    
    // Panel de control
    private JPanel panelControles; 
    private ControladorAtaque controladorAtaqueActual; 
    private ControladorMaquina ia;
    private JButton btnHistorial;
    private JButton btnCerraSesion;
    
    private Usuario usuarioActual;
    private JLabel lblTime;
    private TimeControlador timeControlador;

    

    /**
     * Crea una nueva instancia de {@code VistaPrincipal}.
     * Configura la interfaz de usuario, incluyendo los tableros, el fondo y los controles.
     * @param dimension La dimensión del tablero (N x N).
     * @param usuario El usuario que está jugando la partida.
     */
    public VistaPrincipal(int dimension, Usuario usuario) {
        setTitle("Batalla Naval");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1188, 820); 
        
        // ** 1. Configuración del ContentPane con Fondo **
        this.usuarioActual = usuario;
        this.contentPane = new BackgroundPanel("/imagenes/FondoPartida.png");
        this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(this.contentPane);
        this.contentPane.setLayout(null); 
        
        
        // 2. Panel superior (INFO DE PARTIDA/JUGADOR)
        JPanel panelSuperior = new JPanel();
        panelSuperior.setBounds(40, 30, 1093, 60); 
        panelSuperior.setOpaque(false); 
        this.contentPane.add(panelSuperior);
        panelSuperior.setLayout(null);
        
        // TÍTULO DEL JUEGO
        JLabel lblTitulo = new JLabel("BATALLA NAVAL: JUEGO TACTICO");
        lblTitulo.setForeground(COLOR_MARCO_UI); 
        lblTitulo.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblTitulo.setBounds(300, 0, 500, 25);
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(lblTitulo);
        
        lblScore = new JLabel("RANGO: CAPITAN | ELIMINADOS: | SCORE: ");
        lblScore.setForeground(COLOR_TEXTO_CLARO); 
        lblScore.setFont(FUENTE_UI);
        lblScore.setBounds(300, 30, 500, 20);
        lblScore.setHorizontalAlignment(SwingConstants.CENTER);
        panelSuperior.add(lblScore);

        // Botones de sesión a la izquierda
        btnHistorial = new JButton("HISTORIAL");
        btnHistorial.setFont(FUENTE_UI);
        btnHistorial.setBounds(0, 5, 127, 40);
        btnHistorial.setBackground(COLOR_FONDO_OSCURO); 
        btnHistorial.setBorder(BORDE_UI); 
        btnHistorial.setForeground(COLOR_MARCO_UI); 
        panelSuperior.add(btnHistorial);
        
        btnCerraSesion = new JButton("CERRAR SESION");
        btnCerraSesion.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		dispose();
        		new LoginUI().setVisible(true);
        	}
        });
        btnCerraSesion.setFont(FUENTE_UI);
        btnCerraSesion.setBounds(137, 5, 120, 40);
        btnCerraSesion.setBackground(COLOR_FONDO_OSCURO);
        btnCerraSesion.setBorder(BORDE_UI); 
        btnCerraSesion.setForeground(COLOR_MARCO_UI);
        panelSuperior.add(btnCerraSesion);
        
        // Time
        lblTime = new JLabel("TIME: 00:00");
        lblTime.setForeground(COLOR_TEXTO_CLARO);
        lblTime.setFont(FUENTE_UI);
        lblTime.setBounds(980, 5, 113, 40);
        lblTime.setHorizontalAlignment(SwingConstants.RIGHT);
        panelSuperior.add(lblTime);
        
        timeControlador = new TimeControlador(lblTime);

     // 3. Panel central para los dos tableros (CENTER)
        JPanel panelCentral = new JPanel();
        panelCentral.setBounds(40, 100, 1093, 560);
        panelCentral.setOpaque(false);
        // USAR GridLayout: 1 fila, 2 columnas, con 50px de espacio (gap)
        panelCentral.setLayout(new GridLayout(1, 2, 50, 0)); 
        contentPane.add(panelCentral);
        
        // Configuramos TableroPanel para que también refleje el estilo
        panelJugador = new TableroPanel(dimension, true);
        panelJugador.setOpaque(false); 
        
        panelOponente = new TableroPanel(dimension, false); 
        panelOponente.setOpaque(false); 
        
        panelCentral.add(panelJugador);
        panelCentral.add(panelOponente);


        // 4. Panel inferior para los controles
        this.panelControles = new JPanel(); 
        this.panelControles.setOpaque(false);
        this.panelControles.setBounds(40, 670, 1093, 100);
        this.contentPane.add(this.panelControles);
        this.panelControles.setLayout(null);

        // A. Controles de COLOCACIÓN (Barcos)
        int xOffset = 0;
        int buttonWidth = 135;
        int buttonHeight = 45;
        int spacing = 15;
        
        btnSubmarino = createStyledButton("SUB (4)", CasillaEstado.SUBMARINO.getColor(), xOffset, 15, buttonWidth, buttonHeight);
        xOffset += buttonWidth + spacing;
        btnDestructor = createStyledButton("DEST (3)", CasillaEstado.DESTRUCTOR.getColor(), xOffset, 15, buttonWidth, buttonHeight);
        xOffset += buttonWidth + spacing;
        btnCrucero = createStyledButton("CRUC (2)", CasillaEstado.CRUCERO.getColor(), xOffset, 15, buttonWidth, buttonHeight);
        xOffset += buttonWidth + spacing;
        btnPortaviones = createStyledButton("PORT (1)", CasillaEstado.PORTAVIONES.getColor(), xOffset, 15, buttonWidth, buttonHeight);
        
        this.panelControles.add(btnSubmarino);
        this.panelControles.add(btnDestructor);
        this.panelControles.add(btnCrucero);
        this.panelControles.add(btnPortaviones);
        
        // Botón COMENZAR JUEGO
        btnComenzarJuego = new JButton("START BATTLE");
        btnComenzarJuego.setFont(new Font("Monospaced", Font.BOLD, 18));
        btnComenzarJuego.setBounds(900, 15, 193, 45); 
        btnComenzarJuego.setBackground(COLOR_MARCO_UI); 
        btnComenzarJuego.setBorder(new LineBorder(COLOR_TEXTO_CLARO, 3, true)); 
        btnComenzarJuego.setForeground(COLOR_FONDO_OSCURO); 
        this.panelControles.add(btnComenzarJuego);
        
        // B. ComboBox de Ataques
        String[] tiposAtaque = {"Ataque Normal (1x1)", "Bomba 3x3 Cruz", "Nuclear 5x5 Cruz", "Triple Aleatorio"};
        cmbTipoAtaque = new JComboBox<>(tiposAtaque);
        cmbTipoAtaque.setFont(FUENTE_UI);
        cmbTipoAtaque.setBackground(COLOR_FONDO_OSCURO);
        cmbTipoAtaque.setForeground(COLOR_MARCO_UI);
        cmbTipoAtaque.setBorder(BORDE_UI); 
        // Posicionamiento central
        cmbTipoAtaque.setBounds(470, 15, 200, 45); 
        this.panelControles.add(cmbTipoAtaque);
        
        // Inicialmente, ocultamos los controles de ataque (ComboBox)
        ocultarControlesAtaque();
        
        // Conexión del botón Historial
        btnHistorial.addActionListener(e -> {
            HistorialUI ventanaHistorial = new HistorialUI(usuarioActual.getUsername());
            ventanaHistorial.setVisible(true);
        });
    }
    
    /**
     * Método auxiliar para crear botones con un estilo unificado.
     * @param text El texto del botón.
     * @param bgColor El color de fondo base (se oscurecerá).
     * @param x Posición x.
     * @param y Posición y.
     * @param w Ancho.
     * @param h Alto.
     * @return El botón estilizado.
     */
    private JButton createStyledButton(String text, Color bgColor, int x, int y, int w, int h) {
        JButton button = new JButton(text);
        button.setFont(FUENTE_UI);
        button.setBounds(x, y, w, h);
        button.setForeground(COLOR_TEXTO_CLARO);
        button.setBackground(bgColor.darker().darker()); 
        button.setBorder(BORDE_UI); 
        return button;
    }
    
    /**
     * {@code JPanel} personalizado que se utiliza como contenedor principal
     * para dibujar una imagen de fondo escalada y con un overlay semi-transparente.
     */
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        /**
         * Carga la imagen de fondo desde la ruta especificada.
         * @param path La ruta del recurso de imagen.
         */
        public BackgroundPanel(String path) {
            try {
                ImageIcon icon = new ImageIcon(getClass().getResource(path));
                this.backgroundImage = icon.getImage();
            } catch (Exception e) {
                System.err.println("Error al cargar la imagen de fondo: " + e.getMessage());
            }
        }

        @Override
        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                // Dibujar la imagen escalada para que cubra todo el panel
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                
                // Aplicar un overlay oscuro y semi-transparente
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(new Color(0, 0, 0, 100)); // Negro, 100/255 de transparencia
                g2d.fillRect(0, 0, getWidth(), getHeight());
                g2d.dispose();
            }
        }
    }
    
    // --- MÉTODOS DE CONEXIÓN ---
    
    /**
     * Establece y conecta los controladores y modelos iniciales para la fase de colocación de barcos.
     * @param controlador El controlador de la fase de colocación ({@code ControladorBarcos}).
     * @param modeloJugador El modelo de tablero del jugador.
     * @param modeloOponente El modelo de tablero del oponente.
     * @param ia La instancia del controlador de la máquina (IA).
     */
    public void setControladorInicial(ControladorBarcos controlador, Tablero modeloJugador, Tablero modeloOponente, ControladorMaquina ia) {
        this.ia = ia; 
        
        panelJugador.setModelo(modeloJugador);
        panelOponente.setModelo(modeloOponente);
        
        panelJugador.agregarMouseListenerCasillas(controlador);
        
        btnSubmarino.addActionListener(controlador);
        btnDestructor.addActionListener(controlador);
        btnCrucero.addActionListener(controlador);
        btnPortaviones.addActionListener(controlador);
        btnComenzarJuego.addActionListener(controlador);

        btnSubmarino.setActionCommand("seleccionar_submarino");
        btnDestructor.setActionCommand("seleccionar_destructor");
        btnCrucero.setActionCommand("seleccionar_crucero");
        btnPortaviones.setActionCommand("seleccionar_portaviones");
        btnComenzarJuego.setActionCommand("comenzar_juego");
    }
    
    /** * Activa el controlador de ataque y conecta el ComboBox de tipos de ataque con sus acciones. 
     * @param controlador El controlador de la fase de ataque ({@code ControladorAtaque}).
     */
    public void activarControladorAtaque(ControladorAtaque controlador) {
        this.controladorAtaqueActual = controlador; 
        panelOponente.agregarMouseListenerCasillas(controlador); 
        cmbTipoAtaque.addActionListener(controlador);
        cmbTipoAtaque.setActionCommand("seleccionar_ataque"); 
    }
    
    /**
     * Remueve el controlador de colocación y todos los listeners asociados a la fase inicial.
     * @param controlador El controlador de la fase de colocación ({@code ControladorBarcos}).
     */
    public void desactivarControladorColocacion(ControladorBarcos controlador) {
        panelJugador.removerMouseListenerCasillas(controlador); 
        
        btnSubmarino.removeActionListener(controlador);
        btnDestructor.removeActionListener(controlador);
        btnCrucero.removeActionListener(controlador);
        btnPortaviones.removeActionListener(controlador);
        btnComenzarJuego.removeActionListener(controlador);
    }

    // --- MÉTODOS DE CONTROL VISUAL ---
    
    /** Oculta todos los botones de colocación de barcos. */
    public void ocultarControlesColocacion() {
        btnSubmarino.setVisible(false);
        btnDestructor.setVisible(false);
        btnCrucero.setVisible(false);
        btnPortaviones.setVisible(false);
        btnComenzarJuego.setVisible(false);
        panelControles.revalidate();
    }

    /** Muestra el ComboBox de tipos de ataque y reutiliza el botón "Comenzar Juego" como "ATTACK!". */
    public void mostrarControlesAtaque() {
        btnComenzarJuego.setText("ATTACK!"); 
        btnComenzarJuego.setActionCommand("iniciar_ataque");
        btnComenzarJuego.setVisible(true);
        cmbTipoAtaque.setVisible(true); 
        panelControles.revalidate();
    }
    
    /** Oculta el ComboBox de tipos de ataque. */
    public void ocultarControlesAtaque() {
        cmbTipoAtaque.setVisible(false); 
        panelControles.revalidate();
    }
    
    /** Muestra los botones de colocación de barcos. */
    public void mostrarControlesColocacion() {
        btnSubmarino.setVisible(true);
        btnDestructor.setVisible(true);
        btnCrucero.setVisible(true);
        btnPortaviones.setVisible(true);
        btnComenzarJuego.setText("START BATTLE"); // Asegurar el texto original
        btnComenzarJuego.setVisible(true);
        cmbTipoAtaque.setVisible(false); 
        panelControles.revalidate();
    }
    
    // --- GETTERS ---
    /** @return El panel que muestra el tablero del jugador. */
    public TableroPanel getPanelJugador() { return panelJugador; }
    /** @return El panel que muestra el tablero del oponente. */
    public TableroPanel getPanelOponente() { return panelOponente; }
    /** @return El ComboBox utilizado para seleccionar el tipo de ataque. */
    public JComboBox<String> getCmbTipoAtaque() { return cmbTipoAtaque; }

    /**
     * Actualiza la vista de ambos tableros utilizando los modelos de datos más recientes.
     * @param modeloJugador El nuevo modelo del tablero del jugador.
     * @param modeloOponente El nuevo modelo del tablero del oponente.
     */
    public void actualizarTableros(Tablero modeloJugador, Tablero modeloOponente) {
        if (modeloJugador != null) panelJugador.actualizarVista(modeloJugador);
        if (modeloOponente != null) panelOponente.actualizarVista(modeloOponente);
    }
    
    /** * @return El controlador de ataque actualmente activo. 
     */
 	public ControladorAtaque getControladorAtaque() { 
        return controladorAtaqueActual; 
    }

    
    /**
     * Habilita o deshabilita la interacción del jugador en la fase de ataque.
     * @param habilitar {@code true} para permitir clics en el tablero del oponente y uso de controles; {@code false} en caso contrario.
     */
    public void habilitarInteraccionJugador(boolean habilitar) {
        if (this.controladorAtaqueActual == null) return;
        
        if (habilitar) {
            panelOponente.agregarMouseListenerCasillas(controladorAtaqueActual); 
        } else {
            panelOponente.removerMouseListenerCasillas(controladorAtaqueActual); 
        }
        cmbTipoAtaque.setEnabled(habilitar);
        btnComenzarJuego.setEnabled(habilitar); 
    }
    
    /**
     * Muestra un diálogo de mensaje al finalizar el juego.
     * @param mensaje El mensaje a mostrar (Victoria o Derrota).
     */
    public void mostrarMensajeFinJuego(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Fin de la Partida", JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Actualiza el puntaje y la información del juego en la etiqueta superior.
     * @param suma El número total de eliminaciones o el score base.
     */
    public void actualizarPuntaje(int suma) {
    	lblScore.setText("RANGO: CAPITAN | ELIMINACIONES: " + suma + " | SCORE: " + suma * 150 + " PTS");
    	lblScore.revalidate();
    	lblScore.repaint();
    }
    
    /**
     * Cierra la ventana actual y reinicia el juego abriendo una nueva instancia
     * de la vista principal para el mismo usuario.
     */
    public void reiniciarJuego() {
        this.dispose();
        main.IniciarJuego(usuarioActual); 
    }
    
    /**
     * Establece la instancia del controlador de la máquina (IA).
     * @param ia El controlador de la máquina.
     */
    public void setIA(ControladorMaquina ia) {
        this.ia = ia;
    }
    
    /**
     * @return El controlador del tiempo de la partida.
     */
    public TimeControlador getTimer() {
        return timeControlador;
    }
}