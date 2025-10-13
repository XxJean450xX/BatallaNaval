package Vista;

import Controlador.ControladorHistorial;
import Controlador.GestorPersistencia;
import Controlador.RepositorioPartidas;
import Modelo.Partida;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * Interfaz gráfica ({@code JFrame}) para mostrar el historial de partidas de un usuario específico.
 * Utiliza una {@code JTable} estilizada para presentar los datos de cada partida.
 */
public class HistorialUI extends JFrame {
    private final String username;

    // --- Constantes de Estilo ---
    private static final Color COLOR_FONDO_OSCURO = new Color(20, 20, 20); // Casi negro
    private static final Color COLOR_TEXTO_CLARO = new Color(240, 240, 240); // Blanco roto
    private static final Color COLOR_BORDE = new Color(60, 60, 60); // Gris oscuro sutil
    private static final Color COLOR_BOTON_NORMAL = new Color(50, 50, 50); // Gris oscuro para botones
    private static final Color COLOR_BOTON_CERRAR = new Color(110, 30, 30); // Rojo oscuro (como el botón de Volver/Registro)
    private static final Color COLOR_FILA_GANADA = new Color(30, 80, 50); // Verde oscuro para VICTORIA
    private static final Color COLOR_FILA_PERDIDA = new Color(80, 40, 40); // Rojo oscuro para DERROTA
    
    // --- Fin Constantes de Estilo ---

    /**
     * Crea una nueva instancia de {@code HistorialUI}.
     * @param username El nombre del usuario cuyo historial se va a mostrar.
     */
    public HistorialUI(String username) {
        this.username = username;
        setTitle("Historial de Partidas - " + username);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);
        
        setLayout(new BorderLayout());
        getContentPane().setBackground(COLOR_FONDO_OSCURO); 
        
        // --- Título ---
        JLabel lblTitulo = new JLabel("HISTORIAL DE PARTIDAS DE " + username.toUpperCase(), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Ebrima", Font.BOLD, 22));
        lblTitulo.setForeground(COLOR_TEXTO_CLARO);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        lblTitulo.setBackground(COLOR_FONDO_OSCURO);
        lblTitulo.setOpaque(true);
        add(lblTitulo, BorderLayout.NORTH);

        // --- Configuración de la tabla ---
        String[] columnNames = {"Resultado", "Ataques", "Tiempo", "Fecha (Simulada)"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);
        
        // Estilización de la Tabla
        table.setFont(new Font("Ebrima", Font.PLAIN, 14));
        table.setBackground(COLOR_FONDO_OSCURO.brighter());
        table.setForeground(COLOR_TEXTO_CLARO);
        table.setSelectionBackground(new Color(60, 60, 100));
        table.setGridColor(COLOR_BORDE.darker());
        table.setRowHeight(25);
        
        table.setFillsViewportHeight(true); 
        
        // Estilizar Encabezado de la Tabla
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Ebrima", Font.BOLD, 16));
        header.setBackground(COLOR_BOTON_NORMAL.darker());
        header.setForeground(COLOR_TEXTO_CLARO);
        header.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 1));
        
        // Renderer para el contenido de las celdas (centrado y colores de victoria/derrota)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Centrar el texto en todas las celdas
                setHorizontalAlignment(SwingConstants.CENTER);
                
                // Aplicar color de fondo basado en el resultado (primera columna)
                if (column == 0) {
                    String resultado = (String) table.getValueAt(row, 0);
                    if (resultado.contains("VICTORIA")) {
                        c.setBackground(COLOR_FILA_GANADA);
                        c.setForeground(COLOR_TEXTO_CLARO.brighter());
                    } else if (resultado.contains("DERROTA")) {
                        c.setBackground(COLOR_FILA_PERDIDA);
                        c.setForeground(COLOR_TEXTO_CLARO);
                    }
                } else if (!isSelected) {
                    // Restaurar colores para otras columnas si no está seleccionado
                    c.setBackground(COLOR_FONDO_OSCURO.brighter());
                    c.setForeground(COLOR_TEXTO_CLARO);
                }
                
                return c;
            }
        };
        
        // Aplicar el renderer a todas las columnas
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Cargar datos DESPUÉS de configurar el modelo y la tabla
        cargarDatos(model);

        // El JScrollPane debe tener un fondo oscuro para el borde exterior
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(COLOR_BORDE, 2));
        scrollPane.setBackground(COLOR_FONDO_OSCURO); 
        
        // Solución para el espacio blanco en el viewport
        scrollPane.getViewport().setBackground(COLOR_FONDO_OSCURO); 
        
        add(scrollPane, BorderLayout.CENTER);
        
        // --- Botón Cerrar ---
        JButton btnCerrar = new JButton("CERRAR");
        btnCerrar.setFont(new Font("Ebrima", Font.BOLD, 14));
        btnCerrar.setForeground(COLOR_TEXTO_CLARO);
        btnCerrar.setBackground(COLOR_BOTON_CERRAR);
        btnCerrar.setBorder(null);
        btnCerrar.setPreferredSize(new Dimension(150, 40));
        btnCerrar.setFocusPainted(false);
        btnCerrar.addActionListener(e -> dispose());
        
        JPanel southPanel = new JPanel();
        southPanel.setBackground(COLOR_FONDO_OSCURO);
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        southPanel.add(btnCerrar);
        add(southPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Carga las partidas del historial del usuario actual desde el controlador
     * y las añade al modelo de la tabla.
     * @param model El {@code DefaultTableModel} de la tabla.
     */
    private void cargarDatos(DefaultTableModel model) {
        ControladorHistorial ctrl = new ControladorHistorial(new RepositorioPartidas());
        ctrl.obtenerHistorial(username).forEach(p -> {
            String resultado = p.isVictoria() ? "VICTORIA" : "DERROTA";
            model.addRow(new Object[]{
                resultado,
                p.getAtaquesRealizados(),
                p.getTiempoFormateado(),
                p.getFechaFormateada()
            });
        });
    }
}