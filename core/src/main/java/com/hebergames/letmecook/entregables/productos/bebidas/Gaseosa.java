package com.hebergames.letmecook.entregables.productos.bebidas;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.entregables.productos.CategoriaProducto;
import com.hebergames.letmecook.utiles.GestorTexturas;

import java.util.HashMap;
import java.util.Map;

public class Gaseosa extends Bebida {

    private static final Map<String, Float> TIPOS_GASEOSA = new HashMap<>();

    static {
        TIPOS_GASEOSA.put("Jugo", 2f);
        TIPOS_GASEOSA.put("Soda", 2f);
        TIPOS_GASEOSA.put("Sprite", 2f);
        TIPOS_GASEOSA.put("Pepsi", 2f);
        TIPOS_GASEOSA.put("CocaCola", 2f);
    }

    private String tipo;

    public Gaseosa(String tipo, TamanoBebida tamano) {
        super(tipo + " " + tamano.getNombre(),
            GestorTexturas.getInstance().getTexturaProducto((tipo + tamano.getNombre()).toLowerCase().replace(" ", "")),
            CategoriaProducto.BEBIDAS,
            tamano,
            TIPOS_GASEOSA.getOrDefault(tipo, 2f));
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public static Map<String, Float> getTiposGaseosa() {
        return new HashMap<>(TIPOS_GASEOSA);
    }
}
