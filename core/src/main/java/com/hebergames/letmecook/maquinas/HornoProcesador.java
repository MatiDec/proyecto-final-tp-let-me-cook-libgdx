package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entregables.ingredientes.CoccionListener;
import com.hebergames.letmecook.entregables.ingredientes.EstadoCoccion;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.utiles.GestorAudio;
import com.hebergames.letmecook.utiles.Recursos;

public class HornoProcesador implements MaquinaProcesadora, CoccionListener {

    private static int contadorInstancias = 0;
    private final int idInstancia;

    private Ingrediente ingredienteCocinando;
    private boolean procesando = false;
    private float tiempoSonidoTemporizador = 0f;
    private static final float INTERVALO_SONIDO_TEMPORIZADOR = 1f;

    private Texto indicadorEstado;
    private Rectangle area;

    public HornoProcesador(Rectangle area) {
        this.idInstancia = ++contadorInstancias;
        this.area = area;
        inicializarIndicador();
        System.out.println("DEBUG: Creado HornoProcesador #" + idInstancia + " en posición (" + area.x + ", " + area.y + ")");
    }

    private void inicializarIndicador() {
        indicadorEstado = new Texto(Recursos.FUENTE_MENU, 16, Color.WHITE, true);
        float centroX = area.x + area.width / 2f;
        float arribaY = area.y + area.height + 10f;
        indicadorEstado.setPosition(centroX, arribaY);
    }

    @Override
    public boolean puedeIniciarProceso() {
        return !procesando;
    }

    @Override
    public boolean iniciarProceso(Ingrediente ingrediente) {
        System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - iniciarProceso llamado con ingrediente: " + ingrediente.getNombre());
        System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - puedeIniciarProceso: " + puedeIniciarProceso());
        System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - esCocinableInterna: " + ingrediente.esCocinableInterna());
        System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - estaQuemado: " + ingrediente.estaQuemado());

        if (!puedeIniciarProceso() || !ingrediente.esCocinableInterna() || ingrediente.estaQuemado()) {
            System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - No se puede iniciar el proceso");
            return false;
        }

        ingredienteCocinando = ingrediente;
        ingredienteCocinando.setCoccionListener(this);
        procesando = true;

        System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - Proceso iniciado exitosamente - procesando: " + procesando);
        System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - ingredienteCocinando: " + (ingredienteCocinando != null ? ingredienteCocinando.getNombre() : "null"));
        System.out.println("Iniciando cocción de: " + ingrediente.getNombre());
        return true;
    }

    @Override
    public void actualizarProceso(float delta) {
        if (!procesando || ingredienteCocinando == null) return;

        ingredienteCocinando.actualizarCoccion(delta);

        tiempoSonidoTemporizador += delta;
        if (tiempoSonidoTemporizador >= INTERVALO_SONIDO_TEMPORIZADOR) {
            tiempoSonidoTemporizador = 0f;
            System.out.println("Estado:" + ingredienteCocinando.getEstadoCoccion().getNombre());
        }

        if (ingredienteCocinando.estaQuemado()) {
            procesando = false;
        }
    }

    @Override
    public boolean tieneProcesandose() {
        return procesando && ingredienteCocinando != null;
    }

    @Override
    public Ingrediente obtenerResultado() {
        if (!tieneProcesandose()) return null;

        Ingrediente resultado = ingredienteCocinando;
        ingredienteCocinando.setCoccionListener(null); // Limpiar listener
        ingredienteCocinando = null;
        procesando = false;
        tiempoSonidoTemporizador = 0f;

        return resultado;
    }

    @Override
    public void dibujarIndicador(SpriteBatch batch) {
        // Solo debug cuando hay algo importante que reportar
        if (procesando || ingredienteCocinando != null) {
            System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - dibujarIndicador - procesando: " + procesando +
                ", ingrediente: " + (ingredienteCocinando != null ? ingredienteCocinando.getNombre() : "null"));
        }

        String texto;
        if (procesando && ingredienteCocinando != null) {
            EstadoCoccion estado = ingredienteCocinando.getEstadoCoccion();

            if (estado == EstadoCoccion.CRUDO) {
                float progreso = ingredienteCocinando.getTiempoCoccionActual() / ingredienteCocinando.getTiempoCoccionMaximo();
                int barrasLlenas = (int)(progreso * 5);
                StringBuilder barra = new StringBuilder("[");
                for (int i = 0; i < 5; i++) {
                    barra.append(i < barrasLlenas ? "█" : "░");
                }
                barra.append("]");
                texto = "Cocinando " + barra.toString();
            } else {
                texto = estado.getNombre() + " - Shift+Click para retirar";
            }

            System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - Mostrando: " + texto);
        } else {
            texto = "Horno Libre";
        }

        indicadorEstado.setTexto(texto);
        indicadorEstado.dibujarEnUi(batch);
    }

    @Override
    public void detenerProceso() {
        procesando = false;
        if (ingredienteCocinando != null) {
            ingredienteCocinando.setCoccionListener(null);
            ingredienteCocinando = null;
        }
    }

    @Override
    public void onCambioEstado(EstadoCoccion nuevoEstado) {
        System.out.println("Estado de cocción cambió a: " + nuevoEstado.getNombre());

        switch (nuevoEstado) {
            case COCIDO:
                GestorAudio.getInstance().reproducirSonido("coccion_perfecta");
                break;
        }
    }

    @Override
    public void onIngredienteQuemado() {
        System.out.println("¡Ingrediente quemado!");
        procesando = false;
    }
}
