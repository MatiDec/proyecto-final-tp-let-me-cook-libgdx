package com.hebergames.letmecook.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class MapaColisiones {
    private List<Rectangle> zonasColision;

    public MapaColisiones(String rutaImagen) {
        zonasColision = new ArrayList<>();
        cargarMapaDesdeImagen(rutaImagen);
    }

    private void cargarMapaDesdeImagen(String rutaImagen) {
        Pixmap pixmap = null; /*que es un pixmap: es basicamente un componente de libgdx que descompone la imagen en pixeles y
        te permite analizar los valores rgb de esos pixeles, cosa que nos va a ser muy util para que el codigo sepa donde poner colisiones y las maquinas
        */
        try { //este try catch era porque antes le pifie en el name del archivo y me entere re tarde
            pixmap = new Pixmap(Gdx.files.internal(rutaImagen));
        } catch (Exception e) {
            System.err.println("no se pudo cargar el mapa de colisiones: " + rutaImagen);
            e.printStackTrace();
            return;
        }

        int ancho = pixmap.getWidth();
        int alto = pixmap.getHeight();

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = pixmap.getPixel(x, y);
                Color color = new Color();
                Color.rgba8888ToColor(color, pixel);

                if (esVerde(color)) { //podemos poner esRojo esAzul etc para cada maquina tambien y en vez de poner una zonaColision ponemos una zonaMaquina
                    zonasColision.add(new Rectangle(x, (alto - y - 1), 1, 1));
                }
            }
        }

        pixmap.dispose();
    }

    private boolean esVerde(Color color) {
        return color.g > 0.8f && color.r < 0.2f && color.b < 0.2f;
    }

    public List<Rectangle> getZonasColision() {
        return zonasColision;
    }
}
