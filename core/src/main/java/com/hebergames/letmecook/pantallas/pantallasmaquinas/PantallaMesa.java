package com.hebergames.letmecook.pantallas.pantallasmaquinas;

import com.badlogic.gdx.graphics.Color;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.eventos.TextoInteractuable;
import com.hebergames.letmecook.entregables.*;
import com.hebergames.letmecook.entregables.ingredientes.*;
import com.hebergames.letmecook.entregables.recetas.*;
import com.hebergames.letmecook.entregables.productos.*;
import com.hebergames.letmecook.pantallas.Pantalla;
import com.hebergames.letmecook.pantallas.PantallaJuego;
import com.hebergames.letmecook.utiles.*;

import java.util.ArrayList;

public class PantallaMesa extends PantallaMaquina {

    private static PantallaMesa instancia;

    private final int MAX_SLOTS = 2;

    private Texto tSlot1, tSlot2, tPreparar, tLimpiar, tCerrar, tInventario, tProducto;

    // Estado persistente de la mesa
    private ArrayList<Ingrediente> inventarioMesa;
    private Producto productoPreparado;
    private GestorRecetas gestorRecetas;

    private PantallaMesa() {
        super(true); // true porque tiene overlay visual
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
    protected void ejecutarLogicaMaquina() {
        System.out.println("Mesa abierta");
    }

    @Override
    protected void actualizarLogicaMaquina(float delta) {
        // La mesa no necesita actualización continua
    }

    @Override
    protected void inicializarInterfaz() {
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
            JUGADOR.setAnimacion(((PantallaJuego) Pantalla.getPantallaActual()).getAnimacionNormal());

        }

        // Actualizar producto preparado
        if (productoPreparado != null) {
            tProducto.setTexto("Producto: " + productoPreparado.getNombre() + " (Click para tomar)");
        } else {
            tProducto.setTexto("Producto: Ninguno");
        }
    }

    @Override
    protected void posicionarElementos() {
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

    @Override
    protected void registrarInteracciones() {
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
            System.out.println("Se cerro");
            cerrarMaquina();
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
    protected void renderizarInterfaz() {
        tSlot1.dibujar();
        tSlot2.dibujar();
        tPreparar.dibujar();
        tLimpiar.dibujar();
        tProducto.dibujar();
        tCerrar.dibujar();
        tInventario.dibujar();
    }

    @Override
    protected void cerrarMaquina() {
        Pantalla pantallaActual = Pantalla.getPantallaActual();
        if (pantallaActual instanceof PantallaJuego) {
            ((PantallaJuego) pantallaActual).cerrarMesa();
        }
    }

    public static void resetearInstancia() {
        instancia = null;
    }
}
