package Vista;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import Controlador.ControladorLogin;
import Controlador.GestorPersistencia;
import Controlador.RepositorioUsuarios;
import Main.main;
import Modelo.Usuario;

/**
 * Interfaz gráfica ({@code JFrame}) para el inicio de sesión (Login) de usuarios.
 * Permite ingresar credenciales y acceder a la funcionalidad del juego o
 * navegar a la pantalla de registro.
 */
public class LoginUI extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;

    /**
     * Crea una nueva instancia de {@code LoginUI}.
     * Configura la ventana, los componentes de entrada y los botones de acción.
     */
    public LoginUI() {
        setLocationByPlatform(true);
        setResizable(false);
        setTitle("Batalla Naval - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(844, 541);
        setLocationRelativeTo(null);
        
        JPanel panel = new JPanel();
        panel.setBackground(new Color(20, 20, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setLayout(null);

        JLabel label = new JLabel("Usuario:");
        label.setForeground(new Color(255, 255, 255));
        label.setFont(new Font("Barlow Condensed", Font.BOLD, 17));
        label.setBounds(228, 167, 167, 35);
        panel.add(label);
        txtUsuario = new JTextField(15);
        txtUsuario.setFont(new Font("Ebrima", Font.BOLD, 16));
        txtUsuario.setForeground(new Color(255, 255, 255));
        txtUsuario.setBorder(null);
        txtUsuario.setBounds(228, 204, 344, 35);
        txtUsuario.setBackground(new Color(0, 0, 0, 0));
        txtUsuario.setOpaque(false);
        panel.add(txtUsuario);

        JLabel label_1 = new JLabel("Contraseña:");
        label_1.setForeground(new Color(255, 255, 255));
        label_1.setFont(new Font("Barlow Condensed", Font.BOLD, 17));
        label_1.setBounds(228, 268, 167, 35);
        panel.add(label_1);
        txtContrasena = new JPasswordField(15);
        txtContrasena.setFont(new Font("Ebrima", Font.BOLD, 16));
        txtContrasena.setForeground(new Color(255, 255, 255));
        txtContrasena.setBorder(null);
        txtContrasena.setBounds(228, 302, 344, 35);
        txtContrasena.setBackground(new Color(0, 0, 0, 0));
        txtContrasena.setOpaque(false);
        
        panel.add(txtContrasena);

        JButton btnLogin = new JButton("LOGIN");
        btnLogin.setFont(new Font("Barlow Condensed", Font.BOLD, 30));
        btnLogin.setForeground(new Color(255, 255, 255));
        btnLogin.setBounds(273, 379, 280, 67);
        btnLogin.setBackground(new Color(110, 30, 30));
        btnLogin.setBorderPainted(false);
        btnLogin.setBorder(null);
        btnLogin.addActionListener(e -> intentarLogin());
        panel.add(btnLogin);
        
        JButton btnRegister = new JButton("REGISTRARTE");
        btnRegister.setFont(new Font("Barlow Condensed", Font.BOLD, 17));
        btnRegister.setForeground(new Color(255, 255, 255));
        btnRegister.setBounds(326, 457, 167, 35);
        btnRegister.setBackground(new Color(110, 30, 30));
        btnRegister.setBorder(null);
        btnRegister.addActionListener(e -> abrirRegistro());
        panel.add(btnRegister);

        getContentPane().add(panel);
        
        JSeparator separator = new JSeparator();
        separator.setBounds(228, 240, 344, 2);
        panel.add(separator);
        
        JSeparator separator_1 = new JSeparator();
        separator_1.setBackground(new Color(255, 255, 255));
        separator_1.setBounds(228, 339, 344, 2);
        panel.add(separator_1);
        
        JLabel imgInicio = new JLabel("");
        imgInicio.setHorizontalAlignment(SwingConstants.CENTER);
        imgInicio.setBounds(0, 11, 828, 481);
        panel.add(imgInicio);
        imgInicio.setForeground(new Color(29, 33, 35));
        imgInicio.setFont(new Font("Leelawadee UI", Font.BOLD, 11));

        // Cargar la imagen original
        ImageIcon ico1 = new ImageIcon(LoginUI.class.getResource("/imagenes/FondoLogin.png"));
        Image imagenOriginal = ico1.getImage();

        // Definir la opacidad deseada (ej. 70%)
        float opacidad = 0.8f; 

        // Procesar la imagen: aplicar opacidad y escalar
        Image imagenOpaca = crearImagenOpacaYEscalada(imagenOriginal, 830, 510, opacidad);

        // Asignar la imagen procesada al JLabel
        imgInicio.setIcon(new ImageIcon(imagenOpaca));
    }
    
    /**
     * Intenta iniciar sesión con el nombre de usuario y la contraseña proporcionados.
     * Muestra mensajes de éxito o error y, si es exitoso, inicia la pantalla principal del juego.
     */
    private void intentarLogin() {
        String user = txtUsuario.getText().trim();
        String pass = new String(txtContrasena.getPassword());

        ControladorLogin ctrlLogin = new ControladorLogin(new RepositorioUsuarios());
        ctrlLogin.intentarLogin(user, pass).ifPresentOrElse(usuario -> {
            JOptionPane.showMessageDialog(this, "Bienvenido " + usuario.getUsername(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            main.IniciarJuego(usuario);
            dispose();
        }, () -> JOptionPane.showMessageDialog(this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE));
    }
    
    /**
     * Abre la interfaz de registro ({@code RegisterUI}) y oculta la ventana actual.
     */
    private void abrirRegistro() {
        RegisterUI register = new RegisterUI(this);
        register.setVisible(true);
        this.setVisible(false);
    }
    
    /**
     * Método auxiliar para aplicar opacidad y escalar una imagen, útil para el fondo.
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
}