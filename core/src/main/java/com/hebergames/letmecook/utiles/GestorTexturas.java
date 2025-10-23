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
    private Texture texturaCaras;
    private TextureRegion caraFeliz;
    private TextureRegion caraImpaciente;
    private TextureRegion caraEnojada;
    private Texture texturaPisoMojado;
    private TextureRegion regionPisoMojado;

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

    public void cargarTexturas() {
        if (texturasListas) return;

        // Cargar texturas de clientes
        texturaClientes = new Texture(Gdx.files.internal(
            "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/clientes.jpg"));
        TextureRegion[][] tmpClientes = TextureRegion.split(texturaClientes, 32, 32);
        texturaClientePresencial = tmpClientes[0][0];
        texturaClienteVirtual = tmpClientes[0][1];

        texturaCaras = new Texture(Gdx.files.internal(
            "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/caras.jpg"));
        TextureRegion[][] tmpCaras = TextureRegion.split(texturaCaras, 32, 32);
        caraFeliz = tmpCaras[0][0];
        caraImpaciente = tmpCaras[0][1];
        caraEnojada = tmpCaras[0][2];

        // Cargar texturas de productos
        try {
            Texture texturaProductos = new Texture(Gdx.files.internal(
                "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/productos.png"));
            TextureRegion[][] tmpProductos = TextureRegion.split(texturaProductos, 32, 32);

            // Registrar productos con nombre y región correspondiente
            texturasProductos.put("hamburguesa", tmpProductos[0][0]);
            texturasProductos.put("gaseosa", tmpProductos[0][1]);
            texturasProductos.put("cafe", tmpProductos[0][2]);

        } catch (Exception e) {
            System.err.println("No se pudieron cargar las texturas de productos: " + e.getMessage());
        }

        //carga texturas del piso mojado y la region
        try {
            texturaPisoMojado = new Texture(Gdx.files.internal(
                "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/piso_mojado.png"));
            regionPisoMojado = new TextureRegion(texturaPisoMojado);
        } catch (Exception e) {
            System.err.println("No se pudo cargar textura de piso mojado: " + e.getMessage());
        }



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

    public TextureRegion getTexturaPisoMojado() {
        if (!texturasListas) {
            System.err.println("ADVERTENCIA: Texturas no cargadas aún");
            return null;
        }
        return regionPisoMojado;
    }

    public void registrarTexturaProducto(String nombreProducto, TextureRegion textura) {
        texturasProductos.put(nombreProducto, textura);
    }

    public boolean estanTexturasListas() {
        return texturasListas;
    }

    public TextureRegion getCaraPorTolerancia(float porcentajeTolerancia) {
        if (porcentajeTolerancia > 0.6f) {
            return caraFeliz;    // Verde
        } else if (porcentajeTolerancia > 0.3f) {
            return caraImpaciente; // Amarillo
        } else {
            return caraEnojada;   // Rojo
        }
    }

    public void dispose() {
        if (texturaClientes != null) {
            texturaClientes.dispose();
        }
        if (texturaPisoMojado != null) {
            texturaPisoMojado.dispose();
        }
        if (texturaCaras != null) {
            texturaCaras.dispose();
        }
        texturasListas = false;
    }
}
