package com.hebergames.letmecook.utiles;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Recursos {
    public static final String FONDO = "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/pruebadeimagen.png";
    public static final String FUENTE_MENU = "core/src/main/java/com/hebergames/letmecook/recursos/fuentes/Chewy-Regular.ttf";
    public static final Texture PIXEL = new Texture("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/pixel.png");
    private static final Texture TEXTURE = new Texture("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/ingredientes.png");
    public static final TextureRegion INGREDIENTES = new TextureRegion(TEXTURE);
    public static final int MEDIDA_TILE = 128;
    public static final float ESPACIADO = 10f;
    public static final float ANCHO_DIA = 120f;
    public static final float ALTO_DIA = 240f;
}
