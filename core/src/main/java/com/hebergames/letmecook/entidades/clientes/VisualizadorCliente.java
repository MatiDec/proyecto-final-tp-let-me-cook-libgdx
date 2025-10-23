package com.hebergames.letmecook.entidades.clientes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;
import com.hebergames.letmecook.utiles.GestorTexturas;
import com.hebergames.letmecook.utiles.Recursos;

public class VisualizadorCliente {

    private final TextureRegion TEXTURA_CLIENTE;

    public VisualizadorCliente(TextureRegion textura) {
        this.TEXTURA_CLIENTE = textura;
    }

    public void dibujar(SpriteBatch batch, Cliente cliente) {
        EstacionTrabajo estacion = cliente.getEstacionAsignada();
        if (estacion == null || cliente.esVirtual()) return;

        Rectangle area = estacion.area;
        float x = area.x + (area.width / 2f) - (Recursos.MEDIDA_TILE / 2f);
        float y = area.y + area.height;

        // Dibujar cliente
        batch.draw(TEXTURA_CLIENTE, x, y, Recursos.MEDIDA_TILE, Recursos.MEDIDA_TILE);

        // Dibujar indicador de tolerancia encima del cliente
        float porcentajeTolerancia = cliente.getPorcentajeToleranciaActual();
        TextureRegion cara = GestorTexturas.getInstance().getCaraPorTolerancia(porcentajeTolerancia);
        if (cara != null) {
            float xCara = x + (Recursos.MEDIDA_TILE / 2f) - 12f; // Ajustado para tamaño 24x24
            float yCara = y + Recursos.MEDIDA_TILE + 4f;
            batch.draw(cara, xCara, yCara, 24f, 24f); // Aumentado de 16x16 a 24x24
        }
    }
}
