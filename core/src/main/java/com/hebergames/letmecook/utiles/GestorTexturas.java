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
    private TextureRegion[] texturasTemporizador;
    private TextureRegion texturaCheck;
    private TextureRegion texturaAlerta;
    private TextureRegion texturaFlecha;

    private final Map<String, TextureRegion> TEXTURAS_PRODUCTOS;
    private boolean texturasListas = false;
    private TextureRegion texturaIngrediente;

    private GestorTexturas() {
        TEXTURAS_PRODUCTOS = new HashMap<>();
    }

    public static GestorTexturas getInstance() {
        if (instancia == null) {
            instancia = new GestorTexturas();
        }
        return instancia;
    }

    public void cargarTexturas() {
        if (texturasListas) return;

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

        try {
            Texture texturaProductos = new Texture(Gdx.files.internal(
                "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/productos.png"));
            TextureRegion[][] tmpProductos = TextureRegion.split(texturaProductos, 32, 32);

            TEXTURAS_PRODUCTOS.put("hamburguesa", tmpProductos[0][0]);

        } catch (Exception e) {
            System.err.println("No se pudieron cargar las texturas de productos: " + e.getMessage());
        }

        try {
            texturaError = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/error_icon.png"));
            iconoError = new TextureRegion(texturaError);
        } catch (Exception e) {
            System.err.println("No se pudo cargar icono de error: " + e.getMessage());
        }

        try {
            Texture texturaBebidas = new Texture(Gdx.files.internal(
                "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/bebidas.png"));
            TextureRegion[][] tmpBebidas = TextureRegion.split(texturaBebidas, 32, 32);


            String[] tiposBebidas = {"expreso", "americano", "cortado", "jugo", "soda", "sprite", "pepsi", "cocacola"};
            String[] tamanos = {"pequeno", "mediano", "grande"};

            for (int fila = 0; fila < tamanos.length; fila++) {
                for (int col = 0; col < tiposBebidas.length && col < tmpBebidas[fila].length; col++) {
                    String nombreTex = tiposBebidas[col] + tamanos[fila];
                    TEXTURAS_PRODUCTOS.put(nombreTex, tmpBebidas[fila][col]);
                }
            }
        } catch (Exception e) {
            System.err.println("No se pudieron cargar las texturas de bebidas: " + e.getMessage());
        }

        try {
            Texture texturaTemporizadores = new Texture(Gdx.files.internal(
                "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/temporizadores.png"));
            TextureRegion[][] tmpTemp = TextureRegion.split(texturaTemporizadores, 32, 32);

            texturasTemporizador = new TextureRegion[3];
            texturasTemporizador[0] = tmpTemp[0][0];
            texturasTemporizador[1] = tmpTemp[0][1];
            texturasTemporizador[2] = tmpTemp[0][2];

            texturaCheck = tmpTemp[1][0];
            texturaAlerta = tmpTemp[1][1];
            Texture texturaFlechaArchivo = new Texture(Gdx.files.internal(
                "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/texturaFlecha.png"));
            texturaFlecha = new TextureRegion(texturaFlechaArchivo);

        } catch (Exception e) {
            System.err.println("No se pudieron cargar texturas de indicadores: " + e.getMessage());
        }

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

    public TextureRegion getTexturaTemporizador(int frame) {
        if (texturasTemporizador != null && frame >= 0 && frame < texturasTemporizador.length) {
            return texturasTemporizador[frame];
        }
        return null;
    }

    public TextureRegion getTexturaCheck() {
        return this.texturaCheck;
    }

    public TextureRegion getTexturaAlerta() {
        return this.texturaAlerta;
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

        TextureRegion textura = TEXTURAS_PRODUCTOS.get(nombreProducto);
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

    public TextureRegion getTexturaFlecha() {
        return texturaFlecha != null ? texturaFlecha : iconoError;
    }

    public boolean estanTexturasListas() {
        return texturasListas;
    }

    public TextureRegion getCaraPorTolerancia(float porcentajeTolerancia) {
        if (porcentajeTolerancia > 0.6f) {
            return caraFeliz;
        } else if (porcentajeTolerancia > 0.3f) {
            return caraImpaciente;
        } else {
            return caraEnojada;
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
