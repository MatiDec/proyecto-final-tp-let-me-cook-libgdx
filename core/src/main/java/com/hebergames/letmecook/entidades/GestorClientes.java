package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entregables.productos.GestorProductos;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.pedidos.Pedido;

import java.util.ArrayList;
import java.util.Random;

public class GestorClientes {

    private ArrayList<Pedido> pedidosActivos;

    private ArrayList<Cliente> clientes;
    private ArrayList<Rectangle> ubicacionesClientes;
    private TextureRegion texturaClientePresencial;
    private TextureRegion texturaVirtualInactiva;
    private TextureRegion texturaVirtualActiva;

    private float tiempoSpawn;
    private float intervalosSpawn;
    private float tiempoToleranciaCliente;
    private int maxClientesSimultaneos;
    private Random random;

    private GestorProductos gestorProductos;

    public GestorClientes(ArrayList<Rectangle> ubicacionesClientes,
                          TextureRegion texturaClientePresencial,
                          TextureRegion texturaVirtualInactiva,
                          TextureRegion texturaVirtualActiva) {
        this.ubicacionesClientes = new ArrayList<>(ubicacionesClientes);
        this.texturaClientePresencial = texturaClientePresencial;
        this.texturaVirtualInactiva = texturaVirtualInactiva;
        this.texturaVirtualActiva = texturaVirtualActiva;

        this.clientes = new ArrayList<>();
        this.pedidosActivos = new ArrayList<>();
        this.gestorProductos = new GestorProductos();
        this.tiempoSpawn = 0f;
        this.intervalosSpawn = 5f;
        this.tiempoToleranciaCliente = 15f;
        this.maxClientesSimultaneos = 3;
        this.random = new Random();
    }

    public void actualizar(float delta) {
        // Actualizar clientes existentes
        for (int i = clientes.size() - 1; i >= 0; i--) {
            Cliente cliente = clientes.get(i);
            cliente.actualizar(delta);

            if(cliente.isActivo() && !yaTienePedido(cliente) && cliente.acabaDeAparecer()) {
                asignarPedido(cliente);
            }

            // Remover clientes inactivos
            if (!cliente.isActivo()) {
                removerPedido(cliente);
                clientes.remove(i);
            }
        }

        // Manejar spawn de nuevos clientes
        tiempoSpawn += delta;
        if (tiempoSpawn >= intervalosSpawn && clientes.size() < maxClientesSimultaneos) {
            spawnearCliente();
            tiempoSpawn = 0f;
        }
    }

    private void spawnearCliente() {
        if (ubicacionesClientes.isEmpty()) return;

        // Buscar ubicación libre
        ArrayList<Rectangle> ubicacionesLibres = new ArrayList<>();
        for (Rectangle ubicacion : ubicacionesClientes) {
            boolean ocupada = false;
            for (Cliente cliente : clientes) {
                if (cliente.getUbicacion().equals(ubicacion)) {
                    ocupada = true;
                    break;
                }
            }
            if (!ocupada) {
                ubicacionesLibres.add(ubicacion);
            }
        }

        if (ubicacionesLibres.isEmpty()) return;

        // Seleccionar ubicación aleatoria
        Rectangle ubicacionSeleccionada = ubicacionesLibres.get(random.nextInt(ubicacionesLibres.size()));

        // Decidir tipo de cliente (70% presencial, 30% virtual)
        Cliente nuevoCliente;
        if (random.nextFloat() < 0.7f) {
            // Crear cliente presencial usando el constructor correspondiente
            nuevoCliente = new Cliente(tiempoToleranciaCliente, ubicacionSeleccionada, texturaClientePresencial);
        } else {
            // Crear cliente virtual usando el constructor correspondiente
            nuevoCliente = new Cliente(tiempoToleranciaCliente, ubicacionSeleccionada,
                texturaVirtualInactiva, texturaVirtualActiva);
        }

        nuevoCliente.aparecer();
        clientes.add(nuevoCliente);
    }

    private void asignarPedido(Cliente cliente) {
        if(cliente.isPedidoAsignado() || yaTienePedido(cliente)) {
            return;
        }

        Producto producto = gestorProductos.obtenerProductoAleatorio();
        if(producto == null) return;

        Pedido nuevoPedido = new Pedido(cliente.getIdCliente(), producto);
        this.pedidosActivos.add(nuevoPedido);

        cliente.setPedidoAsignado(true);

        // Si es presencial, marcar que ya no es recién aparecido
        if(cliente.getTipo() == TipoCliente.PRESENCIAL) {
            cliente.setRecienAparecido(false);
        }
    }

    private boolean yaTienePedido(Cliente cliente) {
        for (Pedido pedido : pedidosActivos) {
            if(pedido.getIdClienteSolicitante() == cliente.getIdCliente()) return true;
        }
        return false;
    }

    private void removerPedido(Cliente cliente) {
        pedidosActivos.removeIf(p -> p.getIdClienteSolicitante() == cliente.getIdCliente());
    }

    public void dibujar(SpriteBatch batch) {
        for (Cliente cliente : clientes) {
            cliente.dibujar(batch);
        }
    }

    // Métodos para configurar parámetros
    public void setIntervalosSpawn(float intervalos) {
        this.intervalosSpawn = intervalos;
    }

    public void setTiempoToleranciaCliente(float tiempo) {
        this.tiempoToleranciaCliente = tiempo;
    }

    public void setMaxClientesSimultaneos(int max) {
        this.maxClientesSimultaneos = max;
    }

    public ArrayList<Cliente> getClientes() {
        return new ArrayList<>(clientes);
    }

    public int getClientesActivos() {
        return clientes.size();
    }

    public void limpiarClientes() {
        clientes.clear();
    }

    public ArrayList<Pedido> getPedidosActivos() {
        return this.pedidosActivos;
    }
}
