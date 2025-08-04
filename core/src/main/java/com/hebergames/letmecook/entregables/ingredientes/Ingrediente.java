package com.hebergames.letmecook.entregables.ingredientes;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;

public abstract class Ingrediente implements ObjetoAlmacenable {
    protected String nombre;
    protected TextureRegion textura;
    protected boolean esCocinableInterna;

    // Variables para el sistema de cocción
    protected EstadoCoccion estadoCoccion;
    protected float tiempoCoccionActual;
    protected float tiempoCoccionMinimo;
    protected float tiempoCoccionMaximo;
    protected CoccionListener coccionListener;

    public Ingrediente(String nombre, TextureRegion textura) {
        this(nombre, textura, false, 0f, 0f);
    }

    public Ingrediente(String nombre, TextureRegion textura, boolean esCocinableInterna, float tiempoCoccionMinimo, float tiempoCoccionMaximo) {
        this.nombre = nombre;
        this.textura = textura;
        this.esCocinableInterna = esCocinableInterna;
        this.estadoCoccion = EstadoCoccion.CRUDO;
        this.tiempoCoccionActual = 0f;
        this.tiempoCoccionMinimo = tiempoCoccionMinimo;
        this.tiempoCoccionMaximo = tiempoCoccionMaximo;
    }

    public void actualizarCoccion(float delta) {
        if (!esCocinableInterna) return;

        tiempoCoccionActual += delta;
        EstadoCoccion estadoAnterior = estadoCoccion;

        // Determinar nuevo estado basado en tiempo de cocción
        if (tiempoCoccionActual >= tiempoCoccionMaximo) {
            estadoCoccion = EstadoCoccion.QUEMADO;
            if (coccionListener != null && estadoAnterior != EstadoCoccion.QUEMADO) {
                coccionListener.onIngredienteQuemado();
            }
        } else if (tiempoCoccionActual >= tiempoCoccionMinimo) {
            estadoCoccion = EstadoCoccion.COCIDO;
        }

        // Notificar cambio de estado
        if (estadoAnterior != estadoCoccion && coccionListener != null) {
            coccionListener.onCambioEstado(estadoCoccion);
        }
    }

    public void setCoccionListener(CoccionListener listener) {
        this.coccionListener = listener;
    }

    public boolean esCocinableInterna() {
        return esCocinableInterna;
    }

    public EstadoCoccion getEstadoCoccion() {
        return estadoCoccion;
    }

    public float getTiempoCoccionActual() {
        return tiempoCoccionActual;
    }

    public float getTiempoCoccionMinimo() {
        return tiempoCoccionMinimo;
    }

    public float getTiempoCoccionMaximo() {
        return tiempoCoccionMaximo;
    }

    public boolean estaQuemado() {
        return estadoCoccion == EstadoCoccion.QUEMADO;
    }

    public boolean estaBienCocinado() {
        return estadoCoccion == EstadoCoccion.COCIDO;
    }

    @Override
    public String getNombre() {
        if (esCocinableInterna && estadoCoccion != EstadoCoccion.CRUDO) {
            return nombre + " (" + estadoCoccion.getNombre() + ")";
        }
        return nombre;
    }

    public TextureRegion getTextura() { return textura; }
}
