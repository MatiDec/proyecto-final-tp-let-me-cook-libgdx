package com.hebergames.letmecook.pedidos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entidades.clientes.Cliente;
import com.hebergames.letmecook.utiles.Recursos;

public class TarjetaPedido {

    private TextureRegion texturaCliente;
    private TextureRegion texturaProducto;
    private Texto textoTiempo;
    private ShapeRenderer shapeRenderer;

    private static final float ANCHO_TARJETA = 200f;
    private static final float ALTO_TARJETA = 100f;
    private static final float TAMAÑO_IMAGEN = 64f;
    private static final float PADDING = 10f;

    public TarjetaPedido() {
        shapeRenderer = new ShapeRenderer();
        textoTiempo = new Texto(Recursos.FUENTE_MENU, 20, Color.WHITE, true);
    }

    public void dibujar(SpriteBatch batch, Cliente cliente, float x, float y,
                        TextureRegion texturaCliente, TextureRegion texturaProducto) {

        if (cliente.getPedido().getEstadoPedido() != EstadoPedido.EN_PREPARACION) {
            return;
        }

        // Terminar el batch para dibujar formas
        batch.end();

        // Dibujar fondo de la tarjeta
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Fondo semitransparente
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.8f);
        shapeRenderer.rect(x, y, ANCHO_TARJETA, ALTO_TARJETA);

        // Barra de progreso del tiempo
        float porcentaje = 1f - cliente.getPorcentajeTiempo();
        Color colorBarra = getColorPorcentaje(porcentaje);
        shapeRenderer.setColor(colorBarra);
        shapeRenderer.rect(x, y, ANCHO_TARJETA * porcentaje, 5f);

        shapeRenderer.end();

        // Reiniciar el batch para dibujar texturas
        batch.begin();

        // Dibujar imagen del cliente
        batch.draw(texturaCliente,
            x + PADDING,
            y + ALTO_TARJETA - TAMAÑO_IMAGEN - PADDING,
            TAMAÑO_IMAGEN, TAMAÑO_IMAGEN);

        // Dibujar imagen del producto
        batch.draw(texturaProducto,
            x + ANCHO_TARJETA - TAMAÑO_IMAGEN - PADDING,
            y + ALTO_TARJETA - TAMAÑO_IMAGEN - PADDING,
            TAMAÑO_IMAGEN, TAMAÑO_IMAGEN);

        // Dibujar tiempo restante
        int segundos = (int) cliente.getTiempoRestante();
        textoTiempo.setTexto(segundos + "s");
        textoTiempo.setPosition(
            x + (ANCHO_TARJETA / 2f) - (textoTiempo.getAncho() / 2f),
            y + (ALTO_TARJETA / 2f) + (textoTiempo.getAlto() / 2f)
        );
        textoTiempo.dibujarEnUi(batch);
    }

    private Color getColorPorcentaje(float porcentaje) {
        if (porcentaje > 0.6f) {
            return Color.GREEN;
        } else if (porcentaje > 0.3f) {
            return Color.YELLOW;
        } else {
            return Color.RED;
        }
    }

    public float getAlto() {
        return ALTO_TARJETA;
    }

    public void dispose() {
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
