package Controlador;

import javax.swing.JOptionPane;

import Modelo.Partida;
import Modelo.Tablero;
import Vista.VistaPrincipal;

/**
 * Gestiona el flujo de la partida y el sistema de turnos (Jugador/Máquina) y el fin del juego.
 */
public class ControladorTurnos {

    private final VistaPrincipal vista;
    private final Tablero tableroJugador; 
    private final Tablero tableroIA; 
    private final ControladorMaquina iaControlador;
    private boolean turnoJugador;
    private final String username; 
    private int ataques;
    private long inicioPartida;

    /**
     * Constructor del gestor de turnos.
     * * @param vista La vista principal para la interacción con el usuario.
     * @param jugador El tablero de defensa del jugador.
     * @param ia El tablero de defensa de la IA.
     * @param iaCtrl El controlador de la lógica de la IA.
     * @param username El nombre de usuario que está jugando.
     */
    public ControladorTurnos(VistaPrincipal vista, Tablero jugador, Tablero ia, ControladorMaquina iaCtrl, String username) {
        this.vista = vista;
        this.tableroJugador = jugador;
        this.tableroIA = ia;
        this.iaControlador = iaCtrl;
        this.turnoJugador = true;
        this.username = username;
        this.ataques = 0;
        this.inicioPartida = System.currentTimeMillis();
    }

    /**
     * Inicia la fase de ataque, colocando los barcos de la IA.
     */
    public void iniciarFaseAtaque() {
        iaControlador.colocarBarcosAleatorio();
        System.out.println("¡Partida Iniciada! Turno del Jugador.");
    }
    
    /**
     * Verifica si el juego ha terminado (si alguno de los tableros de defensa tiene todos los barcos hundidos).
     * Si el juego termina, registra la partida, detiene el tiempo y ofrece reiniciar.
     * * @return {@code true} si el juego terminó, {@code false} en caso contrario.
     */
    public boolean verificarFinDeJuego() {
        if (tableroIA.estanTodosBarcosHundidos()) {
            boolean victoria = true;

            vista.getTimer().detener();

            long tiempoTotal = vista.getTimer().getSegundosTotales();

            Partida nueva = new Partida(username, victoria, tiempoTotal, ataques);
            ControladorHistorial ctrlHistorial = new ControladorHistorial(new RepositorioPartidas());
            ctrlHistorial.registrarPartida(nueva);

            System.out.println("[DEBUG] Partida registrada: " + nueva.getTiempoFormateado());

            vista.habilitarInteraccionJugador(false);

            int opcion = JOptionPane.showOptionDialog(
                vista,
                "¡VICTORIA! Has hundido todos los barcos de la IA.\n¿Deseas volver a jugar?",
                "Fin del Juego",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                new Object[]{"Volver a jugar", "Salir"},
                "Volver a jugar"
            );

            if (opcion == JOptionPane.YES_OPTION) vista.reiniciarJuego();
            return true;
        }

        if (tableroJugador.estanTodosBarcosHundidos()) {
            boolean victoria = false;

            vista.getTimer().detener();

            long tiempoTotal = vista.getTimer().getSegundosTotales();

            Partida nueva = new Partida(username, victoria, tiempoTotal, ataques);
            ControladorHistorial ctrlHistorial = new ControladorHistorial(new RepositorioPartidas());
            ctrlHistorial.registrarPartida(nueva);

            System.out.println("[DEBUG] Partida registrada: " + nueva.getTiempoFormateado());

            vista.habilitarInteraccionJugador(false);

            int opcion = JOptionPane.showOptionDialog(
                vista,
                "Has perdido... Todos tus barcos fueron hundidos.\n¿Deseas volver a jugar?",
                "Fin del Juego",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE,
                null,
                new Object[]{"Reintentar", "Salir"},
                "Reintentar"
            );

            if (opcion == JOptionPane.YES_OPTION) vista.reiniciarJuego();
            return true;
        }

        return false; 
    }

    /**
     * Alterna el turno al siguiente jugador (de Jugador a IA o viceversa).
     * * @param impacto Indica si el último ataque resultó en un impacto.
     * @param origen Indica si el turno que acaba de finalizar fue del "Jugador" o "IA".
     */
    public void cambiarTurno(boolean impacto, String origen) {
        
        System.out.println(origen + " finaliza su turno (Impacto: " + (impacto ? "Sí" : "No") + ").");

        this.turnoJugador = !this.turnoJugador;
        
        if (this.turnoJugador) {
            System.out.println("Turno del Jugador.");
            vista.habilitarInteraccionJugador(true);
        } else {
            System.out.println("Turno de la IA...");
            vista.habilitarInteraccionJugador(false);
            ejecutarTurnoIA();
        }
    }

    /**
     * Ejecuta la lógica del turno de la IA (selección de objetivo, ataque), 
     * actualiza la vista y gestiona el cambio de turno.
     */
    private void ejecutarTurnoIA() {
        
        boolean impactoIA = iaControlador.ejecutarTurnoIA();
        
        vista.actualizarTableros(tableroJugador, tableroIA); 

        if (verificarFinDeJuego()) {
            return; 
        }

        cambiarTurno(impactoIA, "IA");
    }
    
    /**
     * Verifica si es el turno actual del jugador humano.
     * * @return {@code true} si es el turno del jugador, {@code false} si es el de la IA.
     */
    public boolean esTurnoJugador() {
        return turnoJugador;
    }
}