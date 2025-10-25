package com.hebergames.letmecook.mapa.indicadores;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.sonido.GestorAudio;

import java.util.ArrayList;

public class GestorIndicadores {
    private ArrayList<IndicadorVisual> indicadores;
    private Rectangle areaVisibleCache;
    private boolean sonidoAlertaReproducido;
    private float tiempoUltimaAlerta;

    private static final float INTERVALO_SONIDO_ALERTA = 2f;

    public GestorIndicadores() {
        indicadores = new ArrayList<>();
        areaVisibleCache = new Rectangle();
        sonidoAlertaReproducido = false;
        tiempoUltimaAlerta = 0f;
    }

    public IndicadorVisual crearIndicador(float x, float y, IndicadorVisual.TipoIndicador tipo) {
        IndicadorVisual indicador = new IndicadorVisual(x, y, tipo);
        indicadores.add(indicador);
        return indicador;
    }

    public void registrarIndicador(IndicadorVisual indicador) {
        if (indicador != null && !indicadores.contains(indicador)) {
            indicadores.add(indicador);
        }
    }

    public void actualizar(float delta, OrthographicCamera camara) {
        calcularAreaVisible(camara);

        boolean hayAlertaActiva = false;
        tiempoUltimaAlerta += delta;

        for (IndicadorVisual indicador : indicadores) {
            indicador.actualizar(delta, camara, areaVisibleCache);

            if (indicador.isVisible() &&
                indicador.getEstado() == IndicadorVisual.EstadoIndicador.QUEMANDOSE &&
                indicador.isEnBorde()) {
                hayAlertaActiva = true;
            }
        }

        if (hayAlertaActiva && tiempoUltimaAlerta >= INTERVALO_SONIDO_ALERTA) {
            GestorAudio.getInstance().reproducirSonido("alerta_quemado");
            tiempoUltimaAlerta = 0f;
        }
    }

    private void calcularAreaVisible(OrthographicCamera camara) {
        float anchoVista = camara.viewportWidth * camara.zoom;
        float altoVista = camara.viewportHeight * camara.zoom;

        areaVisibleCache.set(
            camara.position.x - anchoVista / 2f,
            camara.position.y - altoVista / 2f,
            anchoVista,
            altoVista
        );
    }

    public void dibujar(SpriteBatch batch) {
        for (IndicadorVisual indicador : indicadores) {
            indicador.dibujar(batch);
        }
    }

    public void limpiar() {
        indicadores.clear();
    }
}
