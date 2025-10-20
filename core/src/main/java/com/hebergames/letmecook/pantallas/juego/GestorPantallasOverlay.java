package com.hebergames.letmecook.pantallas.juego;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.pantallas.PantallaCalendario;
import com.hebergames.letmecook.pantallas.PantallaPausa;
import com.hebergames.letmecook.sonido.GestorAudio;

public class GestorPantallasOverlay {

    private PantallaPausa pantallaPausa;
    private PantallaCalendario pantallaCalendario;
    private boolean calendarioVisible;
    private boolean juegoEnPausa;

    private final GestorAudio gestorAudio;

    public GestorPantallasOverlay(PantallaPausa pantallaPausa, PantallaCalendario pantallaCalendario, GestorAudio gestorAudio) {
        this.pantallaPausa = pantallaPausa;
        this.pantallaCalendario = pantallaCalendario;
        this.gestorAudio = gestorAudio;
        this.juegoEnPausa = false;
        this.calendarioVisible = false;
    }

    public void toggleCalendario() {
        calendarioVisible = !calendarioVisible;

        if (calendarioVisible) {
            if (juegoEnPausa) {
                juegoEnPausa = false;
            }
            pantallaCalendario.show();
        }
    }

    public void togglePausa() {
        juegoEnPausa = !juegoEnPausa;

        if (juegoEnPausa) {
            pantallaPausa.show();
            gestorAudio.pausarMusica();
        } else {
            gestorAudio.reanudarMusica();
        }
    }

    public void reanudarJuego() {
        juegoEnPausa = false;
        gestorAudio.reanudarMusica();
    }

    public void renderOverlays(float delta, SpriteBatch batch) {
        if (juegoEnPausa) {
            pantallaPausa.render(delta);
        } else if (calendarioVisible) {
            pantallaCalendario.render(delta);
        }
    }

    public void dispose() {
        if (pantallaPausa != null) {
            pantallaPausa.dispose();
        }
        if (pantallaCalendario != null) {
            pantallaCalendario.dispose();
        }
    }

    // Getters
    public boolean isJuegoEnPausa() {
        return juegoEnPausa;
    }

    public boolean isCalendarioVisible() { return calendarioVisible; }

    public boolean hayOverlayActivo() {
        return juegoEnPausa;
    }
}
