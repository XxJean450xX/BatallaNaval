package Main;

import Controlador.ControladorAtaque;
import Controlador.ControladorBarcos;
import Controlador.ControladorMaquina;
import Controlador.ControladorTurnos;
import Modelo.Tablero;
import Modelo.Usuario;
import Vista.VistaPrincipal;

public class main {

    public static void main(String[] args) {
        // Aquí deberías iniciar desde el Login, no directamente el juego
        new Vista.LoginUI().setVisible(true);
    }

    public static void IniciarJuego(Usuario usuario) {
        final int DIMENSION = 10;

        // 1. Crear Modelos
        Tablero modeloJugador = new Tablero(DIMENSION);
        Tablero modeloOponente = new Tablero(DIMENSION);

        // 2. Crear la Vista (ya con el usuario)
        VistaPrincipal vista = new VistaPrincipal(DIMENSION, usuario);

        // 3. Crear controladores
        ControladorMaquina ctrlIA = new ControladorMaquina(modeloOponente, modeloJugador);
        ControladorTurnos turnoManager = new ControladorTurnos(vista, modeloJugador, modeloOponente, ctrlIA, usuario.getUsername());

        ControladorAtaque ctrlAtaque = new ControladorAtaque(modeloOponente, vista, turnoManager);
        ControladorBarcos ctrlColocacion = new ControladorBarcos(modeloJugador, modeloOponente, vista, turnoManager);
        ctrlColocacion.setControladorAtaque(ctrlAtaque);

        vista.setIA(ctrlIA);
        vista.setControladorInicial(ctrlColocacion, modeloJugador, modeloOponente, ctrlIA);

        vista.setVisible(true);
    }
}
