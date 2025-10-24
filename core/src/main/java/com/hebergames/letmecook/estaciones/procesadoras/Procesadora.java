package com.hebergames.letmecook.estaciones.procesadoras;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entregables.ingredientes.CoccionListener;
import com.hebergames.letmecook.entregables.ingredientes.EstadoCoccion;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.sonido.GestorAudio;
import com.hebergames.letmecook.utiles.Recursos;

public class Procesadora implements MaquinaProcesadora, CoccionListener {

    private static int contadorInstancias = 0;
    private final int idInstancia;

    private Ingrediente ingredienteCocinando;
    private boolean procesando = false;
    private float tiempoSonidoTemporizador = 0f;
    private static final float INTERVALO_SONIDO_TEMPORIZADOR = 1f;

    private Texto indicadorEstado;
    private Rectangle area;

    private TipoCoccion tipoCoccion;

    public Procesadora(Rectangle area, TipoCoccion tipo) {
        this.idInstancia = ++contadorInstancias;
        this.area = area;
        this.tipoCoccion = tipo;
        inicializarIndicador();
        System.out.println("DEBUG: Creado Procesador #" + idInstancia + " tipo: " + tipo);
    }

    private void inicializarIndicador() {
        indicadorEstado = new Texto(Recursos.FUENTE_MENU, 16, Color.WHITE, true);
        float centroX = area.x + area.width / 2f;
        float arribaY = area.y + area.height + 10f;
        indicadorEstado.setPosition(centroX, arribaY);
    }

    @Override
    public boolean puedeIniciarProceso() {
        return !procesando;
    }

    @Override
    public boolean iniciarProceso(Ingrediente ingrediente) {

        if (!puedeIniciarProceso() || !ingrediente.esCocinableInterna()
            || ingrediente.estaQuemado()) {
            return false;
        }

        ingredienteCocinando = ingrediente;
        ingredienteCocinando.setCoccionListener(this);
        procesando = true;

        return true;
    }

    @Override
    public void actualizarProceso(float delta) {
        if (!procesando || ingredienteCocinando == null) return;

        ingredienteCocinando.actualizarCoccion(delta);

        tiempoSonidoTemporizador += delta;
        if (tiempoSonidoTemporizador >= INTERVALO_SONIDO_TEMPORIZADOR) {
            tiempoSonidoTemporizador = 0f;
            System.out.println(tipoCoccion.getAccionRealizada() + " - Estado:" +
                ingredienteCocinando.getEstadoCoccion().getESTADO());
        }

        if (ingredienteCocinando.estaQuemado()) {
            procesando = false;
        }
    }

    @Override
    public boolean tieneProcesandose() {
        return procesando && ingredienteCocinando != null;
    }

    @Override
    public Ingrediente obtenerResultado() {
        if (!tieneProcesandose()) return null;

        Ingrediente resultado = ingredienteCocinando;
        ingredienteCocinando.setCoccionListener(null); // Limpiar listener
        ingredienteCocinando = null;
        procesando = false;
        tiempoSonidoTemporizador = 0f;

        return resultado;
    }

    @Override
    public void dibujarIndicador(SpriteBatch batch) {
        // Solo debug cuando hay algo importante que reportar
        if (procesando || ingredienteCocinando != null) {
            System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - dibujarIndicador - procesando: " + procesando +
                ", ingrediente: " + (ingredienteCocinando != null ? ingredienteCocinando.getNombre() : "null"));
        }

        String texto;
        if (procesando && ingredienteCocinando != null) {
            EstadoCoccion estado = ingredienteCocinando.getEstadoCoccion();

            if (estado == EstadoCoccion.MAL_HECHO) {
                texto = tipoCoccion.getAccionRealizada();
            } else {
                texto = estado.getESTADO();
            }

            System.out.println("DEBUG: HornoProcesador #" + idInstancia + " - Mostrando: " + texto);
        } else {
            texto = tipoCoccion.name() + " Libre";
        }

        indicadorEstado.setTexto(texto);
        indicadorEstado.dibujarEnUi(batch);
    }

    @Override
    public void detenerProceso() {
        procesando = false;
        if (ingredienteCocinando != null) {
            ingredienteCocinando.setCoccionListener(null);
            ingredienteCocinando = null;
        }
    }

    @Override
    public void onCambioEstado(EstadoCoccion nuevoEstado) {
        System.out.println("Estado de cocción cambió a: " + nuevoEstado.getESTADO());

        switch (nuevoEstado) {
            case BIEN_HECHO:
                GestorAudio.getInstance().reproducirSonido("coccion_perfecta");
                break;
        }
    }

    @Override
    public void onIngredienteQuemado() {
        System.out.println("¡Ingrediente quemado!");
        procesando = false;
    }
}
