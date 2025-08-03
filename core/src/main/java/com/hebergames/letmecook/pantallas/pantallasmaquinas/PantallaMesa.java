package com.hebergames.letmecook.pantallas.pantallasmaquinas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.TextoInteractuable;
import com.hebergames.letmecook.entregables.*;
import com.hebergames.letmecook.entregables.ingredientes.*;
import com.hebergames.letmecook.entregables.recetas.*;
import com.hebergames.letmecook.entregables.productos.*;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.pantallas.PantallaJuego;
import com.hebergames.letmecook.utiles.*;

import java.util.ArrayList;

public class PantallaMesa extends Pantalla {

    private static PantallaMesa instancia;

    private final JugadorHost JUGADOR;
    private final SpriteBatch BATCH;
    private final int MAX_SLOTS = 2;

    private Texto tSlot1, tSlot2, tPreparar, tLimpiar, tCerrar, tInventario, tProducto;
    private Entrada entrada;
    private Viewport viewport;
    private OrthographicCamera camara;

    // Estado persistente de la mesa
    private ArrayList<Ingrediente> inventarioMesa;
    private Producto productoPreparado;
    private GestorRecetas gestorRecetas;

    private PantallaMesa() {
        this.JUGADOR = Configuracion.getInstancia().getJugadorPrincipal();
        this.BATCH = Render.batch;
        this.camara = new OrthographicCamera();
        this.viewport = new ScreenViewport(camara);
        this.inventarioMesa = new ArrayList<>();
        this.gestorRecetas = GestorRecetas.getInstance();
    }

    public static PantallaMesa getInstancia() {
        if (instancia == null) {
            instancia = new PantallaMesa();
        }
        return instancia;
    }

