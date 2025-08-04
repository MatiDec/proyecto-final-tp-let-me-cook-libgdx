package com.hebergames.letmecook.pantallas.pantallasmaquinas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entregables.ingredientes.Carne;
import com.hebergames.letmecook.entregables.ingredientes.Pan;
import com.hebergames.letmecook.eventos.TextoInteractuable;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.pantallas.PantallaJuego;
import com.hebergames.letmecook.utiles.Recursos;

public class PantallaHeladera extends PantallaMaquina {

    private Texto tCarne, tPan, tCerrar, tInventario;

    // Texturas de ingredientes (cargarlas desde recursos)
    private TextureRegion texturaCarne;
    private TextureRegion texturaPan;

    public PantallaHeladera() {
        super(true); // true porque tiene overlay visual
        cargarTexturas();
    }

    private void cargarTexturas() {
        Texture ingredientesTextura = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/ingredientes.png"));
        TextureRegion[][] tmp = TextureRegion.split(ingredientesTextura, 32, 32);
        texturaCarne = tmp[0][0];
        texturaPan = tmp[0][1];
    }

    @Override
    protected void ejecutarLogicaMaquina() {
        System.out.println("Heladera abierta");
    }

    @Override
    protected void actualizarLogicaMaquina(float delta) {
        // La heladera no necesita actualización continua
    }

    @Override
    protected void inicializarInterfaz() {
        tCarne = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE, true);
        tCarne.setTexto("Carne");

        tPan = new Texto(Recursos.FUENTE_MENU, 48, Color.WHITE, true);
        tPan.setTexto("Pan");

        tCerrar = new Texto(Recursos.FUENTE_MENU, 48, Color.RED, true);
        tCerrar.setTexto("Cerrar");

        tInventario = new Texto(Recursos.FUENTE_MENU, 32, Color.YELLOW, true);
        actualizarTextoInventario();
    }

    private void actualizarTextoInventario() {
        if (JUGADOR.tieneInventarioLleno()) {
            tInventario.setTexto("Inventario: " + JUGADOR.getInventario().getNombre());
        } else {
            tInventario.setTexto("Inventario: Vacío");
        }
    }

    @Override
    protected void posicionarElementos() {
        float anchoViewport = viewport.getWorldWidth();
        float altoViewport = viewport.getWorldHeight();
        float centroX = anchoViewport / 2f;
        float centroY = altoViewport / 2f;
        float espaciado = 80f;

        tCarne.setPosition(centroX - tCarne.getAncho() / 2f, centroY + espaciado);
        tPan.setPosition(centroX - tCarne.getAncho() / 2f, centroY + espaciado*2);
        tCerrar.setPosition(centroX - tCerrar.getAncho() / 2f, centroY - espaciado * 3);
        tInventario.setPosition(50, altoViewport - 50);
    }

    @Override
    protected void registrarInteracciones() {
        entrada.registrar(new TextoInteractuable(tCarne, () -> {
            if (!JUGADOR.tieneInventarioLleno()) {
                JUGADOR.guardarEnInventario(new Carne(texturaCarne));
                PantallaJuego juego = (PantallaJuego) Pantalla.getPantallaActual();
                JUGADOR.setAnimacion(juego.getAnimacionConItem("carne"));
                actualizarTextoInventario();
            }
        }));

        entrada.registrar(new TextoInteractuable(tPan, () -> {
            if (!JUGADOR.tieneInventarioLleno()) {
                JUGADOR.guardarEnInventario(new Pan(texturaPan));
                PantallaJuego juego = (PantallaJuego) Pantalla.getPantallaActual();
                JUGADOR.setAnimacion(juego.getAnimacionConItem("pan"));
                actualizarTextoInventario();
            }
        }));

        entrada.registrar(new TextoInteractuable(tCerrar, () -> {
            cerrarMaquina();
        }));
    }

    @Override
    protected void renderizarInterfaz() {
        tCarne.dibujar();
        tPan.dibujar();
        tCerrar.dibujar();
        tInventario.dibujar();
    }

    @Override
    protected void cerrarMaquina() {
        Pantalla pantallaActual = Pantalla.getPantallaActual();
        if (pantallaActual instanceof PantallaJuego) {
            ((PantallaJuego) pantallaActual).cerrarHeladera();
        }
    }
}
