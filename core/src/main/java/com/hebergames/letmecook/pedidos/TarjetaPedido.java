package com.hebergames.letmecook.pedidos;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entidades.clientes.Cliente;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.utiles.GestorTexturas;
import com.hebergames.letmecook.utiles.Recursos;

import java.util.ArrayList;

public class TarjetaPedido {

    private TextureRegion texturaCliente;
    private TextureRegion texturaProducto;
    private Texto textoTiempo;
    private ShapeRenderer shapeRenderer;

    private static final float ANCHO_TARJETA = 200f;
    private static final float ALTO_TARJETA = 100f;
    private static final float TAMANO_IMAGEN = 64f;
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

        // Obtener porcentaje de tolerancia (mismo que usa el cliente)
        float porcentajeTolerancia = cliente.getPorcentajeToleranciaActual();

        // Terminar el batch para dibujar formas
        batch.end();

        // Dibujar fondo de la tarjeta
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        // Fondo semitransparente
        shapeRenderer.setColor(0.2f, 0.2f, 0.2f, 0.8f);
        shapeRenderer.rect(x, y, ANCHO_TARJETA, ALTO_TARJETA);

        // Barra de progreso del tiempo (usa el mismo porcentaje de tolerancia)
        Color colorBarra = getColorPorcentaje(porcentajeTolerancia);
        shapeRenderer.setColor(colorBarra);
        shapeRenderer.rect(x, y, ANCHO_TARJETA * porcentajeTolerancia, 5f);

        shapeRenderer.end();

        // Reiniciar el batch para dibujar texturas
        batch.begin();

        // Dibujar imagen del cliente
        batch.draw(texturaCliente,
            x + PADDING,
            y + ALTO_TARJETA - TAMANO_IMAGEN - PADDING,
            TAMANO_IMAGEN, TAMANO_IMAGEN);

        // Dibujar cara de tolerancia (usa el mismo porcentaje)
        TextureRegion cara = GestorTexturas.getInstance().getCaraPorTolerancia(porcentajeTolerancia);
        if (cara != null) {
            batch.draw(cara,
                x + PADDING + (TAMANO_IMAGEN / 2f) - 12f,
                y + ALTO_TARJETA - PADDING - 12f,
                24f, 24f);
        }

        // Dibujar imagen del producto
        ArrayList<Producto> productos = cliente.getPedido().getProductosSolicitados();
        int cantidadAMostrar = Math.min(productos.size(), 3);

        for (int i = 0; i < cantidadAMostrar; i++) {
            TextureRegion texturaProductoActual = GestorTexturas.getInstance()
                .getTexturaProducto(productos.get(i).getNombre());

            batch.draw(texturaProductoActual,
                x + ANCHO_TARJETA - TAMANO_IMAGEN - PADDING,
                y + ALTO_TARJETA - TAMANO_IMAGEN - PADDING - (i * 30f),
                TAMANO_IMAGEN * 0.8f, TAMANO_IMAGEN * 0.8f);
        }

        // Dibujar tiempo restante
        int segundos = (int) cliente.getTiempoRestante();
        textoTiempo.setTexto(segundos + "s");
        textoTiempo.setPosition(
            x + (ANCHO_TARJETA / 2f) - (textoTiempo.getAncho() / 2f),
            y + (ALTO_TARJETA / 2f) + (textoTiempo.getAlto() / 2f)
        );
        textoTiempo.dibujarEnUi(batch);
    }

    /**
     * Define los umbrales de color para la barra de tolerancia.
     * IMPORTANTE: Estos umbrales deben coincidir con los de GestorTexturas.getCaraPorTolerancia()
     */
    private Color getColorPorcentaje(float porcentaje) {
        if (porcentaje > 0.6f) {
            return Color.GREEN;  // Cliente contento
        } else if (porcentaje > 0.3f) {
            return Color.YELLOW; // Cliente impaciente
        } else {
            return Color.RED;    // Cliente enojado
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