    @Override
    public void show() {
        viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);
        inicializarTextos();
        posicionarTextos();
        registrarEntradas();
    }

    private void inicializarTextos() {
        tSlot1 = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);
        tSlot2 = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);

        tPreparar = new Texto(Recursos.FUENTE_MENU, 48, Color.GREEN, true);
        tPreparar.setTexto("Preparar");

        tLimpiar = new Texto(Recursos.FUENTE_MENU, 32, Color.ORANGE, true);
        tLimpiar.setTexto("Limpiar Mesa");

        tCerrar = new Texto(Recursos.FUENTE_MENU, 48, Color.RED, true);
        tCerrar.setTexto("Cerrar");

        tInventario = new Texto(Recursos.FUENTE_MENU, 32, Color.YELLOW, true);

        tProducto = new Texto(Recursos.FUENTE_MENU, 32, Color.CYAN, true);

        actualizarTextos();
    }

    private void actualizarTextos() {
        // Actualizar slots de ingredientes
        tSlot1.setTexto("Slot 1: " + (inventarioMesa.size() > 0 ? inventarioMesa.get(0).getNombre() : "Vacío"));
        tSlot2.setTexto("Slot 2: " + (inventarioMesa.size() > 1 ? inventarioMesa.get(1).getNombre() : "Vacío"));

        // Actualizar inventario jugador
        if (JUGADOR.tieneInventarioLleno()) {
            tInventario.setTexto("Inventario: " + JUGADOR.getInventario().getNombre());
        } else {
            tInventario.setTexto("Inventario: Vacío");
        }

        // Actualizar producto preparado
        if (productoPreparado != null) {
            tProducto.setTexto("Producto: " + productoPreparado.getNombre() + " (Click para tomar)");
        } else {
            tProducto.setTexto("Producto: Ninguno");
        }
    }

    private void posicionarTextos() {
        float anchoViewport = viewport.getWorldWidth();
        float altoViewport = viewport.getWorldHeight();
        float centroX = anchoViewport / 2f;
        float centroY = altoViewport / 2f;
        float espaciado = 60f;

        tSlot1.setPosition(centroX - tSlot1.getAncho() / 2f, centroY + espaciado * 2);
        tSlot2.setPosition(centroX - tSlot2.getAncho() / 2f, centroY + espaciado);
        tPreparar.setPosition(centroX - tPreparar.getAncho() / 2f, centroY);
        tLimpiar.setPosition(centroX - tLimpiar.getAncho() / 2f, centroY - espaciado);
        tProducto.setPosition(centroX - tProducto.getAncho() / 2f, centroY - espaciado * 2);
        tCerrar.setPosition(centroX - tCerrar.getAncho() / 2f, centroY - espaciado * 4);
        tInventario.setPosition(50, altoViewport - 50);
    }

    private void registrarEntradas() {
        entrada.registrar(new TextoInteractuable(tSlot1, () -> {
            if (inventarioMesa.size() > 0) {
                // Devolver ingrediente al jugador
                if (!JUGADOR.tieneInventarioLleno()) {
                    JUGADOR.guardarEnInventario(inventarioMesa.remove(0));
                    actualizarTextos();
                }
            } else {
                // Agregar ingrediente del jugador al slot (solo si es ingrediente)
                if (JUGADOR.tieneInventarioLleno() && JUGADOR.getInventario() instanceof Ingrediente) {
                    inventarioMesa.add((Ingrediente) JUGADOR.sacarDeInventario());
                    actualizarTextos();
                }
            }
        }));

        entrada.registrar(new TextoInteractuable(tSlot2, () -> {
            if (inventarioMesa.size() > 1) {
                // Devolver ingrediente al jugador
                if (!JUGADOR.tieneInventarioLleno()) {
                    JUGADOR.guardarEnInventario(inventarioMesa.remove(1));
                    actualizarTextos();
                }
            } else if (inventarioMesa.size() == 1) {
                // Agregar ingrediente del jugador al slot 2 (solo si es ingrediente)
                if (JUGADOR.tieneInventarioLleno() && JUGADOR.getInventario() instanceof Ingrediente) {
                    inventarioMesa.add((Ingrediente) JUGADOR.sacarDeInventario());
                    actualizarTextos();
                }
            }
        }));

        entrada.registrar(new TextoInteractuable(tPreparar, () -> {
            if (inventarioMesa.size() >= 2 && productoPreparado == null) {
                Receta receta = gestorRecetas.buscarReceta(inventarioMesa);
                if (receta != null) {
                    productoPreparado = receta.preparar();
                    inventarioMesa.clear();
                    actualizarTextos();
                }
            }
        }));

        entrada.registrar(new TextoInteractuable(tLimpiar, () -> {
            limpiarMesa();
        }));

        entrada.registrar(new TextoInteractuable(tProducto, () -> {
            if (productoPreparado != null && !JUGADOR.tieneInventarioLleno()) {
                JUGADOR.guardarEnInventario(productoPreparado);
                productoPreparado = null;
                actualizarTextos();
            }
        }));

        entrada.registrar(new TextoInteractuable(tCerrar, () -> {
            Pantalla pantallaActual = Pantalla.getPantallaActual();
            if (pantallaActual instanceof PantallaJuego) {
                ((PantallaJuego) pantallaActual).cerrarMesa();
            }
        }));
    }

    private void limpiarMesa() {
        // Si el jugador no tiene nada en el inventario, devolver un ingrediente
        if (!JUGADOR.tieneInventarioLleno() && !inventarioMesa.isEmpty()) {
            JUGADOR.guardarEnInventario(inventarioMesa.remove(0));
            actualizarTextos();
            return;
        }
    }

    @Override
    public void render(float delta) {
        entrada.actualizarEntradas();
        viewport.apply();
        camara.update();

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        BATCH.setProjectionMatrix(camara.combined);
        BATCH.begin();

        BATCH.setColor(0, 0, 0, 0.5f);
        float anchoViewport = viewport.getWorldWidth();
        float altoViewport = viewport.getWorldHeight();
        BATCH.draw(Recursos.PIXEL, 0, 0, anchoViewport, altoViewport);
        BATCH.setColor(1, 1, 1, 1);

        tSlot1.dibujar();
        tSlot2.dibujar();
        tPreparar.dibujar();
        tLimpiar.dibujar();
        tProducto.dibujar();
        tCerrar.dibujar();
        tInventario.dibujar();

        BATCH.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        posicionarTextos();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    public static void resetearInstancia() {
        instancia = null;
    }
}
