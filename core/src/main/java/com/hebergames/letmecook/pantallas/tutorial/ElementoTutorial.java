package com.hebergames.letmecook.pantallas.tutorial;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.elementos.EfectoHover;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.utiles.Recursos;

public class ElementoTutorial {
    private String titulo;
    private String rutaMiniatura;
    private String rutaSpritesheet;
    private int frameWidth;
    private int frameHeight;
    private int cantidadFrames;
    private float fps;
    private Texture miniatura;
    private Texto textoTitulo;
    private float x, y;
    private static final float ANCHO = 200f;
    private static final float ALTO = 250f;
    private EfectoHover efectoHover;

    public ElementoTutorial(String titulo, String rutaMiniatura, String rutaSpritesheet,
                            int frameWidth, int frameHeight, int cantidadFrames, float fps) {
        this.titulo = titulo;
        this.rutaMiniatura = rutaMiniatura;
        this.rutaSpritesheet = rutaSpritesheet;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.cantidadFrames = cantidadFrames;
        this.fps = fps;
        cargarTextura();

        textoTitulo = new Texto(Recursos.FUENTE_MENU, 24, Color.WHITE, true);
        textoTitulo.setTexto(titulo);

        efectoHover = new EfectoHover(textoTitulo, Color.YELLOW);
    }

    private void cargarTextura() {
        try {
            miniatura = new Texture(Gdx.files.internal(rutaMiniatura));
        } catch (Exception e) {
            System.err.println("Error cargando miniatura: " + rutaMiniatura);
            miniatura = Recursos.PIXEL;
        }
    }

    public void setPosicion(float x, float y) {
        this.x = x;
        this.y = y;
        textoTitulo.setPosition(x + (ANCHO - textoTitulo.getAncho()) / 2f, y - 40f);
    }

    public void dibujar(SpriteBatch batch) {
        // Fondo del cuadro
        batch.setColor(0.2f, 0.2f, 0.2f, 1f);
        batch.draw(Recursos.PIXEL, x, y - ALTO, ANCHO, ALTO);

        // Borde
        batch.setColor(Color.WHITE);
        float grosor = 2f;
        batch.draw(Recursos.PIXEL, x, y - ALTO, ANCHO, grosor);
        batch.draw(Recursos.PIXEL, x, y, ANCHO, grosor);
        batch.draw(Recursos.PIXEL, x, y - ALTO, grosor, ALTO);
        batch.draw(Recursos.PIXEL, x + ANCHO - grosor, y - ALTO, grosor, ALTO);

        // Miniatura
        float miniaturaAlto = 150f;
        batch.setColor(Color.WHITE);
        batch.draw(miniatura, x + 10f, y - miniaturaAlto - 10f, ANCHO - 20f, miniaturaAlto);

        // Título
        textoTitulo.dibujar();

        batch.setColor(Color.WHITE);
    }

    public void actualizarHover(float mouseX, float mouseY) {
        efectoHover.actualizar(mouseX, mouseY);

        if (efectoHover.isEnHover()) {
            // Cambiar color del texto al pasar el mouse
            textoTitulo.getFuente().setColor(Color.YELLOW);
        } else {
            textoTitulo.getFuente().setColor(Color.WHITE);
        }
    }

    public boolean fueClickeado(float mouseX, float mouseY) {
        return mouseX >= x && mouseX <= x + ANCHO &&
            mouseY >= y - ALTO && mouseY <= y;
    }

    public String getRutaSpritesheet() {
        return rutaSpritesheet;
    }

    public int getFrameWidth() {
        return frameWidth;
    }

    public int getFrameHeight() {
        return frameHeight;
    }

    public int getCantidadFrames() {
        return cantidadFrames;
    }

    public float getFps() {
        return fps;
    }

    public void dispose() {
        if (miniatura != null) {
            miniatura.dispose();
        }
    }
}
