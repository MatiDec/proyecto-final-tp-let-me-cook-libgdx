package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.entregables.recetas.GestorRecetas;
import com.hebergames.letmecook.entregables.recetas.Receta;
import com.hebergames.letmecook.utiles.GestorJugadores;
import com.hebergames.letmecook.utiles.Recursos;

import java.util.ArrayList;
import java.util.List;

public class Mesa extends EstacionTrabajo {

    private static final int MAX_SLOTS = 2;
    private List<OpcionMenu> opcionesMenu;
    private List<Texto> textosMenu;
    private static final float MARGEN = 50f;
    private static final float ESPACIADO = 40f;

    // Slots para ingredientes
    private ObjetoAlmacenable[] slots;

    // Producto preparado listo para retirar
    private Producto productoPreparado;

    public Mesa(Rectangle area) {
        super(area);
        slots = new ObjetoAlmacenable[MAX_SLOTS];
        productoPreparado = null;
        inicializarOpciones();
    }

    private void inicializarOpciones() {
        opcionesMenu = new ArrayList<>();

        // Opciones para depositar en cada slot
        opcionesMenu.add(new OpcionMenu(1, "Slot 1", () -> depositarEnSlot(0)));
        opcionesMenu.add(new OpcionMenu(2, "Slot 2", () -> depositarEnSlot(1)));

        // Opción para crear producto
        opcionesMenu.add(new OpcionMenu(3, "Crear Producto", () -> crearProducto()));

        // Opción para retirar producto
        opcionesMenu.add(new OpcionMenu(4, "Retirar Producto", () -> retirarProducto()));
    }

    private void depositarEnSlot(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= MAX_SLOTS) {
            System.out.println("Slot inválido: " + slotIndex);
            return;
        }

        Jugador jugador = getJugadorOcupante();
        if (jugador == null) {
            System.out.println("No hay jugador ocupando la mesa");
            return;
        }

        // Si el slot ya tiene algo, retirar el item al inventario del jugador
        if (slots[slotIndex] != null) {
            if (!jugador.tieneInventarioLleno()) {
                ObjetoAlmacenable objetoRetirado = slots[slotIndex];
                slots[slotIndex] = null;
                jugador.guardarEnInventario(objetoRetirado);
                System.out.println("Retirado " + objetoRetirado.getNombre() + " del Slot " + (slotIndex + 1));
            } else {
                System.out.println("Inventario lleno, no puedes retirar el item del slot");
            }
            return;
        }

        // Si el slot está vacío, depositar del inventario
        ObjetoAlmacenable objeto = jugador.getInventario();

        if (objeto == null) {
            System.out.println("No tienes nada en el inventario para depositar");
            return;
        }

        // Depositar el objeto del inventario en el slot
        slots[slotIndex] = objeto;
        jugador.sacarDeInventario();
        System.out.println("Depositado " + objeto.getNombre() + " en Slot " + (slotIndex + 1));
    }

    private void crearProducto() {
        if (productoPreparado != null) {
            System.out.println("Ya hay un producto preparado. Retíralo primero.");
            return;
        }

        // Recopilar solo ingredientes de los slots
        ArrayList<Ingrediente> ingredientesDisponibles = new ArrayList<>();
        for (ObjetoAlmacenable objeto : slots) {
            if (objeto instanceof Ingrediente) {
                ingredientesDisponibles.add((Ingrediente) objeto);
            }
        }

        if (ingredientesDisponibles.isEmpty()) {
            System.out.println("No hay ingredientes en la mesa para preparar");
            return;
        }

        // Buscar receta que coincida
        Receta receta = GestorRecetas.getInstance().buscarReceta(ingredientesDisponibles);

        if (receta == null) {
            System.out.println("No se encontró ninguna receta con estos ingredientes");
            return;
        }

        // Preparar el producto
        productoPreparado = receta.preparar();

        // Limpiar los slots
        for (int i = 0; i < slots.length; i++) {
            slots[i] = null;
        }

        System.out.println("¡Producto creado: " + productoPreparado.getNombre() + "!");
    }

    private void retirarProducto() {
        Jugador jugador = getJugadorOcupante();
        if (jugador == null) {
            System.out.println("No hay jugador ocupando la mesa");
            return;
        }

        if (productoPreparado == null) {
            System.out.println("No hay ningún producto para retirar");
            return;
        }

        if (jugador.tieneInventarioLleno()) {
            System.out.println("Tu inventario está lleno. No puedes retirar el producto.");
            return;
        }

        // Dar el producto al jugador
        jugador.guardarEnInventario(productoPreparado);
        System.out.println("Retirado: " + productoPreparado.getNombre());
        productoPreparado = null;
    }

    @Override
    protected void iniciarMenu(Jugador jugador) {
        textosMenu = new ArrayList<>();

        for (OpcionMenu opcion : opcionesMenu) {
            Texto texto = new Texto(Recursos.FUENTE_MENU, 24, Color.WHITE, true);

            // Mostrar información de cada slot
            String textoOpcion = opcion.getTextoMenu();
            if (opcion.getNumero() <= MAX_SLOTS) {
                int slotIndex = opcion.getNumero() - 1;
                if (slots[slotIndex] != null) {
                    textoOpcion += " [" + slots[slotIndex].getNombre() + "]";
                } else {
                    textoOpcion += " [Vacío]";
                }
            } else if (opcion.getNumero() == 4) {
                // Mostrar si hay producto preparado
                if (productoPreparado != null) {
                    textoOpcion += " [" + productoPreparado.getNombre() + "]";
                } else {
                    textoOpcion += " [No disponible]";
                }
            }

            texto.setTexto(textoOpcion);
            textosMenu.add(texto);
        }
    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion) {
        // Buscar y ejecutar la opción seleccionada
        for (OpcionMenu opcion : opcionesMenu) {
            if (opcion.getNumero() == numeroSeleccion) {
                if (opcion.esAccionSimple()) {
                    opcion.ejecutarAccion();
                    // Actualizar el menú después de la acción
                    iniciarMenu(jugador);
                }
                break;
            }
        }
    }

    @Override
    protected void dibujarMenu(SpriteBatch batch, Jugador jugador) {
        if (textosMenu == null || textosMenu.isEmpty()) return;

        List<Jugador> jugadores = GestorJugadores.getInstancia().getJugadores();
        boolean esJugador1 = (jugadores.size() > 0 && jugador == jugadores.get(0));

        float anchoMenu = 400f;
        float x = esJugador1 ? MARGEN : Gdx.graphics.getWidth() - anchoMenu - MARGEN;

        float alturaTotal = textosMenu.size() * ESPACIADO;
        float y = (Gdx.graphics.getHeight() / 2f) + (alturaTotal / 2f);

        for (Texto texto : textosMenu) {
            texto.setPosition(x, y);
            texto.dibujarEnUi(batch);
            y -= ESPACIADO;
        }
    }

    @Override
    public void alInteractuar() {
        System.out.println("Interactuando con mesa");
        iniciarMenu(getJugadorOcupante());
    }

    public void limpiarMesa() {
        for (int i = 0; i < slots.length; i++) {
            slots[i] = null;
        }
        productoPreparado = null;
    }

    // Getters útiles para debugging o interfaz
    public ObjetoAlmacenable getObjetoEnSlot(int index) {
        if (index >= 0 && index < MAX_SLOTS) {
            return slots[index];
        }
        return null;
    }

    public boolean tieneProductoPreparado() {
        return productoPreparado != null;
    }

    public Producto getProductoPreparado() {
        return productoPreparado;
    }
}
