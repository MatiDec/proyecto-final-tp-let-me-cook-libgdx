package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;
import com.hebergames.letmecook.entregables.ingredientes.*;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.utiles.GestorJugadores;
import com.hebergames.letmecook.utiles.Recursos;

import java.util.ArrayList;
import java.util.List;

public class Heladera extends EstacionTrabajo {

    private List<OpcionMenu> opcionesMenu;
    private List<Texto> textosMenu;
    private static final float MARGEN = 50f;
    private static final float ESPACIADO = 40f;

    public Heladera(Rectangle area) {
        super(area);
        inicializarOpciones();
    }

    private void inicializarOpciones() {
        opcionesMenu = new ArrayList<>();

        opcionesMenu.add(new OpcionMenu(1, "Pan", () -> new Pan(Recursos.INGREDIENTES)));
        opcionesMenu.add(new OpcionMenu(2, "Carne", () -> new Carne(Recursos.INGREDIENTES)));
        // se pueden agregar hasta 9 opciones (limitado por las teclas numéricas)
    }

    @Override
    protected void alLiberar() {
        // Limpiar los textos del menú para que se inicialicen de nuevo al interactuar
        textosMenu = null;
        System.out.println("DEBUG Heladera: Menú limpiado al liberar");
    }

    @Override
    protected void iniciarMenu(Jugador jugador) {
        textosMenu = new ArrayList<>();

        for (OpcionMenu opcion : opcionesMenu) {
            Texto texto = new Texto(Recursos.FUENTE_MENU, 24, Color.WHITE, true);
            texto.setTexto(opcion.getTextoMenu());
            textosMenu.add(texto);
        }
    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion) {
        int i = 0;
        boolean encontrado = false;

        while (i < opcionesMenu.size() && !encontrado) {
            OpcionMenu opcion = opcionesMenu.get(i);

            if (opcion.getNumero() == numeroSeleccion) {
                encontrado = true;

                if (!jugador.tieneInventarioLleno()) {
                    ObjetoAlmacenable objeto = opcion.crearObjeto();
                    if (objeto != null) {
                        jugador.guardarEnInventario(objeto);
                        System.out.println("Tomado de heladera: " + opcion.getNombre());
                    }
                } else {
                    System.out.println("Inventario lleno, no puedes tomar más items");
                }
            }

            i++;
        }
    }

    @Override
    protected void dibujarMenu(SpriteBatch batch, Jugador jugador) {
        if (textosMenu == null || textosMenu.isEmpty()) {
            System.out.println("DEBUG Heladera: Saliendo porque textosMenu está vacío");
            return;
        }

        // Determinar si es jugador 1 o 2 para posicionar el menú
        List<Jugador> jugadores = GestorJugadores.getInstancia().getJugadores();
        boolean esJugador1 = (jugadores.size() > 0 && jugador == jugadores.get(0));

        // Posición X: izquierda para J1, derecha para J2
        float anchoMenu = 200f;
        float x = esJugador1 ? MARGEN : Gdx.graphics.getWidth() - anchoMenu - MARGEN;

        // Posición Y: centrado verticalmente
        float alturaTotal = textosMenu.size() * ESPACIADO;
        float y = (Gdx.graphics.getHeight() / 2f) + (alturaTotal / 2f);

        // Dibujar cada opción del menú
        for (Texto texto : textosMenu) {
            texto.setPosition(x, y);
            texto.dibujarEnUi(batch);
            y -= ESPACIADO;
        }
    }

    @Override
    public void alInteractuar() {
        Jugador jugador = getJugadorOcupante();
        if (jugador == null) {
            System.out.println("ERROR: Jugador ocupante es null en Heladera");
            return;
        }

        // Llamar a iniciarMenu solo si aún no se ha hecho (si textosMenu es null)
        if (textosMenu == null) {
            iniciarMenu(jugador);
        }
    }
}
