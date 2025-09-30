package com.hebergames.letmecook.pantallas;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaHeladera;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaMesa;
import com.hebergames.letmecook.utiles.GestorAudio;

public class GestorPantallasOverlay {

    private PantallaPausa pantallaPausa;
    private PantallaHeladera pantallaHeladera;
    private PantallaMesa pantallaMesa;

    private boolean juegoEnPausa = false;
    private boolean heladeraAbierta = false;
    private boolean mesaAbierta = false;

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

    public void abrirHeladera() {
        if (!heladeraAbierta) {
            heladeraAbierta = true;
            pantallaHeladera = new PantallaHeladera();
            pantallaHeladera.show();
        }
    }

    public void cerrarHeladera() {
        if (heladeraAbierta) {
            heladeraAbierta = false;
            if (pantallaHeladera != null) {
                pantallaHeladera.hide();
                pantallaHeladera = null;
            }
        }
    }

    public void abrirMesa() {
        if (!mesaAbierta) {
            mesaAbierta = true;
            pantallaMesa = PantallaMesa.getInstancia();
            pantallaMesa.show();
        }
    }

    public void cerrarMesa() {
        if (mesaAbierta) {
            mesaAbierta = false;
            if (pantallaMesa != null) {
                pantallaMesa.hide();
            }
        }
    }

    public void renderOverlays(float delta, SpriteBatch batch) {
        if (heladeraAbierta && pantallaHeladera != null) {
            pantallaHeladera.render(delta);
        }

        if (mesaAbierta && pantallaMesa != null) {
            pantallaMesa.render(delta);
        }

        if (juegoEnPausa) {
            pantallaPausa.render(delta);
        }
    }

    public void dispose() {
        if (pantallaPausa != null) {
            pantallaPausa.dispose();
        }
    }

    public void resetearMesa() {
        if (pantallaMesa != null) {
            pantallaMesa.resetearInstancia();
        }
    }

    // Getters
    public boolean isJuegoEnPausa() {
        return juegoEnPausa;
    }

    public boolean isHeladeraAbierta() {
        return heladeraAbierta;
    }

    public boolean isMesaAbierta() {
        return mesaAbierta;
    }

    public boolean hayOverlayActivo() {
        return juegoEnPausa || heladeraAbierta || mesaAbierta;
    }
}
