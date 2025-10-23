package com.hebergames.letmecook.entidades.clientes;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;
import com.hebergames.letmecook.pedidos.EstadoPedido;
import com.hebergames.letmecook.pedidos.Pedido;
import com.hebergames.letmecook.utiles.GestorTexturas;

import java.util.ArrayList;

public class Cliente {
    private int id;
    private Pedido pedido;
    private EstacionTrabajo estacionAsignada;
    private float tiempoEspera;
    private float tiempoMaximoEspera;
    private VisualizadorCliente visualizador;
    private static int contadorId = 0;
    private float tiempoEsperaEnCaja; // Tiempo que espera sin ser atendido en caja
    private static final float TIEMPO_MAX_ESPERA_CAJA = 30f; // Tiempo antes de irse sin ser atendido
    private TipoCliente tipoCliente;

    public Cliente(ArrayList<Producto> productosSolicitados, float tiempoMaximoEspera, TipoCliente tipo) {
        this.id = contadorId++;
        this.pedido = new Pedido(id, productosSolicitados);
        this.tiempoMaximoEspera = tiempoMaximoEspera;
        this.tiempoEspera = 0f;
        this.visualizador = null;
        this.tiempoEsperaEnCaja = 0f;
        this.tipoCliente = tipo;
    }

    public void actualizar(float delta) {
        EstadoPedido estado = pedido.getEstadoPedido();

        // Si está esperando en caja sin atender
        if (estado == EstadoPedido.EN_ESPERA) {
            tiempoEsperaEnCaja += delta;
        }

        // Si está en preparación
        if (estado == EstadoPedido.EN_PREPARACION) {
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
        if (visualizador != null && estacionAsignada != null &&
            pedido != null && pedido.getEstadoPedido() != EstadoPedido.CANCELADO) {
            visualizador.dibujar(batch, this);
        }
    }

    public void liberarRecursos() {
        this.visualizador = null;
        this.pedido = null;
        this.estacionAsignada = null;
    }

    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    public boolean esVirtual() {
        return tipoCliente == TipoCliente.VIRTUAL;
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

    public Pedido getPedido() {
        return pedido;
    }

    public EstacionTrabajo getEstacionAsignada() {
        return estacionAsignada;
    }

    public void setEstacionAsignada(EstacionTrabajo estacion) {
        this.estacionAsignada = estacion;
    }

    public boolean haExpiradoTiempoCaja() {
        return tiempoEsperaEnCaja >= TIEMPO_MAX_ESPERA_CAJA;
    }

    public float getPorcentajeTiempoCaja() {
        return tiempoEsperaEnCaja / TIEMPO_MAX_ESPERA_CAJA;
    }

    public float getPorcentajeToleranciaActual() {
        if (pedido.getEstadoPedido() == EstadoPedido.EN_ESPERA) {
            // En caja: tolerancia disminuye con el tiempo en caja
            return 1f - getPorcentajeTiempoCaja();
        } else if (pedido.getEstadoPedido() == EstadoPedido.EN_PREPARACION) {
            // En preparación: tolerancia disminuye con el tiempo de espera
            return 1f - getPorcentajeTiempo();
        }
        return 1f;
    }

    public void resetearTiempo() {
        this.tiempoEspera = 0f;
    }
}
