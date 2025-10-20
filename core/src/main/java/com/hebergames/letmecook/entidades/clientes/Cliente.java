package com.hebergames.letmecook.entidades.clientes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;
import com.hebergames.letmecook.pedidos.EstadoPedido;
import com.hebergames.letmecook.pedidos.Pedido;
import com.hebergames.letmecook.utiles.GestorTexturas;

public class Cliente {
    private int id;
    private Pedido pedido;
    private EstacionTrabajo estacionAsignada;
    private float tiempoEspera;
    private float tiempoMaximoEspera;
    private VisualizadorCliente visualizador;
    private static int contadorId = 0;

    public Cliente(Producto productoSolicitado, float tiempoMaximoEspera) {
        this.id = contadorId++;
        this.pedido = new Pedido(id, productoSolicitado);
        this.tiempoMaximoEspera = tiempoMaximoEspera;
        this.tiempoEspera = 0f;
        this.visualizador = null;
    }

    public void actualizar(float delta) {
        if (pedido.getEstadoPedido() == EstadoPedido.EN_PREPARACION) {
            tiempoEspera += delta;
        }
    }

    public void inicializarVisualizador() {
        if (visualizador == null && GestorTexturas.getInstance().estanTexturasListas()) {
            TextureRegion textura = GestorTexturas.getInstance().getTexturaCliente();
            if (textura != null) {
                this.visualizador = new VisualizadorCliente(textura);
            }
        }
    }

    public void dibujar(SpriteBatch batch) {
        if (visualizador != null && estacionAsignada != null) {
            visualizador.dibujar(batch, this);
        }
    }

    public void liberarRecursos() {
        this.visualizador = null;
        this.pedido = null;
    }


    public boolean haExpiradoTiempo() {
        return tiempoEspera >= tiempoMaximoEspera;
    }

    public float getTiempoRestante() {
        return Math.max(0, tiempoMaximoEspera - tiempoEspera);
    }

    public float getPorcentajeTiempo() {
        return tiempoEspera / tiempoMaximoEspera;
    }

    public int getId() {
        return id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public EstacionTrabajo getEstacionAsignada() {
        return estacionAsignada;
    }

    public void setEstacionAsignada(EstacionTrabajo estacion) {
        this.estacionAsignada = estacion;
    }

    public void resetearTiempo() {
        this.tiempoEspera = 0f;
    }
}
