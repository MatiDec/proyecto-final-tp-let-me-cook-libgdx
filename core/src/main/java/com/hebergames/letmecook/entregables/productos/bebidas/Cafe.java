package com.hebergames.letmecook.entregables.productos.bebidas;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.entregables.productos.CategoriaProducto;

import java.util.HashMap;
import java.util.Map;

public class Cafe extends Bebida {

    private static final Map<String, Float> TIPOS_CAFE = new HashMap<>();

    static {
        TIPOS_CAFE.put("Expreso", 3f);
        TIPOS_CAFE.put("Americano", 4f);
        TIPOS_CAFE.put("Cortado", 5f);
        TIPOS_CAFE.put("Doble", 6f);
    }

    private String tipo;

    public Cafe(String tipo, TamanoBebida tamano, TextureRegion textura) {
        super(tipo + " " + tamano.getNombre(),
            textura,
            CategoriaProducto.BEBIDAS,
            tamano,
            TIPOS_CAFE.getOrDefault(tipo, 3f));
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public static Map<String, Float> getTiposCafe() {
        return new HashMap<>(TIPOS_CAFE);
    }
}
