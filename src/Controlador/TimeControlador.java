package Controlador;

import javax.swing.JLabel;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Controlador de tiempo concurrente que utiliza un {@code ScheduledExecutorService}
 * para llevar la cuenta de los segundos transcurridos en el juego y actualizar
 * un {@code JLabel} en el formato mm:ss.
 */
public class TimeControlador {
    private final ScheduledExecutorService scheduler;
    private final AtomicInteger segundosTranscurridos;
    private ScheduledFuture<?> tareaProgramada;
    private final JLabel lblTime;

    /**
     * Constructor del controlador de tiempo.
     * @param lblTime El {@code JLabel} en la vista que mostrará el tiempo transcurrido.
     */
    public TimeControlador(JLabel lblTime) {
        this.lblTime = lblTime;
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.segundosTranscurridos = new AtomicInteger(0);
    }

    /**
     * Inicia el contador de tiempo, ejecutando una tarea cada segundo para
     * incrementar el contador y actualizar el JLabel.
     */
    public void iniciar() {
        tareaProgramada = scheduler.scheduleAtFixedRate(() -> {
            int segundos = segundosTranscurridos.incrementAndGet();

            // Convertir a formato mm:ss
            int minutos = segundos / 60;
            int restoSegundos = segundos % 60;
            String texto = String.format("TIME: %02d:%02d", minutos, restoSegundos);

            // Actualizar el JLabel en el hilo de Swing
            javax.swing.SwingUtilities.invokeLater(() -> lblTime.setText(texto));
        }, 0, 1, TimeUnit.SECONDS);
    }

    /**
     * Detiene el contador de tiempo y apaga el servicio de ejecución.
     */
    public void detener() {
        if (tareaProgramada != null && !tareaProgramada.isCancelled()) {
            tareaProgramada.cancel(true);
        }
        scheduler.shutdownNow();
    }

    /**
     * Obtiene la cantidad total de segundos transcurridos desde el inicio.
     * @return El total de segundos.
     */
    public int getSegundosTotales() {
        return segundosTranscurridos.get();
    }

    /**
     * Obtiene el tiempo transcurrido formateado como una cadena "mm:ss".
     * @return La cadena de tiempo formateada.
     */
    public String getTiempoFormateado() {
        int s = segundosTranscurridos.get();
        int min = s / 60;
        int seg = s % 60;
        return String.format("%02d:%02d", min, seg);
    }
}