package com.hebergames.letmecook.entregables.recetas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.entregables.productos.Hamburguesa;

import java.util.ArrayList;

public class RecetaHamburguesa extends Receta {
    private TextureRegion texturaHamburguesa;

    public RecetaHamburguesa() {
        super("Hamburguesa", crearListaIngredientes());
        cargarTextura();
    }

    private static ArrayList<String> crearListaIngredientes() {
        ArrayList<String> ingredientes = new ArrayList<>();
        ingredientes.add("Carne (Cocido)");
        ingredientes.add("Pan");
        return ingredientes;
    }

    private void cargarTextura() {
        Texture productosTextura = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/productos.png"));
        TextureRegion[][] tmp = TextureRegion.split(productosTextura, 32, 32);
        texturaHamburguesa = tmp[0][0]; // Primera textura para hamburguesa
    }

    @Override
    public Producto preparar() {
        return new Hamburguesa(texturaHamburguesa);
    }
}
