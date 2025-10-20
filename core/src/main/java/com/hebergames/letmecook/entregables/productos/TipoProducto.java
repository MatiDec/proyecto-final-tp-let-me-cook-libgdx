package com.hebergames.letmecook.entregables.productos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// Nuevo archivo: TipoProducto.java
public enum TipoProducto {
    HAMBURGUESA("Hamburguesa", CategoriaProducto.ALMUERZO, 0, 0);

    private static TextureRegion[][] regionesProductos;
    private static boolean texturasCache = false;

    private final String nombre;
    private final CategoriaProducto categoria;
    private final int filaTextura;
    private final int columnaTextura;

    TipoProducto(String nombre, CategoriaProducto categoria, int filaTextura, int columnaTextura) {
        this.nombre = nombre;
        this.categoria = categoria;
        this.filaTextura = filaTextura;
        this.columnaTextura = columnaTextura;
    }

    public ProductoGenerico crear() {
        TextureRegion textura = cargarTextura();
        return new ProductoGenerico(nombre, textura, categoria);
    }

    private TextureRegion cargarTextura() {
        if (!texturasCache) {
            Texture productosTextura = new Texture(Gdx.files.internal(
                "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/productos.png"));
            regionesProductos = TextureRegion.split(productosTextura, 32, 32);
            texturasCache = true;
        }
        return regionesProductos[filaTextura][columnaTextura];
    }

    public String getNombre() { return nombre; }
    public CategoriaProducto getCategoria() { return categoria; }

    public static void disposeTexturas() {
        if (texturasCache && regionesProductos != null && regionesProductos.length > 0) {
            regionesProductos[0][0].getTexture().dispose();
            texturasCache = false;
        }
    }
}
