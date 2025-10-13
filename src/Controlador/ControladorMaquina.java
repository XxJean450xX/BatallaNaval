package Controlador;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import Modelo.*;

/**
 * Controla la lógica de la Máquina (IA): Posicionamiento inicial y Ataque.
 */
public class ControladorMaquina {

    private final Tablero tableroIA; 
    private Tablero tableroObjetivo; 
    private final Random random;
    private Point ultimoImpacto; 
    private final List<Point> casillasVisitadas; 

    private boolean bombaUsada = false;
    private boolean nuclearUsada = false;
    private boolean alAzarUsado = false;

    /**
     * Constructor del controlador de la IA.
     * * @param tableroIA El tablero donde la IA coloca sus barcos (su defensa).
     * @param tableroObjetivo El tablero del jugador que la IA debe atacar (el objetivo).
     */
    public ControladorMaquina(Tablero tableroIA, Tablero tableroObjetivo) {
        this.tableroIA = tableroIA;
        this.tableroObjetivo = tableroObjetivo;
        this.random = new Random();
        this.casillasVisitadas = new ArrayList<>();
        resetearEstado();
    }
    
    /** * Resetea el tablero objetivo (Modelo del Jugador) después de un reinicio 
     * y reinicia el estado de ataque de la IA.
     * * @param nuevoTableroObjetivo El nuevo tablero del jugador para atacar.
     */
    public void resetearModelo(Tablero nuevoTableroObjetivo) {
        this.tableroObjetivo = nuevoTableroObjetivo;
        resetearEstado();
    }
    
    /** * Reinicia la lógica de búsqueda de la IA, las casillas visitadas y los usos de ataques especiales. 
     */
    public void resetearEstado() {
        this.ultimoImpacto = null;
        this.casillasVisitadas.clear();
        this.bombaUsada = false;
        this.nuclearUsada = false;
        this.alAzarUsado = false;
    }
    
    /** * Coloca todos los barcos de la IA de forma aleatoria en su tablero. 
     */
    public void colocarBarcosAleatorio() {
        int dimension = tableroIA.getDimension();
        List<Barco> barcos = new ArrayList<>();
        for (int i = 0; i < 4; i++) barcos.add(new Submarino());
        for (int i = 0; i < 3; i++) barcos.add(new Destructor());
        for (int i = 0; i < 2; i++) barcos.add(new Crucero());
        for (int i = 0; i < 1; i++) barcos.add(new Portaviones());
        
        for (Barco barco : barcos) {
            boolean colocado = false;
            while (!colocado) {
                int r = random.nextInt(dimension);
                int c = random.nextInt(dimension);
                boolean horizontal = random.nextBoolean();
                
                barco.setHorizontal(horizontal);
                colocado = tableroIA.colocarBarco(barco, r, c);
            }
        }
        System.out.println("IA: Barcos colocados aleatoriamente.");
    }

    /**
     * Ejecuta el turno de ataque de la IA, seleccionando un objetivo y una estrategia.
     * * @return {@code true} si el ataque resultó en un impacto, {@code false} en caso contrario.
     */
    public boolean ejecutarTurnoIA() {
        Point objetivo = seleccionarObjetivo();
        casillasVisitadas.add(objetivo);
        
        Ataque estrategia = seleccionarEstrategiaAleatoria();
        
        int dimension = tableroObjetivo.getDimension();
        List<Point> casillasAtaque = estrategia.calcularCasillasAtaque(objetivo.x, objetivo.y, dimension);
        boolean impacto = tableroObjetivo.recibirAtaque(casillasAtaque);

        if (impacto) {
            if (tableroObjetivo.getEstadoCasilla(objetivo.x, objetivo.y) == CasillaEstado.IMPACTADO) {
                this.ultimoImpacto = objetivo;
            }
        } else {
            if (ultimoImpacto != null && objetivo.equals(ultimoImpacto)) {
                this.ultimoImpacto = null;
            }
        }
        return impacto;
    }

    /** * Selecciona el punto objetivo de ataque. 
     * Prioriza buscar alrededor de impactos previos, si no, ataca aleatoriamente.
     * * @return El punto (fila, columna) a atacar.
     */
    private Point seleccionarObjetivo() {
        if (ultimoImpacto != null) {
            Point p = intentarDestruir(ultimoImpacto);
            if (p != null) return p;
            this.ultimoImpacto = null;
        }
        
        int r, c;
        int dimension = tableroObjetivo.getDimension();
        Point p;
        do {
            r = random.nextInt(dimension);
            c = random.nextInt(dimension);
            p = new Point(r, c);
        } while (tableroObjetivo.getEstadoCasilla(r, c) == CasillaEstado.ATACADO || 
                 tableroObjetivo.getEstadoCasilla(r, c) == CasillaEstado.IMPACTADO);

        return p;
    }
    
    /** * Busca casillas adyacentes no atacadas al punto del último impacto 
     * para intentar hundir el barco.
     * * @param centro El punto del último impacto exitoso.
     * @return Un punto adyacente no atacado, o {@code null} si no hay.
     */
    private Point intentarDestruir(Point centro) {
        int[][] direcciones = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        List<Point> posiblesObjetivos = new ArrayList<>();
        
        for (int[] dir : direcciones) {
            int r = centro.x + dir[0];
            int c = centro.y + dir[1];
            
            if (tableroObjetivo.esPosicionValida(r, c)) {
                CasillaEstado estado = tableroObjetivo.getEstadoCasilla(r, c);
                if (estado != CasillaEstado.ATACADO && estado != CasillaEstado.IMPACTADO) { 
                    posiblesObjetivos.add(new Point(r, c));
                }
            }
        }
        if (!posiblesObjetivos.isEmpty()) {
            return posiblesObjetivos.get(random.nextInt(posiblesObjetivos.size()));
        }
        return null;
    }

    /**
     * Selecciona aleatoriamente una estrategia de ataque, dando prioridad a las especiales
     * si no han sido usadas. Si todas se usaron o la selección aleatoria falla varias veces,
     * regresa a {@code AtaqueNormal}.
     * * @return Una instancia de una clase que implementa {@code Ataque}.
     */
    private Ataque seleccionarEstrategiaAleatoria() {
        int choice = random.nextInt(4); 

        for (int i = 0; i < 5; i++) {
            switch (choice) {
                case 1:
                    if (!bombaUsada) {
                        bombaUsada = true;
                        System.out.println("[IA] 🔥 Usó ATAQUE BOMBA (3x3 Cruz)");
                        return new AtaqueBomba();
                    }
                    break;
                case 2:
                    if (!nuclearUsada) {
                        nuclearUsada = true;
                        System.out.println("[IA] 💥 Usó ATAQUE NUCLEAR (5x5 Cruz)");
                        return new AtaqueNuclear();
                    }
                    break;
                case 3:
                    if (!alAzarUsado) {
                        alAzarUsado = true;
                        System.out.println("[IA] ⚡ Usó ATAQUE TRIPLE ALEATORIO");
                        return new AtaqueAlazar();
                    }
                    break;
            }
            choice = random.nextInt(4); 
        }

        System.out.println("[IA] 🎯 Usó ATAQUE NORMAL");
        return new AtaqueNormal();
    }
}