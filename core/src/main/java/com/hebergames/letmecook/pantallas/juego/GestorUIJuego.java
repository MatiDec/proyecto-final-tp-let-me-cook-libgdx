package com.hebergames.letmecook.pantallas.juego;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entidades.clientes.Cliente;
import com.hebergames.letmecook.pedidos.TarjetaPedido;
import com.hebergames.letmecook.utiles.GestorTexturas;
import com.hebergames.letmecook.utiles.Recursos;

import java.util.ArrayList;

public class GestorUIJuego {

    private final ArrayList<ObjetoVisualizable> objetosUI;
    private final Texto textoContador;
    private final Texto textoInventario1;
    private final Texto textoInventario2;

    private static final float MARGEN = 50f;
    private ArrayList<Texto> textosPedidos;
    private ArrayList<TarjetaPedido> tarjetasPedidos;

    private Texto textoPuntaje;
    private final int MAX_PEDIDOS_VISIBLES = 5;

    public GestorUIJuego() {
        objetosUI = new ArrayList<>();

        textoContador = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);
        textoContador.setTexto("00:00");

        textoInventario1 = new Texto(Recursos.FUENTE_MENU, 32, Color.GREEN, true); // J1 en verde
        textoInventario1.setTexto("J1 Inventario: Vacío");

        textoInventario2 = new Texto(Recursos.FUENTE_MENU, 32, Color.BLUE, true); // J2 en azul
        textoInventario2.setTexto("J2 Inventario: Vacío");

        textoPuntaje = new Texto(Recursos.FUENTE_MENU, 32, Color.YELLOW, true);
        textoPuntaje.setTexto("Puntos: 0");
        objetosUI.add(textoPuntaje);

        textosPedidos = new ArrayList<>();

        tarjetasPedidos = new ArrayList<>();

        objetosUI.add(textoContador);
        objetosUI.add(textoInventario1);
        objetosUI.add(textoInventario2);

        actualizarPosiciones(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void actualizarPedidosActivos(ArrayList<Cliente> clientes) {
        // Liberar tarjetas anteriores antes de limpiar
        for (TarjetaPedido t : tarjetasPedidos) {
            t.dispose();
        }
        tarjetasPedidos.clear();
        textosPedidos.clear();

        for (int i = 0; i < Math.min(clientes.size(), MAX_PEDIDOS_VISIBLES); i++) {
            TarjetaPedido tarjeta = new TarjetaPedido();
            tarjetasPedidos.add(tarjeta);
        }
    }


    public void actualizarTiempo(int segundos) {
        int minutos = segundos / 60;
        int segundosRestantes = segundos % 60;
        String tiempoFormateado = String.format("%02d:%02d", minutos, segundosRestantes);
        textoContador.setTexto(tiempoFormateado);
    }

    public void actualizarPuntaje(int puntos) {
        textoPuntaje.setTexto("Puntos: " + puntos);
    }

    public void actualizarInventario(String nombreItem1, String nombreItem2) {
        if (nombreItem1 != null) {
            textoInventario1.setTexto("J1 Inventario: " + nombreItem1);
        }
        if (nombreItem2 != null) {
            textoInventario2.setTexto("J2 Inventario: " + nombreItem2);
        }

    }

    public void dibujar(SpriteBatch batch) {
        for (ObjetoVisualizable obj : objetosUI) {
            obj.dibujarEnUi(batch);
        }
        for (Texto texto : textosPedidos) {
            texto.dibujarEnUi(batch);
        }
    }

    public void dibujarPedidos(SpriteBatch batch, ArrayList<Cliente> clientes, float anchoUI, float altoUI) {
        float yInicial = altoUI / 2f;
        float x = anchoUI - 220f; // 200 (ancho tarjeta) + 20 (margen)

        for (int i = 0; i < Math.min(clientes.size(), MAX_PEDIDOS_VISIBLES); i++) {
            Cliente cliente = clientes.get(i);
            float y = yInicial - (i * 120f); // 100 (alto tarjeta) + 20 (espaciado)

            if (i < tarjetasPedidos.size()) {
                tarjetasPedidos.get(i).dibujar(batch, cliente, x, y,
                    GestorTexturas.getInstance().getTexturaCliente(), null);
            }
        }
    }

    public void actualizarPosiciones(float anchoUI, float altoUI) {
        // Contador de tiempo (Arriba, centro)
        textoContador.setPosition(anchoUI / 2f - textoContador.getAncho() / 2f, altoUI - MARGEN);
        // Inventario Jugador 1 (Arriba, izquierda)
        textoInventario1.setPosition(MARGEN, altoUI - MARGEN);
        // Inventario Jugador 2 (Arriba, derecha)
        textoInventario2.setPosition(anchoUI - textoInventario2.getAncho() - MARGEN, altoUI - MARGEN);
        textoPuntaje.setPosition(MARGEN, altoUI - MARGEN * 2);

        float margenPedidos = 100f;
        float yInicialPedidos = altoUI - margenPedidos;
        for (int i = 0; i < textosPedidos.size(); i++) {
            Texto texto = textosPedidos.get(i);
            float yPos = yInicialPedidos - (i * 30);
            texto.setPosition(anchoUI - texto.getAncho() - MARGEN, yPos);
        }
    }

    public void dispose() {
        for (TarjetaPedido tarjeta : tarjetasPedidos) {
            tarjeta.dispose();
        }
        tarjetasPedidos.clear();
    }

}
