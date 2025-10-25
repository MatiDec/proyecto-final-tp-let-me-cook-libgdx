package com.hebergames.letmecook.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.HashMap;

public class GestorFuentes {
    private static final HashMap<String, BitmapFont> fuentes = new HashMap<>();

    public static BitmapFont obtenerFuente(String ruta, int tamaño, Color color, boolean sombra) {
        String clave = ruta + "_" + tamaño + "_" + color.toString() + "_" + sombra;
        if (!fuentes.containsKey(clave)) {
            FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal(ruta));
            FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
            param.size = tamaño;
            param.color = color;
            if (sombra) {
                param.shadowColor = Color.BLACK;
                param.shadowOffsetX = 1;
                param.shadowOffsetY = 1;
            }
            BitmapFont font = gen.generateFont(param);
            gen.dispose();
            fuentes.put(clave, font);
        }
        return fuentes.get(clave);
    }

    public static void dispose() {
        for (BitmapFont font : fuentes.values()) font.dispose();
        fuentes.clear();
    }
}
