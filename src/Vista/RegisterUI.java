package Vista;

import Controlador.ControladorRegistro;
import Controlador.GestorPersistencia;
import Controlador.RepositorioUsuarios;
import Modelo.Usuario;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import javax.swing.*;

/**
 * Interfaz gráfica ({@code JFrame}) para el registro de nuevos usuarios.
 * Permite introducir un nombre de usuario y una contraseña, con confirmación.
 */
public class RegisterUI extends JFrame {
    private final JFrame parent;
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JPasswordField txtConfirmar;

    /**
     * Método auxiliar para aplicar opacidad y escalar una imagen, útil para el fondo.
     * (Copiado de {@code LoginUI}).
     * @param imagen La imagen fuente.
     * @param ancho El ancho al que se debe escalar.
     * @param alto El alto al que se debe escalar.
     * @param opacidad El nivel de opacidad (0.0f a 1.0f).
     * @return La imagen procesada como un objeto {@code Image}.
     */
    private Image crearImagenOpacaYEscalada(Image imagen, int ancho, int alto, float opacidad) {
        // 1. Convertir la imagen original a un BufferedImage ARGB compatible
        BufferedImage bimage = new BufferedImage(imagen.getWidth(null), imagen.getHeight(null), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bimage.createGraphics();
        g.drawImage(imagen, 0, 0, null);
        g.dispose();
        
        // 2. Escalar la imagen
        Image imagenEscalada = bimage.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

        // 3. Crear el buffer FINAL para aplicar opacidad (solo si no es 1.0f)
        if (opacidad >= 1.0f) {
            return imagenEscalada; 
        }

        // 4. Aplicar opacidad
        BufferedImage bufferedImageFinal = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bufferedImageFinal.createGraphics();
        
        AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacidad);
        g2.setComposite(ac);
        
        g2.drawImage(imagenEscalada, 0, 0, null);
        g2.dispose();
        
        return bufferedImageFinal;
    }

