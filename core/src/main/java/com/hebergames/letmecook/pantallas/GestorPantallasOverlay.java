package com.hebergames.letmecook.pantallas;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.utiles.GestorAudio;

public class GestorPantallasOverlay {

    private PantallaPausa pantallaPausa;

    private boolean juegoEnPausa = false;

    private final GestorAudio gestorAudio;

    public GestorPantallasOverlay(PantallaPausa pantallaPausa, GestorAudio gestorAudio) {
        this.pantallaPausa = pantallaPausa;
        this.gestorAudio = gestorAudio;
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
        }
    }

    public void dispose() {
        if (pantallaPausa != null) {
            pantallaPausa.dispose();
        }
    }

    // Getters
    public boolean isJuegoEnPausa() {
        return juegoEnPausa;
    }

    public boolean hayOverlayActivo() {
        return juegoEnPausa;
    }
}
