package com.hebergames.letmecook.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.hebergames.letmecook.eventos.Interactuable;
import com.hebergames.letmecook.utiles.Render;

public class Texto implements Interactuable {

    BitmapFont fuente;
    private float x = 0, y = 0;
    private String texto = "";
    GlyphLayout layout;

    public Texto(final String RUTA_FUENTE, final int DIMENSION, final Color COLOR, final boolean SOMBRA) {

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(RUTA_FUENTE));
        FreeTypeFontGenerator.FreeTypeFontParameter parametro = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parametro.size = DIMENSION;
        parametro.color = COLOR;
        if(SOMBRA) {
            parametro.shadowColor = Color.BLACK;
            parametro.shadowOffsetX = 1;
            parametro.shadowOffsetY = 1;
        }

        fuente = generator.generateFont(parametro);
        layout = new GlyphLayout();
    }

    @Override
    public boolean fueClickeado(float x, float y) {
        float ancho = getAncho();
        float alto = getAlto();

        float yInferior = this.y - alto;
        return x >= this.x && x<=this.x + ancho && y >= yInferior && y <= this.y; // Si quieren se puede hacer más legible pero así funciona bien
    }

    @Override
    public void alClick() {

    }

    public void dibujar() {
        fuente.draw(Render.batch, this.texto, this.x, this.y);
    }

    public void setTexto(String texto) {
        this.texto = texto;
        this.layout.setText(fuente, texto);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getAncho() {
        return this.layout.width;
    }

    public float getAlto() {
        return this.layout.height;
    }

    public Vector2 getPosicion() {
        return new Vector2(this.x, this.y);
    }
}
