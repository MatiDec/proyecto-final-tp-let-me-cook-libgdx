package com.hebergames.letmecook.pedidos;

public class PedidoConTiempo {
    private Pedido pedido;
    private float tiempoRestante;
    private float tiempoMaximo;
    private boolean activo;

    public PedidoConTiempo(Pedido pedido, float tiempoMaximo) {
        this.pedido = pedido;
        this.tiempoMaximo = tiempoMaximo;
        this.tiempoRestante = tiempoMaximo;
        this.activo = true;
    }

    public void actualizar(float delta) {
        if (activo) {
            tiempoRestante -= delta;
            if (tiempoRestante <= 0) {
                activo = false;
            }
        }
    }

    public Pedido getPedido() {
        return pedido;
    }

    public float getTiempoRestante() {
        return Math.max(0, tiempoRestante);
    }

    public float getPorcentajeTiempo() {
        return tiempoRestante / tiempoMaximo;
    }

    public boolean isActivo() {
        return activo;
    }

    public void desactivar() {
        this.activo = false;
    }
}
