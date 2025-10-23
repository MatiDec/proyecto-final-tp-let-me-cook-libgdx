package com.hebergames.letmecook.entidades.clientes;

import com.hebergames.letmecook.entregables.productos.CategoriaProducto;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.mapa.niveles.TurnoTrabajo;
import com.hebergames.letmecook.estaciones.CajaRegistradora;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;
import com.hebergames.letmecook.estaciones.MesaRetiro;
import com.hebergames.letmecook.pedidos.CallbackPenalizacion;
import com.hebergames.letmecook.pedidos.EstadoPedido;
import com.hebergames.letmecook.pedidos.Pedido;

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
    private CallbackPenalizacion callbackPenalizacion;
    private int ultimaCantidadClientes = 0;
    private int clientesAtendidos; // Clientes que completaron su pedido
    private int clientesPerdidos; // Clientes que se fueron sin atender o por timeout
    private static final int MAX_CLIENTES_SIMULTANEOS = 5; // Máximo de clientes al mismo tiempo
    private static final int MAX_CLIENTES_TOTALES = 15; // Total de clientes antes de cambiar nivel


    public GestorClientes(ArrayList<CajaRegistradora> cajas, ArrayList<Producto> productos, float intervaloSpawn, TurnoTrabajo turno) {
        this.clientesActivos = new ArrayList<>();
        this.cajasDisponibles = cajas;
        this.productosDisponibles = productos;
        this.intervaloSpawn = intervaloSpawn;
        this.tiempoParaSiguienteCliente = intervaloSpawn;
        this.random = new Random();
        this.turnoActual = turno;
        this.clientesAtendidos = 0;
        this.clientesPerdidos = 0;
    }

    public void setCallbackPenalizacion(CallbackPenalizacion callback) {
        this.callbackPenalizacion = callback;
    }

    public void actualizar(float delta) {
        for (int i = clientesActivos.size() - 1; i >= 0; i--) {
            Cliente cliente = clientesActivos.get(i);
            cliente.actualizar(delta);

            EstadoPedido estado = cliente.getPedido().getEstadoPedido();

            if (estado == EstadoPedido.COMPLETADO) {
                liberarEstacion(cliente);
                clientesActivos.remove(i);
                clientesAtendidos++; // Incrementar clientes atendidos
            } else if (cliente.haExpiradoTiempo() && estado == EstadoPedido.EN_PREPARACION) {
                // Penalización por timeout en preparación
                aplicarPenalizacion(-50, "Cliente se fue por timeout en preparación");
                cliente.getPedido().setEstadoPedido(EstadoPedido.CANCELADO);
                liberarEstacion(cliente);
                clientesActivos.remove(i);
                clientesPerdidos++; // Incrementar clientes perdidos
            } else if (cliente.haExpiradoTiempoCaja() && estado == EstadoPedido.EN_ESPERA) {
                // Penalización por no atender al cliente en caja
                aplicarPenalizacion(-30, "Cliente se fue sin ser atendido");
                cliente.getPedido().setEstadoPedido(EstadoPedido.CANCELADO);
                liberarEstacion(cliente);
                clientesActivos.remove(i);
                clientesPerdidos++; // Incrementar clientes perdidos
            }
        }


        tiempoParaSiguienteCliente -= delta;
        if (tiempoParaSiguienteCliente <= 0) {
            generarNuevoCliente();
            tiempoParaSiguienteCliente = intervaloSpawn;
        }
    }

    private void generarNuevoCliente() {
        // Verificar límites de clientes
        if (clientesActivos.size() >= MAX_CLIENTES_SIMULTANEOS) {
            return; // No generar más clientes si ya hay demasiados simultáneos
        }

        int totalClientesProcesados = clientesAtendidos + clientesPerdidos;
        if (totalClientesProcesados >= MAX_CLIENTES_TOTALES) {
            return; // No generar más clientes, nivel debe terminar
        }
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

            int cantidadProductos = Pedido.getCantidadProductosAleatorios(random);
            ArrayList<Producto> productosDelPedido = new ArrayList<>();

            for (int i = 0; i < cantidadProductos; i++) {
                Producto productoAleatorio = productosFiltrados.get(random.nextInt(productosFiltrados.size()));
                productosDelPedido.add(productoAleatorio);
            }

            float tiempoMaximo = 60f + random.nextFloat() * 30f;
            Cliente nuevoCliente = new Cliente(productosDelPedido, tiempoMaximo);
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

    private void aplicarPenalizacion(int puntos, String razon) {
        System.out.println(razon + ": " + puntos + " puntos");
        if (callbackPenalizacion != null) {
            callbackPenalizacion.aplicarPenalizacion(puntos, razon);
        }
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

    public int getClientesAtendidos() {
        return clientesAtendidos;
    }

    public int getClientesPerdidos() {
        return clientesPerdidos;
    }

    public boolean haAlcanzadoLimiteClientes() {
        return (clientesAtendidos + clientesPerdidos) >= MAX_CLIENTES_TOTALES;
    }

    public void limpiar() {
        for (Cliente cliente : new ArrayList<>(clientesActivos)) {
            liberarEstacion(cliente);
        }
        clientesActivos.clear();
        clientesAtendidos = 0;
        clientesPerdidos = 0;
        tiempoParaSiguienteCliente = intervaloSpawn;
    }


}
