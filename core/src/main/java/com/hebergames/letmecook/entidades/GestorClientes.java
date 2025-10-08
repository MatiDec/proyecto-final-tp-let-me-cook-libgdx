package com.hebergames.letmecook.entidades;

import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.maquinas.CajaRegistradora;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;
import com.hebergames.letmecook.maquinas.MesaRetiro;
import com.hebergames.letmecook.pedidos.EstadoPedido;

import java.util.ArrayList;
import java.util.Random;

public class GestorClientes {
    private ArrayList<Cliente> clientesActivos;
    private ArrayList<CajaRegistradora> cajasDisponibles;
    private float tiempoParaSiguienteCliente;
    private float intervaloSpawn;
    private Random random;
    private ArrayList<Producto> productosDisponibles;

    public GestorClientes(ArrayList<CajaRegistradora> cajas, ArrayList<Producto> productos, float intervaloSpawn) {
        this.clientesActivos = new ArrayList<>();
        this.cajasDisponibles = cajas;
        this.productosDisponibles = productos;
        this.intervaloSpawn = intervaloSpawn;
        this.tiempoParaSiguienteCliente = intervaloSpawn;
        this.random = new Random();
    }

    public void actualizar(float delta) {
        // Actualizar clientes existentes
        for (int i = clientesActivos.size() - 1; i >= 0; i--) {
            Cliente cliente = clientesActivos.get(i);
            cliente.actualizar(delta);

            // Si el tiempo expiro, cancelar pedido
            if (cliente.haExpiradoTiempo() &&
                    cliente.getPedido().getEstadoPedido() == EstadoPedido.EN_PREPARACION) {
                cliente.getPedido().setEstadoPedido(EstadoPedido.CANCELADO);
                liberarEstacion(cliente);
                clientesActivos.remove(i);
                // Aquí se puede restar puntos
            }
        }

        // Spawn de nuevos clientes
        tiempoParaSiguienteCliente -= delta;
        if (tiempoParaSiguienteCliente <= 0) {
            generarNuevoCliente();
            tiempoParaSiguienteCliente = intervaloSpawn;
        }
    }

    private void generarNuevoCliente() {
        CajaRegistradora cajaLibre = buscarCajaLibre();
        if (cajaLibre != null && !productosDisponibles.isEmpty()) {
            Producto productoAleatorio = productosDisponibles.get(
                    random.nextInt(productosDisponibles.size())
            );

            float tiempoMaximo = 60f + random.nextFloat() * 30f; // 60-90 segundos
            Cliente nuevoCliente = new Cliente(productoAleatorio, tiempoMaximo);
            nuevoCliente.setEstacionAsignada(cajaLibre);

            cajaLibre.asignarCliente(nuevoCliente);
            clientesActivos.add(nuevoCliente);

            System.out.println("Nuevo cliente generado en caja. Producto: " +
                    productoAleatorio.getNombre());
        }
    }

    private CajaRegistradora buscarCajaLibre() {
        for (CajaRegistradora caja : cajasDisponibles) {
            if (!caja.tieneCliente()) {
                return caja;
            }
        }
        return null;
    }

    private void liberarEstacion(Cliente cliente) {
        EstacionTrabajo estacion = cliente.getEstacionAsignada();
        if (estacion instanceof CajaRegistradora) {
            ((CajaRegistradora) estacion).liberarCliente();
        } else if (estacion instanceof MesaRetiro) {
            ((MesaRetiro) estacion).liberarCliente();
        }
    }

    public void removerCliente(Cliente cliente) {
        clientesActivos.remove(cliente);
        liberarEstacion(cliente);
    }

    public ArrayList<Cliente> getClientesActivos() {
        return clientesActivos;
    }

    public ArrayList<Cliente> getClientesEnPreparacion() {
        ArrayList<Cliente> enPreparacion = new ArrayList<>();
        for (Cliente cliente : clientesActivos) {
            if (cliente.getPedido().getEstadoPedido() == EstadoPedido.EN_PREPARACION) {
                enPreparacion.add(cliente);
            }
        }
        return enPreparacion;
    }
}
