package com.hebergames.letmecook.entregables.productos;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.utiles.GestorTexturas;

public enum TipoProducto {
    HAMBURGUESA_CARNE("Hamburguesa de Carne", CategoriaProducto.ALMUERZO),
    HAMBURGUESA_POLLO("Hamburguesa de Pollo", CategoriaProducto.ALMUERZO),
    MILANESA_CARNE("Milanesa de Carne", CategoriaProducto.ALMUERZO),
    MILANESA_POLLO("Milanesa de Pollo", CategoriaProducto.ALMUERZO),
    PAPAS_FRITAS("Papas Fritas", CategoriaProducto.ALMUERZO),
    NUGGETS_POLLO("Nuggets de pollo", CategoriaProducto.ALMUERZO),
    RABAS("Rabas", CategoriaProducto.ALMUERZO),
    AROS_CEBOLLA("Aros de Cebolla", CategoriaProducto.ALMUERZO),
    CAFE("Cafe", CategoriaProducto.BEBIDAS),
    GASEOSA("Gaseosa", CategoriaProducto.BEBIDAS);

    private final String NOMBRE;
    private final CategoriaProducto CATEGORIA;

    TipoProducto(final String NOMBRE, CategoriaProducto CATEGORIA) {
        this.NOMBRE = NOMBRE;
        this.CATEGORIA = CATEGORIA;
    }

    public ProductoGenerico crear() {
        TextureRegion textura = GestorTexturas.getInstance().getTexturaProducto(NOMBRE.toLowerCase());
        return new ProductoGenerico(NOMBRE, textura, CATEGORIA);
    }

    public String getNombre() { return this.NOMBRE; }
    public CategoriaProducto getCategoria() { return this.CATEGORIA; }

    public TextureRegion getTextura() {
        return GestorTexturas.getInstance().getTexturaProducto(NOMBRE.toLowerCase());
    }

}
