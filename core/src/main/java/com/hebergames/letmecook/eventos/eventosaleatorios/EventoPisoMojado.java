package com.hebergames.letmecook.eventos.eventosaleatorios;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.utiles.GestorTexturas;

import java.util.ArrayList;
import java.util.Random;

public class EventoPisoMojado implements EventoAleatorio {
    private ArrayList<Rectangle> tilesAfectadas;
    private boolean activo;
    private static final String NOMBRE = "Piso Mojado";
    private static final float PROBABILIDAD = 0.20f; // 20% por ronda
    private static final int MIN_TILES = 3;
    private static final int MAX_TILES = 8;

    private ArrayList<Rectangle> todasLasTiles;
    private Random random;

    public EventoPisoMojado(ArrayList<Rectangle> tilesDisponibles) {
        this.todasLasTiles = tilesDisponibles;
        this.tilesAfectadas = new ArrayList<>();
        this.random = new Random();
        this.activo = false;
    }

    @Override
    public void activar() {
        if (!activo && todasLasTiles != null && !todasLasTiles.isEmpty()) {
            tilesAfectadas.clear();

            int cantidadTiles = MIN_TILES + random.nextInt(MAX_TILES - MIN_TILES + 1);
            cantidadTiles = Math.min(cantidadTiles, todasLasTiles.size());

            ArrayList<Rectangle> tilesDisponibles = new ArrayList<>(todasLasTiles);

            for (int i = 0; i < cantidadTiles; i++) {
                int index = random.nextInt(tilesDisponibles.size());
                tilesAfectadas.add(tilesDisponibles.remove(index));
            }

            activo = true;
        }
    }

    @Override
    public void desactivar() {
        tilesAfectadas.clear();
        activo = false;
    }

    @Override
    public boolean estaActivo() {
        return activo;
    }

    @Override
    public String getNombre() {
        return NOMBRE;
    }

    @Override
    public float getProbabilidad() {
        return PROBABILIDAD;
    }

    public boolean estaSobrePisoMojado(float x, float y) {
        if (!activo) return false;

        for (Rectangle tile : tilesAfectadas) {
            if (tile.contains(x, y)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Rectangle> getTilesAfectadas() {
        return tilesAfectadas;
    }

    public void dibujar(SpriteBatch batch) {
        if (!activo) return;

        TextureRegion textura = GestorTexturas.getInstance().getTexturaPisoMojado();
        if (textura == null) return;

        for (Rectangle tile : tilesAfectadas) {
            batch.draw(textura, tile.x, tile.y, tile.width, tile.height);
        }
    }
}
