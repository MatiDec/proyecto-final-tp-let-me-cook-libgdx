package com.hebergames.letmecook.entregables.productos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.utiles.GestorTexturas;

public enum TipoProducto {
    HAMBURGUESA("Hamburguesa", CategoriaProducto.ALMUERZO),
    CAFE("Cafe", CategoriaProducto.BEBIDAS),
    GASEOSA("Gaseosa", CategoriaProducto.BEBIDAS);
    //por ahora todos tienen la misma textura :/

    private final String nombre;
    private final CategoriaProducto categoria;

    TipoProducto(String nombre, CategoriaProducto categoria) {
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public ProductoGenerico crear() {
        TextureRegion textura = GestorTexturas.getInstance().getTexturaProducto(nombre.toLowerCase());
        return new ProductoGenerico(nombre, textura, categoria);
    }

    public String getNombre() { return nombre; }
    public CategoriaProducto getCategoria() { return categoria; }

    public TextureRegion getTextura() {
        return GestorTexturas.getInstance().getTexturaProducto(nombre.toLowerCase());
    }

}
