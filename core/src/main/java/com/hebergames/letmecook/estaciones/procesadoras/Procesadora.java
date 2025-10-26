package com.hebergames.letmecook.estaciones.procesadoras;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entregables.ingredientes.CoccionListener;
import com.hebergames.letmecook.entregables.ingredientes.EstadoCoccion;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.mapa.indicadores.EstadoIndicador;
import com.hebergames.letmecook.mapa.indicadores.IndicadorVisual;
import com.hebergames.letmecook.sonido.GestorAudio;
import com.hebergames.letmecook.utiles.Recursos;

import java.util.Objects;

public class Procesadora implements MaquinaProcesadora, CoccionListener {

    private Ingrediente ingredienteCocinando;
    private boolean procesando = false;
    private float tiempoSonidoTemporizador = 0f;
    private static final float INTERVALO_SONIDO_TEMPORIZADOR = 1f;

    private final Rectangle AREA;

    private final IndicadorVisual INDICADOR;

    public Procesadora(Rectangle area) {
        this.AREA = area;
        inicializarIndicador();
        this.INDICADOR = new IndicadorVisual(
            area.x + area.width / 2f,
            area.y + area.height
        );
    }

    private void inicializarIndicador() {
        Texto indicadorEstado = new Texto(Recursos.FUENTE_MENU, 16, Color.WHITE, true);
        float centroX = AREA.x + AREA.width / 2f;
        float arribaY = AREA.y + AREA.height + 10f;
        indicadorEstado.setPosition(centroX, arribaY);
    }

    @Override
    public boolean puedeIniciarProceso() {
        return !procesando;
    }

    @Override
    public boolean iniciarProceso(Ingrediente ingrediente) {

        if (!puedeIniciarProceso() || !ingrediente.esCocinableInterna()
            || ingrediente.estaQuemado()) {
            return false;
        }

        ingredienteCocinando = ingrediente;
        ingredienteCocinando.setCoccionListener(this);
        procesando = true;

        return true;
    }

    @Override
    public void actualizarProceso(float delta) {
        if (!procesando || ingredienteCocinando == null) {
            if (INDICADOR != null) {
                INDICADOR.setVisible(false);
            }
            return;
        }

        ingredienteCocinando.actualizarCoccion(delta);

        if (INDICADOR != null) {
            INDICADOR.setVisible(true);

            if (ingredienteCocinando.estaQuemado()) {
                INDICADOR.setEstado(EstadoIndicador.QUEMANDOSE);
            } else if (ingredienteCocinando.getEstadoCoccion() == EstadoCoccion.BIEN_HECHO) {
                INDICADOR.setEstado(EstadoIndicador.LISTO);
            } else {
                INDICADOR.setEstado(EstadoIndicador.PROCESANDO);
            }
        }

        tiempoSonidoTemporizador += delta;
        if (tiempoSonidoTemporizador >= INTERVALO_SONIDO_TEMPORIZADOR) {
            tiempoSonidoTemporizador = 0f;
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
        ingredienteCocinando.setCoccionListener(null);
        ingredienteCocinando = null;
        procesando = false;
        tiempoSonidoTemporizador = 0f;

        return resultado;
    }

    @Override
    public void onCambioEstado(EstadoCoccion nuevoEstado) {
        if (Objects.requireNonNull(nuevoEstado) == EstadoCoccion.BIEN_HECHO) {
            GestorAudio.getInstance().reproducirSonido("coccion_perfecta");
        }
    }

    @Override
    public void onIngredienteQuemado() {
        procesando = false;
    }

    public IndicadorVisual getIndicador() {
        return this.INDICADOR;
    }
}