    /**
     * Crea una nueva instancia de {@code RegisterUI}.
     * @param parent La ventana padre (normalmente {@code LoginUI}) para poder volver.
     */
    public RegisterUI(JFrame parent) {
        setLocationByPlatform(true);
        setResizable(false);
        this.parent = parent;
        setTitle("Batalla Naval - Registro");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(845, 544);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(20, 20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(null); 


        final int X_START = 228;
        final int WIDTH = 344;
        final int HEIGHT = 35;
        
        final int Y_BASE = 110; 
        final int Y_SPACING = 80;

        // --- CAMPO USUARIO ---
        JLabel labelUsuario = new JLabel("Nuevo Usuario:");
        labelUsuario.setForeground(new Color(255, 255, 255));
        labelUsuario.setFont(new Font("Barlow Condensed", Font.BOLD, 17));
        labelUsuario.setBounds(X_START, Y_BASE, 167, 35);
        panel.add(labelUsuario);
        
        txtUsuario = new JTextField(15);
        txtUsuario.setFont(new Font("Ebrima", Font.BOLD, 16));
        txtUsuario.setForeground(new Color(255, 255, 255));
        txtUsuario.setBorder(null);
        txtUsuario.setBounds(X_START, Y_BASE + HEIGHT, WIDTH, HEIGHT);
        txtUsuario.setBackground(new Color(0, 0, 0, 0));
        txtUsuario.setOpaque(false);
        panel.add(txtUsuario);
        
        JSeparator separator_user = new JSeparator();
        separator_user.setBackground(new Color(255, 255, 255));
        separator_user.setBounds(X_START, Y_BASE + HEIGHT + HEIGHT - 3, WIDTH, 2);
        panel.add(separator_user);


        // --- CAMPO CONTRASEÑA ---
        JLabel labelContrasena = new JLabel("Contraseña:");
        labelContrasena.setForeground(new Color(255, 255, 255));
        labelContrasena.setFont(new Font("Barlow Condensed", Font.BOLD, 17));
        labelContrasena.setBounds(X_START, Y_BASE + Y_SPACING, 167, 35);
        panel.add(labelContrasena);
        
        txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(new Font("Ebrima", Font.BOLD, 16));
        txtContrasena.setForeground(new Color(255, 255, 255));
        txtContrasena.setBorder(null);
        txtContrasena.setBackground(new Color(0, 0, 0, 0));
        txtContrasena.setOpaque(false);
        txtContrasena.setBounds(X_START, Y_BASE + Y_SPACING + HEIGHT, WIDTH, HEIGHT);
        panel.add(txtContrasena);
        
        JSeparator separator_pass = new JSeparator();
        separator_pass.setBackground(new Color(255, 255, 255));
        separator_pass.setBounds(X_START, Y_BASE + Y_SPACING + HEIGHT + HEIGHT - 3, WIDTH, 2);
        panel.add(separator_pass);


        // --- CAMPO CONFIRMAR CONTRASEÑA ---
        JLabel labelConfirmar = new JLabel("Confirmar Contraseña:");
        labelConfirmar.setForeground(new Color(255, 255, 255));
        labelConfirmar.setFont(new Font("Barlow Condensed", Font.BOLD, 17));
        labelConfirmar.setBounds(X_START, Y_BASE + 2 * Y_SPACING, 250, 35);
        panel.add(labelConfirmar);
        
        txtConfirmar = new JPasswordField(15);
        txtConfirmar.setFont(new Font("Ebrima", Font.BOLD, 16));
        txtConfirmar.setForeground(new Color(255, 255, 255));
        txtConfirmar.setBorder(null);
        txtConfirmar.setBackground(new Color(0, 0, 0, 0));
        txtConfirmar.setOpaque(false);
        txtConfirmar.setBounds(X_START, Y_BASE + 2 * Y_SPACING + HEIGHT, WIDTH, HEIGHT);
        panel.add(txtConfirmar);
        
        JSeparator separator_confirm = new JSeparator();
        separator_confirm.setBackground(new Color(255, 255, 255));
        separator_confirm.setBounds(X_START, Y_BASE + 2 * Y_SPACING + HEIGHT + HEIGHT - 3, WIDTH, 2);
        panel.add(separator_confirm);


        // --- BOTÓN REGISTRAR ---
        JButton btnRegister = new JButton("REGISTRAR");
        btnRegister.setFont(new Font("Barlow Condensed", Font.BOLD, 30));
        btnRegister.setForeground(new Color(255, 255, 255));
        btnRegister.setBackground(new Color(110, 30, 30));
        btnRegister.setBounds(270, 383, 280, 67); 
        btnRegister.setBorderPainted(false);
        btnRegister.setBorder(null);
        btnRegister.addActionListener(e -> intentarRegistro());
        panel.add(btnRegister);
        
        
        // --- BOTÓN VOLVER ---
        JButton btnVolver = new JButton("Volver");
        btnVolver.setFont(new Font("Barlow Condensed", Font.BOLD, 17));
        btnVolver.setForeground(new Color(255, 255, 255));
        
        btnVolver.setBounds(326, 461, 167, 35); 
        
        btnVolver.setBackground(new Color(110, 30, 30)); 
        btnVolver.setBorder(null);
        btnVolver.addActionListener(e -> volver());
        panel.add(btnVolver);
        

        // --- FONDO DE IMAGEN ---
        JLabel imgInicio = new JLabel("");
        imgInicio.setHorizontalAlignment(SwingConstants.CENTER);
        
        imgInicio.setBounds(0, 11, 828, 481); 
        
        imgInicio.setForeground(new Color(29, 33, 35));
        imgInicio.setFont(new Font("Leelawadee UI", Font.BOLD, 11));

        ImageIcon ico1 = new ImageIcon(parent.getClass().getResource("/imagenes/FondoLogin.png"));
        Image imagenOriginal = ico1.getImage();
        
        float opacidad = 0.8f; 
        Image imagenOpaca = crearImagenOpacaYEscalada(imagenOriginal, 830, 510, opacidad);
        
        imgInicio.setIcon(new ImageIcon(imagenOpaca));
        
        panel.add(imgInicio); 
        // --- FIN FONDO DE IMAGEN ---


        getContentPane().add(panel);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent windowEvent) {
                volver();
            }
        });
    }

    /**
     * Intenta registrar un nuevo usuario con los datos introducidos.
     * Muestra mensajes de éxito o error basados en la validación y el proceso del controlador.
     */
    private void intentarRegistro() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());
        String confirm = new String(txtConfirmar.getPassword());

        ControladorRegistro ctrl = new ControladorRegistro(new RepositorioUsuarios());
        if (ctrl.registrar(user, pass, confirm)) {
            JOptionPane.showMessageDialog(this, "Usuario registrado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            volver();
        } else {
            JOptionPane.showMessageDialog(this, "Error en el registro. Verifique los datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Cierra la ventana actual y hace visible la ventana padre.
     */
    private void volver() {
        parent.setVisible(true);
        this.dispose();
    }
}