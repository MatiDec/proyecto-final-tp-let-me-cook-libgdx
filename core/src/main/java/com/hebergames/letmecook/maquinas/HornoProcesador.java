package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entregables.ingredientes.CoccionListener;
import com.hebergames.letmecook.entregables.ingredientes.EstadoCoccion;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.utiles.GestorAudio;
import com.hebergames.letmecook.utiles.Recursos;

public class HornoProcesador implements MaquinaProcesadora, CoccionListener {

    private Ingrediente ingredienteCocinando;
    private boolean procesando = false;
    private float tiempoSonidoTemporizador = 0f;
    private static final float INTERVALO_SONIDO_TEMPORIZADOR = 1f;

    private Texto indicadorEstado;
    private Rectangle area;

    public HornoProcesador(Rectangle area) {
        this.area = area;
        inicializarIndicador();
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
        if (!puedeIniciarProceso() || !ingrediente.esCocinableInterna() || ingrediente.estaQuemado()) {
            return false;
        }

        ingredienteCocinando = ingrediente;
        ingredienteCocinando.setCoccionListener(this);
        procesando = true;

        GestorAudio.getInstance().reproducirSonido("horno_inicio");
        System.out.println("Iniciando cocción de: " + ingrediente.getNombre());
        return true;
    }

    @Override
    public void actualizarProceso(float delta) {
        if (!procesando || ingredienteCocinando == null) return;

        ingredienteCocinando.actualizarCoccion(delta);

        // Sonido de temporizador
        tiempoSonidoTemporizador += delta;
        if (tiempoSonidoTemporizador >= INTERVALO_SONIDO_TEMPORIZADOR) {
            GestorAudio.getInstance().reproducirSonido("temporizador");
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
        // El jugador puede retirar el ingrediente en cualquier estado
        if (!tieneProcesandose()) return null;

        Ingrediente resultado = ingredienteCocinando;
        ingredienteCocinando.setCoccionListener(null); // Limpiar listener
        ingredienteCocinando = null;
        procesando = false;
        tiempoSonidoTemporizador = 0f;

        return resultado;
    }

    @Override
    public void dibujarIndicador(SpriteBatch batch) {//Esto es lo que debería aparecer visualmente, pero no sale
        if (procesando && ingredienteCocinando != null) {
            EstadoCoccion estado = ingredienteCocinando.getEstadoCoccion();

            // Mostrar estado actual y que se puede retirar
            String texto;
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
                texto = estado.getNombre() + " - Click para retirar";
            }

            indicadorEstado.setTexto(texto);
        } else {
            indicadorEstado.setTexto("Horno Libre");
        }
        indicadorEstado.dibujar();
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
