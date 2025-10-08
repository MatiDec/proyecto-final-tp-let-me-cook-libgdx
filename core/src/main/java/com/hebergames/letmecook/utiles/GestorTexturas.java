package com.hebergames.letmecook.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.Map;

public class GestorTexturas {

    private static GestorTexturas instancia;

    private Texture texturaClientes;
    private TextureRegion texturaClientePresencial;
    private TextureRegion texturaClienteVirtual;

    private Map<String, TextureRegion> texturasProductos;
    private boolean texturasListas = false;

    private GestorTexturas() {
        texturasProductos = new HashMap<>();
    }

    public static GestorTexturas getInstance() {
        if (instancia == null) {
            instancia = new GestorTexturas();
        }
        return instancia;
    }

    // Este método DEBE llamarse desde el hilo principal (render thread)
    public void cargarTexturas() {
        if (texturasListas) return;

        // Cargar texturas de clientes
        texturaClientes = new Texture(Gdx.files.internal(
            "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/clientes.jpg"));
        TextureRegion[][] tmpClientes = TextureRegion.split(texturaClientes, 32, 32);
        texturaClientePresencial = tmpClientes[0][0];
        texturaClienteVirtual = tmpClientes[0][1];

        texturasListas = true;
        System.out.println("Texturas cargadas correctamente");
    }

    public TextureRegion getTexturaCliente() {
        if (!texturasListas) {
            System.err.println("ADVERTENCIA: Texturas no cargadas aún");
            return null;
        }
        return texturaClientePresencial;
    }

    public TextureRegion getTexturaProducto(String nombreProducto) {
        if (!texturasListas) {
            System.err.println("ADVERTENCIA: Texturas no cargadas aún");
            return null;
        }

        TextureRegion textura = texturasProductos.get(nombreProducto);
        if (textura == null) {
            return texturaClientePresencial;
        }
        return textura;
    }

    public void registrarTexturaProducto(String nombreProducto, TextureRegion textura) {
        texturasProductos.put(nombreProducto, textura);
    }

    public boolean estanTexturasListas() {
        return texturasListas;
    }

    public void dispose() {
        if (texturaClientes != null) {
            texturaClientes.dispose();
        }
        texturasListas = false;
    }
}
