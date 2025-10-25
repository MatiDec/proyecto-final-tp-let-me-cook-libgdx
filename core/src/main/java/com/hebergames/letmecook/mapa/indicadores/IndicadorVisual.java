package com.hebergames.letmecook.mapa.indicadores;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hebergames.letmecook.utiles.GestorTexturas;

public class IndicadorVisual {
    private TextureRegion texturaActual;
    private Vector2 posicionMundo;
    private Vector2 posicionPantalla;
    private boolean visible;
    private boolean enBorde;
    private TipoIndicador tipo;
    private float tiempoAnimacion;
    private float anguloFlecha;
    private EstadoIndicador estado;

    private static final float OFFSET_Y = 40f;
    private static final float TAMANO = 32f;

    public enum TipoIndicador {
        TEMPORIZADOR,
        ALERTA,
        CLIENTE_NUEVO
    }

    public enum EstadoIndicador {
        PROCESANDO,
        LISTO,
        QUEMANDOSE,
        INACTIVO
    }

    public IndicadorVisual(float x, float y, TipoIndicador tipo) {
        this.posicionMundo = new Vector2(x, y + OFFSET_Y);
        this.posicionPantalla = new Vector2();
        this.tipo = tipo;
        this.visible = false;
        this.enBorde = false;
        this.tiempoAnimacion = 0f;
        this.estado = EstadoIndicador.INACTIVO;
    }

    public void actualizar(float delta, OrthographicCamera camara, Rectangle areaVisible) {
        tiempoAnimacion += delta;

        // Actualizar textura continuamente si está procesando
        if (estado == EstadoIndicador.PROCESANDO) {
            actualizarTextura();
        }

        if (!estaEnVista(camara, areaVisible)) {
            enBorde = true;
            calcularPosicionBorde(camara);
        } else {
            enBorde = false;
            posicionPantalla.set(posicionMundo);
        }
    }

    private boolean estaEnVista(OrthographicCamera camara, Rectangle areaVisible) {
        return areaVisible.contains(posicionMundo.x, posicionMundo.y);
    }


    private void calcularPosicionBorde(OrthographicCamera camara) {
        float camaraX = camara.position.x;
        float camaraY = camara.position.y;
        float anchoVista = camara.viewportWidth * camara.zoom;
        float altoVista = camara.viewportHeight * camara.zoom;

        float margen = 50f;

        float minX = camaraX - anchoVista / 2f + margen;
        float maxX = camaraX + anchoVista / 2f - margen;
        float minY = camaraY - altoVista / 2f + margen;
        float maxY = camaraY + altoVista / 2f - margen;

        Vector2 direccion = new Vector2(posicionMundo).sub(camaraX, camaraY).nor();

        float t1 = (maxX - camaraX) / direccion.x;
        float t2 = (minX - camaraX) / direccion.x;
        float t3 = (maxY - camaraY) / direccion.y;
        float t4 = (minY - camaraY) / direccion.y;

        float t = Float.MAX_VALUE;

        if (direccion.x > 0 && t1 > 0) t = Math.min(t, t1);
        if (direccion.x < 0 && t2 > 0) t = Math.min(t, t2);
        if (direccion.y > 0 && t3 > 0) t = Math.min(t, t3);
        if (direccion.y < 0 && t4 > 0) t = Math.min(t, t4);

        posicionPantalla.set(camaraX + direccion.x * t, camaraY + direccion.y * t);

        anguloFlecha = (float) Math.toDegrees(Math.atan2(direccion.y, direccion.x));
    }

    public void dibujar(SpriteBatch batch) {
        if (!visible || texturaActual == null) return;

        float x = posicionPantalla.x - TAMANO / 2f;
        float y = posicionPantalla.y - TAMANO / 2f;

        if (enBorde) {
            // Dibujar flecha direccional
            GestorTexturas gestor = GestorTexturas.getInstance();
            TextureRegion flecha = gestor.getTexturaFlecha();

            if (flecha != null) {
                float rad = (float) Math.toRadians(anguloFlecha);
                float distancia = TAMANO * 1.2f; // separación desde el indicador

                float flechaX = posicionPantalla.x + (float) Math.cos(rad) * distancia;
                float flechaY = posicionPantalla.y + (float) Math.sin(rad) * distancia;

                batch.draw(flecha,
                    flechaX - TAMANO / 2f,
                    flechaY - TAMANO / 2f,
                    TAMANO / 2f,
                    TAMANO / 2f,
                    TAMANO,
                    TAMANO,
                    1f,
                    1f,
                    anguloFlecha);
            }
        }

        batch.draw(texturaActual, x, y, TAMANO, TAMANO);
        batch.setColor(1f, 1f, 1f, 1f);
    }

    public void setTextura(TextureRegion textura) {
        this.texturaActual = textura;
    }

    public void setEstado(EstadoIndicador estado) {
        this.estado = estado;
        actualizarTextura();
    }

    private void actualizarTextura() {
        GestorTexturas gestor = GestorTexturas.getInstance();

        switch (estado) {
            case PROCESANDO:
                int frame = (int)(tiempoAnimacion / 2f) % 3;
                texturaActual = gestor.getTexturaTemporizador(frame);
                break;
            case LISTO:
                texturaActual = gestor.getTexturaCheck();
                break;
            case QUEMANDOSE:
                texturaActual = gestor.getTexturaAlerta();
                break;
            case INACTIVO:
                texturaActual = null;
                visible = false;
                break;
        }
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public void setPosicionMundo(float x, float y) {
        this.posicionMundo.set(x, y + OFFSET_Y);
    }

    public boolean isVisible() {
        return visible;
    }

    public EstadoIndicador getEstado() {
        return estado;
    }

    public boolean isEnBorde() {
        return enBorde;
    }
}
