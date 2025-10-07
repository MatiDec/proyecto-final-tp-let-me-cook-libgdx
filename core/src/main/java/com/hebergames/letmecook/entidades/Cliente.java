package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.maquinas.CajaRegistradora;
import com.hebergames.letmecook.maquinas.MesaRetiro;
import com.hebergames.letmecook.utiles.Render;

public class Cliente {

    private static int siguienteId = 1;
    private final int ID_CLIENTE;
    private final TipoCliente tipo;

    protected float tiempoTolerancia;
    protected float tiempoActual;
    protected boolean activo;
    protected Rectangle ubicacion;
    protected SpriteBatch BATCH;
    protected boolean pedidoAsignado;
    private CajaRegistradora cajaAsignada;
    private MesaRetiro mesaRetiroAsignada;

    //cliente presencial
    private boolean visible;
    private boolean recienAparecido;
    private TextureRegion texturaCliente;

    //cliente virtual
    private TextureRegion texturaInactiva;
    private TextureRegion texturaActiva;
    private TextureRegion texturaActual;
    private boolean nuevo;

    public Cliente(float tiempoTolerancia, Rectangle ubicacion, TextureRegion textura) {
        this.ID_CLIENTE = siguienteId++;
        this.tipo = TipoCliente.PRESENCIAL;
        this.tiempoTolerancia = tiempoTolerancia;
        this.tiempoActual = 0f;
        this.activo = true;
        this.ubicacion = ubicacion;
        this.BATCH = Render.batch;
        this.pedidoAsignado = false;

        this.texturaCliente = textura;
        this.visible = false;
        this.recienAparecido = false;
    }

    //si le pasa texturas es q es virtual, esto es ineficiente pero por ahora lo dejo acá como un checkpoint
    public Cliente(float tiempoTolerancia, Rectangle ubicacion,
                   TextureRegion texturaInactiva, TextureRegion texturaActiva) {
        this.ID_CLIENTE = siguienteId++;
        this.tipo = TipoCliente.VIRTUAL;
        this.tiempoTolerancia = tiempoTolerancia;
        this.tiempoActual = 0f;
        this.activo = true;
        this.ubicacion = ubicacion;
        this.BATCH = Render.batch;
        this.pedidoAsignado = false;

        this.texturaInactiva = texturaInactiva;
        this.texturaActiva = texturaActiva;
        this.texturaActual = texturaInactiva;
        this.nuevo = false;
    }

    public void actualizar(float delta) {
        if (!activo) return;

        this.tiempoActual += delta;

        // Si se agotó el tiempo de tolerancia, desaparece
        if (this.tiempoActual >= this.tiempoTolerancia) {
            desaparecer();
        }
    }

    public void dibujar(SpriteBatch batch) {
        if (tipo == TipoCliente.PRESENCIAL) {
            if (this.visible && this.activo && this.texturaCliente != null) {
                batch.draw(texturaCliente, ubicacion.x, ubicacion.y, ubicacion.width, ubicacion.height);
            }
        } else { // VIRTUAL
            if (this.texturaActual != null) {
                batch.draw(texturaActual, ubicacion.x, ubicacion.y, ubicacion.width, ubicacion.height);
            }
        }
    }

    public void aparecer() {
        if (tipo == TipoCliente.PRESENCIAL) {
            this.visible = true;
            this.activo = true;
            this.tiempoActual = 0f;
            this.recienAparecido = true;
            this.pedidoAsignado = false;
        } else { // VIRTUAL
            this.nuevo = true;
            this.texturaActual = texturaActiva;
            this.activo = true;
            this.tiempoActual = 0f;
        }
    }

    public void desaparecer() {
        if (tipo == TipoCliente.PRESENCIAL) {
            this.visible = false;
            this.activo = false;
        } else { // VIRTUAL
            this.texturaActual = texturaInactiva;
            this.activo = false;
        }
    }

    public boolean acabaDeAparecer() {
        if (tipo == TipoCliente.PRESENCIAL) {
            return this.recienAparecido && !isPedidoAsignado();
        } else { // VIRTUAL
            if (this.nuevo) {
                this.nuevo = false;
                return true;
            }
            return false;
        }
    }

    public void setCajaAsignada(CajaRegistradora caja) {
        this.cajaAsignada = caja;
    }

    public CajaRegistradora getCajaAsignada() {
        return cajaAsignada;
    }

    public void setMesaRetiroAsignada(MesaRetiro mesa) {
        this.mesaRetiroAsignada = mesa;
    }

    public MesaRetiro getMesaRetiroAsignada() {
        return mesaRetiroAsignada;
    }

    public void moverAMesaRetiro(Rectangle ubicacionMesa) {
        this.ubicacion = ubicacionMesa;
    }

    public void setRecienAparecido(boolean recienAparecido) {
        if (tipo == TipoCliente.PRESENCIAL) {
            this.recienAparecido = recienAparecido;
        }
    }

    public boolean isVisible() {
        if (tipo == TipoCliente.PRESENCIAL) {
            return this.visible;
        }
        return false;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public Rectangle getUbicacion() {
        return ubicacion;
    }

    public float getTiempoRestante() {
        return Math.max(0, tiempoTolerancia - tiempoActual);
    }

    public int getIdCliente() {
        return this.ID_CLIENTE;
    }

    public boolean isPedidoAsignado() {
        return this.pedidoAsignado;
    }

    public void setPedidoAsignado(boolean pedidoAsignado) {
        this.pedidoAsignado = pedidoAsignado;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

}
