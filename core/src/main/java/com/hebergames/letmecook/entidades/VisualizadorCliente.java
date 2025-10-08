package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;
import com.hebergames.letmecook.utiles.Recursos;

public class VisualizadorCliente {

    private TextureRegion texturaCliente;
    private static final float OFFSET_Y = Recursos.MEDIDA_TILE; // Una tile arriba

    public VisualizadorCliente(TextureRegion textura) {
        this.texturaCliente = textura;
    }

    public void dibujar(SpriteBatch batch, Cliente cliente) {
        EstacionTrabajo estacion = cliente.getEstacionAsignada();
        if (estacion == null) return;

        Rectangle area = estacion.area;
        float x = area.x + (area.width / 2f) - (Recursos.MEDIDA_TILE / 2f);
        float y = area.y + area.height + OFFSET_Y;

        batch.draw(texturaCliente, x, y, Recursos.MEDIDA_TILE, Recursos.MEDIDA_TILE);
    }
}
