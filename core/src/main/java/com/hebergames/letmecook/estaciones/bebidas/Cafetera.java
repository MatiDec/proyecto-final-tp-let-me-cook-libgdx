package com.hebergames.letmecook.estaciones.bebidas;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.entregables.productos.bebidas.Cafe;
import com.hebergames.letmecook.entregables.productos.bebidas.EstadoMenuBebida;
import com.hebergames.letmecook.entregables.productos.bebidas.TamanoBebida;
import com.hebergames.letmecook.entregables.productos.TipoProducto;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;
import com.hebergames.letmecook.utiles.Recursos;

public class Cafetera extends EstacionTrabajo {

    private EstadoMenuBebida estadoMenu;
    private String tipoSeleccionado;
    private String[] tiposDisponibles;
    private TamanoBebida tamanoSeleccionado;
    private int seleccionTamano;
    private int seleccionTipo;

    private Cafe cafeEnPreparacion;
    private float tiempoPreparacion;
    private float tiempoTranscurrido;

    private Texto textoMenu;
    private Texto textoOpciones;

    public Cafetera(Rectangle area) {
        super(area);
        this.estadoMenu = EstadoMenuBebida.SELECCION_TAMANO;
        this.seleccionTamano = 0;
        this.seleccionTipo = 0;
        this.textoMenu = new Texto(Recursos.FUENTE_MENU, 20, Color.WHITE, true);
        this.textoOpciones = new Texto(Recursos.FUENTE_MENU, 16, Color.YELLOW, true);
        this.tiposDisponibles = Cafe.getTiposCafe().keySet().toArray(new String[0]);
    }

    @Override
    protected void alLiberar() {
        // Resetear estado si el jugador se aleja
        if (estadoMenu != EstadoMenuBebida.PREPARANDO && estadoMenu != EstadoMenuBebida.LISTO) {
            estadoMenu = EstadoMenuBebida.SELECCION_TAMANO;
            seleccionTamano = 0;
            seleccionTipo = 0;
        }
    }

    @Override
    protected void iniciarMenu(Jugador jugador) {
        // Si hay café listo, retirar
        if (estadoMenu == EstadoMenuBebida.LISTO && cafeEnPreparacion != null) {
            if (!jugador.tieneInventarioLleno()) {
                jugador.guardarEnInventario(cafeEnPreparacion);
                System.out.println("Café retirado: " + cafeEnPreparacion.getNombre());
                cafeEnPreparacion = null;
                estadoMenu = EstadoMenuBebida.SELECCION_TAMANO;
                seleccionTamano = 0;
                seleccionTipo = 0;
            } else {
                System.out.println("Inventario lleno");
            }
        }
    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion) {
        if (estadoMenu == EstadoMenuBebida.PREPARANDO || estadoMenu == EstadoMenuBebida.LISTO) {
            return;
        }

        if (estadoMenu == EstadoMenuBebida.SELECCION_TAMANO) {
            if (numeroSeleccion >= 1 && numeroSeleccion <= 3) {
                seleccionTamano = numeroSeleccion - 1;
                tamanoSeleccionado = TamanoBebida.values()[seleccionTamano];
                estadoMenu = EstadoMenuBebida.SELECCION_TIPO;
                seleccionTipo = 0;
                System.out.println("Tamaño seleccionado: " + tamanoSeleccionado.getNombre());
            }
        } else if (estadoMenu == EstadoMenuBebida.SELECCION_TIPO) {
            if (numeroSeleccion >= 1 && numeroSeleccion <= tiposDisponibles.length) {
                seleccionTipo = numeroSeleccion - 1;
                tipoSeleccionado = tiposDisponibles[seleccionTipo];
                iniciarPreparacion();
                System.out.println("Preparando: " + tipoSeleccionado);
            }
        }
    }

    private void iniciarPreparacion() {
        cafeEnPreparacion = new Cafe(tipoSeleccionado, tamanoSeleccionado);
        tiempoPreparacion = cafeEnPreparacion.getTiempoPreparacion();
        tiempoTranscurrido = 0f;
        estadoMenu = EstadoMenuBebida.PREPARANDO;
    }

    @Override
    public void actualizar(float delta) {
        super.actualizar(delta);

        if (estadoMenu == EstadoMenuBebida.PREPARANDO) {
            tiempoTranscurrido += delta;
            if (tiempoTranscurrido >= tiempoPreparacion) {
                estadoMenu = EstadoMenuBebida.LISTO;
                System.out.println("¡Café listo!");
            }
        }
    }

    @Override
    protected void dibujarMenu(SpriteBatch batch, Jugador jugador) {
        // Las coordenadas ya están en sistema UI desde EstacionTrabajo.dibujar()
        float menuX = 100f;
        float menuY = 400f;

        if (estadoMenu == EstadoMenuBebida.SELECCION_TAMANO) {
            textoMenu.setTexto("Selecciona tamaño:");
            textoMenu.setPosition(menuX, menuY + 80);
            textoMenu.dibujarEnUi(batch);

            String opciones = "1. Pequeño\n2. Mediano\n3. Grande";
            textoOpciones.setTexto(opciones);
            textoOpciones.setPosition(menuX, menuY + 40);
            textoOpciones.dibujarEnUi(batch);

        } else if (estadoMenu == EstadoMenuBebida.SELECCION_TIPO) {
            textoMenu.setTexto("Selecciona tipo de café:");
            textoMenu.setPosition(menuX, menuY + 100);
            textoMenu.dibujarEnUi(batch);

            StringBuilder opciones = new StringBuilder();
            for (int i = 0; i < tiposDisponibles.length; i++) {
                opciones.append((i + 1)).append(". ").append(tiposDisponibles[i]);
                if (i < tiposDisponibles.length - 1) opciones.append("\n");
            }
            textoOpciones.setTexto(opciones.toString());
            textoOpciones.setPosition(menuX, menuY + 40);
            textoOpciones.dibujarEnUi(batch);

        } else if (estadoMenu == EstadoMenuBebida.PREPARANDO) {
            float progreso = (tiempoTranscurrido / tiempoPreparacion) * 100f;
            textoMenu.setTexto(String.format("Preparando... %.0f%%", progreso));
            textoMenu.setPosition(menuX, menuY + 40);
            textoMenu.dibujarEnUi(batch);

        } else if (estadoMenu == EstadoMenuBebida.LISTO) {
            textoMenu.setTexto("¡Café listo! Presiona E");
            textoMenu.setPosition(menuX, menuY + 40);
            textoMenu.dibujarEnUi(batch);
        }
    }

    @Override
    public void alInteractuar() {
        // Si hay café listo, intentar retirarlo
        if (estadoMenu == EstadoMenuBebida.LISTO && cafeEnPreparacion != null) {
            // La lógica de retiro está en iniciarMenu, pero necesitamos llamarla
            // cuando el jugador interactúa estando la bebida lista
            return; // iniciarMenu se llamará desde EstacionTrabajo
        }

        // Forzar actualización del estado del menú si no está visible
        if (estadoMenu == null) {
            estadoMenu = EstadoMenuBebida.SELECCION_TAMANO;
        }
    }
}
