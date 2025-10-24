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
    private Texture texturaError;
    private TextureRegion iconoError;

    private Map<String, TextureRegion> texturasProductos;
    private boolean texturasListas = false;
    private TextureRegion texturaIngrediente;

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

        } catch (Exception e) {
            System.err.println("No se pudieron cargar las texturas de productos: " + e.getMessage());
        }

        try {
            texturaError = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/error_icon.png"));
            iconoError = new TextureRegion(texturaError);
        } catch (Exception e) {
            System.err.println("No se pudo cargar icono de error: " + e.getMessage());
        }

        // AÑADIR: Cargar texturas específicas de bebidas
        try {
            Texture texturaBebidas = new Texture(Gdx.files.internal(
                "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/bebidas.png"));
            TextureRegion[][] tmpBebidas = TextureRegion.split(texturaBebidas, 32, 32);

            texturasProductos.put("expresopequeno", tmpBebidas[0][0]);
            texturasProductos.put("americanopequeno", tmpBebidas[0][1]);
            texturasProductos.put("cortadopequeno", tmpBebidas[0][2]);

            texturasProductos.put("expresomediano", tmpBebidas[1][0]);
            texturasProductos.put("americanomediano", tmpBebidas[1][1]);
            texturasProductos.put("cortadomediano", tmpBebidas[1][2]);

            texturasProductos.put("expresogrande", tmpBebidas[2][0]);
            texturasProductos.put("americanogrande", tmpBebidas[2][1]);
            texturasProductos.put("cortadogrande", tmpBebidas[2][2]);

            texturasProductos.put("jugopequeno", tmpBebidas[3][0]);
            texturasProductos.put("sodapequeno", tmpBebidas[3][1]);
            texturasProductos.put("spritepequeno", tmpBebidas[3][2]);
            texturasProductos.put("pepsipequeno", tmpBebidas[3][3]);
            texturasProductos.put("cocacolapequeno", tmpBebidas[3][4]);

            texturasProductos.put("jugomediano", tmpBebidas[4][0]);
            texturasProductos.put("sodamediano", tmpBebidas[4][1]);
            texturasProductos.put("spritemediano", tmpBebidas[4][2]);
            texturasProductos.put("pepsimediano", tmpBebidas[4][3]);
            texturasProductos.put("cocacolamediano", tmpBebidas[4][4]);

            texturasProductos.put("jugogrande", tmpBebidas[5][0]);
            texturasProductos.put("sodagrande", tmpBebidas[5][1]);
            texturasProductos.put("spritegrande", tmpBebidas[5][2]);
            texturasProductos.put("pepsigrande", tmpBebidas[5][3]);
            texturasProductos.put("cocacolagrande", tmpBebidas[5][4]);

            System.out.println(texturasProductos.keySet());

        } catch (Exception e) {
            System.err.println("No se pudieron cargar las texturas de bebidas: " + e.getMessage());
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

    public TextureRegion getTexturaVirtualInactiva() {
        return this.texturaClienteVirtual;//esto como no existe todavia le mando lo mismo en ambos getters
    }

    public TextureRegion getTexturaVirtualActiva() {
        return this.texturaClienteVirtual;
    }

    public Texture getTexturaError() {
        return this.texturaError;
    }

    public TextureRegion getTexturaIngrediente() { return this.texturaIngrediente; }
}
