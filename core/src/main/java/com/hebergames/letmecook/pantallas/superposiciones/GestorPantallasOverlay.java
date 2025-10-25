package com.hebergames.letmecook.pantallas.superposiciones;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.hebergames.letmecook.sonido.GestorAudio;

public class GestorPantallasOverlay {

    private PantallaPausa pantallaPausa;
    private PantallaCalendario pantallaCalendario;
    private boolean calendarioVisible;
    private boolean juegoEnPausa;
    private GestorMostrarCalendario gestorMostrarCalendario;
    private boolean calendarioMostradoAutomaticamente;

    private final GestorAudio gestorAudio;

    public GestorPantallasOverlay(PantallaPausa pantallaPausa, PantallaCalendario pantallaCalendario,
                                  GestorAudio gestorAudio, GestorMostrarCalendario gestorMostrarCalendario) {
        this.pantallaPausa = pantallaPausa;
        this.pantallaCalendario = pantallaCalendario;
        this.gestorAudio = gestorAudio;
        this.gestorMostrarCalendario = gestorMostrarCalendario;
        this.juegoEnPausa = false;
        this.calendarioVisible = false;
        this.calendarioMostradoAutomaticamente = false;
    }

    public void toggleCalendario() {
        // Si está en modo automático, no permitir toggle manual
        if (calendarioMostradoAutomaticamente) {
            return;
        }

        calendarioVisible = !calendarioVisible;

        if (calendarioVisible) {
            if (juegoEnPausa) {
                juegoEnPausa = false;
            }
            pantallaCalendario.show();
            gestorAudio.pausarMusica();
        } else {
            gestorAudio.reanudarMusica();
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

    public void mostrarCalendarioInicial() {
        if (!calendarioVisible) {
            calendarioVisible = true;
            calendarioMostradoAutomaticamente = true;
            pantallaCalendario.show();
            gestorAudio.pausarMusica();
        }
    }

    public void cerrarCalendarioAutomatico() {
        if (calendarioMostradoAutomaticamente) {
            calendarioVisible = false;
            calendarioMostradoAutomaticamente = false;
            gestorAudio.reanudarMusica();
        }
    }

    public boolean isCalendarioMostradoAutomaticamente() {
        return calendarioMostradoAutomaticamente;
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
