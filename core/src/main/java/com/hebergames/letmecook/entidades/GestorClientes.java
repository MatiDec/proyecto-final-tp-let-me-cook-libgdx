package com.hebergames.letmecook.entidades;

import com.hebergames.letmecook.entregables.productos.CategoriaProducto;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.mapa.TurnoTrabajo;
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
    private TurnoTrabajo turnoActual;
    private int ultimaCantidadClientes = 0;


    public GestorClientes(ArrayList<CajaRegistradora> cajas, ArrayList<Producto> productos, float intervaloSpawn, TurnoTrabajo turno) {
        this.clientesActivos = new ArrayList<>();
        this.cajasDisponibles = cajas;
        this.productosDisponibles = productos;
        this.intervaloSpawn = intervaloSpawn;
        this.tiempoParaSiguienteCliente = intervaloSpawn;
        this.random = new Random();
        this.turnoActual = turno;
    }

    public void actualizar(float delta) {
        for (int i = clientesActivos.size() - 1; i >= 0; i--) {
            Cliente cliente = clientesActivos.get(i);
            cliente.actualizar(delta);

            EstadoPedido estado = cliente.getPedido().getEstadoPedido();

            if (estado == EstadoPedido.COMPLETADO) {
                liberarEstacion(cliente);
                clientesActivos.remove(i);
            } else if (cliente.haExpiradoTiempo() && estado == EstadoPedido.EN_PREPARACION) {

                cliente.getPedido().setEstadoPedido(EstadoPedido.CANCELADO);
                liberarEstacion(cliente);
                clientesActivos.remove(i);

            }
        }


        tiempoParaSiguienteCliente -= delta;
        if (tiempoParaSiguienteCliente <= 0) {
            generarNuevoCliente();
            tiempoParaSiguienteCliente = intervaloSpawn;
        }
    }

    private void generarNuevoCliente() {
        CajaRegistradora cajaLibre = buscarCajaLibre();
        if (cajaLibre != null && !productosDisponibles.isEmpty()) {
            ArrayList<Producto> productosFiltrados = new ArrayList<>();
            CategoriaProducto categoriaActual = turnoActual.getCategoriaProductos();

            for (Producto p : productosDisponibles) {
                if (p.getCategoria() == categoriaActual) {
                    productosFiltrados.add(p);
                }
            }

            if (productosFiltrados.isEmpty()) {
                productosFiltrados = productosDisponibles;
            }

            Producto productoAleatorio = productosFiltrados.get(
                random.nextInt(productosFiltrados.size())
            );

            float tiempoMaximo = 60f + random.nextFloat() * 30f;
            Cliente nuevoCliente = new Cliente(productoAleatorio, tiempoMaximo);
            nuevoCliente.inicializarVisualizador();
            nuevoCliente.setEstacionAsignada(cajaLibre);

            cajaLibre.asignarCliente(nuevoCliente);
            clientesActivos.add(nuevoCliente);
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
        cliente.setEstacionAsignada(null);
        cliente.liberarRecursos();
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

    public int getUltimaCantidadClientes() {
        return ultimaCantidadClientes;
    }

    public void actualizarUltimaCantidadClientes() {
        this.ultimaCantidadClientes = clientesActivos.size();
    }

    public void limpiar() {
        for (Cliente cliente : new ArrayList<>(clientesActivos)) {
            liberarEstacion(cliente);
        }
        clientesActivos.clear();
        tiempoParaSiguienteCliente = intervaloSpawn;
    }


}
