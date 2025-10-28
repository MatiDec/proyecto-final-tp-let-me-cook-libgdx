package com.hebergames.letmecook.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.estaciones.procesadoras.EstadoMaquina;

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
    private final Map<String, TextureRegion[]> TEXTURAS_MAQUINAS;

    private final Map<String, TextureRegion> TEXTURAS_PRODUCTOS;
    private boolean texturasListas = false;
    private TextureRegion texturaIngrediente;

    private GestorTexturas() {
        TEXTURAS_PRODUCTOS = new HashMap<>();
        TEXTURAS_MAQUINAS = new HashMap<>();
    }

    public static GestorTexturas getInstance() {
        if (instancia == null) {
            instancia = new GestorTexturas();
        }
        return instancia;
    }

    public void cargarTexturas() {
        if (texturasListas) return;

        // Cargar clientes
        texturaClientes = new Texture(Gdx.files.internal(Recursos.CLIENTES_SPRITESHEET));
        TextureRegion[][] tmpClientes = TextureRegion.split(texturaClientes,
            Recursos.SPRITE_CLIENTE_WIDTH, Recursos.SPRITE_CLIENTE_HEIGHT);
        texturaClientePresencial = tmpClientes[0][0];
        texturaClienteVirtual = tmpClientes[0][1];

        // Cargar caras
        texturaCaras = new Texture(Gdx.files.internal(Recursos.CARAS_SPRITESHEET));
        TextureRegion[][] tmpCaras = TextureRegion.split(texturaCaras,
            Recursos.SPRITE_ITEM_WIDTH, Recursos.SPRITE_ITEM_HEIGHT);
        caraFeliz = tmpCaras[0][0];
        caraImpaciente = tmpCaras[0][1];
        caraEnojada = tmpCaras[0][2];

        // Cargar productos
        try {
            Texture texturaProductos = new Texture(Gdx.files.internal(Recursos.PRODUCTOS_SPRITESHEET));
            TextureRegion[][] tmpProductos = TextureRegion.split(texturaProductos,
                Recursos.SPRITE_ITEM_WIDTH, Recursos.SPRITE_ITEM_HEIGHT);
            TEXTURAS_PRODUCTOS.put("hamburguesa", tmpProductos[0][0]);
        } catch (Exception e) {
            System.err.println("No se pudieron cargar las texturas de productos: " + e.getMessage());
        }

        // Cargar icono de error
        try {
            texturaError = new Texture(Gdx.files.internal(Recursos.ERROR_ICON));
            iconoError = new TextureRegion(texturaError);
        } catch (Exception e) {
            System.err.println("No se pudo cargar icono de error: " + e.getMessage());
        }

        // Cargar bebidas
        try {
            Texture texturaBebidas = new Texture(Gdx.files.internal(Recursos.BEBIDAS_SPRITESHEET));
            TextureRegion[][] tmpBebidas = TextureRegion.split(texturaBebidas,
                Recursos.SPRITE_ITEM_WIDTH, Recursos.SPRITE_ITEM_HEIGHT);

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

        // Cargar temporizadores
        try {
            Texture texturaTemporizadores = new Texture(Gdx.files.internal(Recursos.TEMPORIZADORES_SPRITESHEET));
            TextureRegion[][] tmpTemp = TextureRegion.split(texturaTemporizadores,
                Recursos.SPRITE_ITEM_WIDTH, Recursos.SPRITE_ITEM_HEIGHT);

            texturasTemporizador = new TextureRegion[3];
            texturasTemporizador[0] = tmpTemp[0][0];
            texturasTemporizador[1] = tmpTemp[0][1];
            texturasTemporizador[2] = tmpTemp[0][2];

            texturaCheck = tmpTemp[1][0];
            texturaAlerta = tmpTemp[1][1];

            Texture texturaFlechaArchivo = new Texture(Gdx.files.internal(Recursos.FLECHA));
            texturaFlecha = new TextureRegion(texturaFlechaArchivo);
        } catch (Exception e) {
            System.err.println("No se pudieron cargar texturas de indicadores: " + e.getMessage());
        }

        try {
            texturaPisoMojado = new Texture(Gdx.files.internal(Recursos.PISO_MOJADO));
            regionPisoMojado = new TextureRegion(texturaPisoMojado);
        } catch (Exception e) {
            System.err.println("No se pudo cargar textura de piso mojado: " + e.getMessage());
        }

        cargarTexturasMaquinas();

        texturasListas = true;
        System.out.println("Texturas cargadas correctamente");
    }

    private void cargarTexturasMaquinas() {
        try {
            Texture texturaMaquinas = new Texture(Gdx.files.internal(Recursos.MAQUINAS_SPRITESHEET));
            TextureRegion[][] tmpMaquinas = TextureRegion.split(texturaMaquinas,
                Recursos.MEDIDA_TILE, Recursos.MEDIDA_TILE);

            // Horno - Fila 0
            TextureRegion[] estadosHorno = new TextureRegion[3];
            estadosHorno[0] = tmpMaquinas[0][0]; // Inactiva
            estadosHorno[1] = tmpMaquinas[0][1]; // Procesando
            estadosHorno[2] = tmpMaquinas[0][2]; // Lista
            TEXTURAS_MAQUINAS.put("horno", estadosHorno);

            // Freidora - Fila 1
            TextureRegion[] estadosFreidora = new TextureRegion[3];
            estadosFreidora[0] = tmpMaquinas[1][0]; // Inactiva
            estadosFreidora[1] = tmpMaquinas[1][1]; // Procesando
            estadosFreidora[2] = tmpMaquinas[1][2]; // Lista
            TEXTURAS_MAQUINAS.put("freidora", estadosFreidora);

            // Tostadora - Fila 2
            TextureRegion[] estadosTostadora = new TextureRegion[3];
            estadosTostadora[0] = tmpMaquinas[2][0]; // Inactiva
            estadosTostadora[1] = tmpMaquinas[2][1]; // Procesando
            estadosTostadora[2] = tmpMaquinas[2][2]; // Lista
            TEXTURAS_MAQUINAS.put("tostadora", estadosTostadora);

            System.out.println("Texturas de máquinas cargadas: Horno, Freidora, Tostadora");

        } catch (Exception e) {
            System.err.println("No se pudieron cargar texturas de máquinas: " + e.getMessage());
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

    public TextureRegion getTexturaMaquina(String nombreMaquina, EstadoMaquina estado) {
        if (!texturasListas) {
            System.err.println("ADVERTENCIA: Texturas no cargadas aún");
            return null;
        }

        TextureRegion[] texturas = TEXTURAS_MAQUINAS.get(nombreMaquina);
        if (texturas == null) {
            System.err.println("ADVERTENCIA: No existe textura para máquina: " + nombreMaquina);
            return iconoError;
        }

        return texturas[estado.getIndice()];
    }

    public TextureRegion[] getTexturasMaquina(String nombreMaquina) {
        if (!texturasListas) {
            System.err.println("ADVERTENCIA: Texturas no cargadas aún");
            return null;
        }

        return TEXTURAS_MAQUINAS.get(nombreMaquina);
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
