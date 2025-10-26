package com.hebergames.letmecook.estaciones;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.entregables.ingredientes.TipoEnvase;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.entregables.recetas.GestorRecetas;
import com.hebergames.letmecook.entregables.recetas.Receta;
import com.hebergames.letmecook.utiles.GestorJugadores;
import com.hebergames.letmecook.utiles.Recursos;

import java.util.ArrayList;
import java.util.List;

public class MaquinaEnvasadora extends EstacionTrabajo {

    private static final float MARGEN = 50f;
    private static final float ESPACIADO = 40f;

    private List<OpcionMenu> opcionesMenu;
    private List<Texto> textosMenu;

    public MaquinaEnvasadora(Rectangle area) {
        super(area);
        inicializarOpciones();
    }

    @Override
    protected void alLiberar() {
        // No necesita limpiar nada especial
    }

    private void inicializarOpciones() {
        opcionesMenu = new ArrayList<>();
        opcionesMenu.add(new OpcionMenu(1, "Envasar Ingrediente", () -> envasarIngrediente()));
    }

    private void envasarIngrediente() {
        Jugador jugador = getJugadorOcupante();
        if (jugador == null) {
            return;
        }

        ObjetoAlmacenable objetoInventario = jugador.getInventario();

        if (!(objetoInventario instanceof Ingrediente)) {
            return;
        }

        Ingrediente ingrediente = (Ingrediente) objetoInventario;
        String nombreIngrediente = ingrediente.getNombre();

        // Verificar si este ingrediente se puede envasar
        TipoEnvase tipoEnvase = TipoEnvase.obtenerPorIngrediente(nombreIngrediente);

        if (tipoEnvase == null) {
            return;
        }

        // Crear el envase como ingrediente temporal
        Ingrediente envase = tipoEnvase.crearEnvase();

        // Buscar receta que combine el envase con el ingrediente
        ArrayList<Ingrediente> ingredientes = new ArrayList<>();
        ingredientes.add(envase);
        ingredientes.add(ingrediente);

        Receta receta = GestorRecetas.getInstance().buscarReceta(ingredientes);

        if (receta == null) {
            return;
        }

        // Preparar el producto
        Producto productoEnvasado = receta.preparar();

        // Retirar el ingrediente del inventario y darle el producto
        jugador.sacarDeInventario();
        jugador.guardarEnInventario(productoEnvasado);
    }

    @Override
    protected void iniciarMenu(Jugador jugador) {
        textosMenu = new ArrayList<>();

        Texto texto = new Texto(Recursos.FUENTE_MENU, 24, Color.WHITE, true);

        ObjetoAlmacenable objeto = jugador.getInventario();
        String textoOpcion = "1. Envasar Ingrediente";

        if (objeto instanceof Ingrediente) {
            Ingrediente ingrediente = (Ingrediente) objeto;
            TipoEnvase tipoEnvase = TipoEnvase.obtenerPorIngrediente(ingrediente.getNombre());

            if (tipoEnvase != null) {
                textoOpcion += " [" + ingrediente.getNombre() + " → " + tipoEnvase.getNombre() + "]";
            } else {
                textoOpcion += " [No válido]";
            }
        } else {
            textoOpcion += " [Sin ingrediente]";
        }

        texto.setTexto(textoOpcion);
        textosMenu.add(texto);
    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion) {
        for (OpcionMenu opcion : opcionesMenu) {
            if (opcion.getNumero() == numeroSeleccion) {
                if (opcion.esAccionSimple()) {
                    opcion.ejecutarAccion();
                    iniciarMenu(jugador);

                    // Salir del menú automáticamente después de envasar
                    if (getJugadorOcupante() != null) {
                        jugador.salirDeMenu();
                        alLiberar();
                    }
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

        float anchoMenu = 500f;
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
        iniciarMenu(getJugadorOcupante());
    }
}
