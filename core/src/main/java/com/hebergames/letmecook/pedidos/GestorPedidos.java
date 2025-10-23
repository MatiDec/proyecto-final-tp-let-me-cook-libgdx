package com.hebergames.letmecook.pedidos;

import com.hebergames.letmecook.entidades.clientes.Cliente;
import com.hebergames.letmecook.entidades.clientes.GestorClientes;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.estaciones.CajaRegistradora;
import com.hebergames.letmecook.estaciones.MesaRetiro;

import java.util.ArrayList;

public class GestorPedidos {
    private GestorClientes gestorClientes;
    private ArrayList<MesaRetiro> mesasRetiro;

    public GestorPedidos(GestorClientes gestorClientes, ArrayList<MesaRetiro> mesas) {
        this.gestorClientes = gestorClientes;
        this.mesasRetiro = mesas;
    }

    public boolean tomarPedido(CajaRegistradora caja) {
        Cliente cliente = caja.getCliente();
        if (cliente == null) {
            return false;
        }

        MesaRetiro mesaLibre = buscarMesaLibre();
        if (mesaLibre == null) {
            System.out.println("No hay mesas de retiro disponibles");
            return false;
        }

        // Cambiar estado del pedido
        logMem("antes setEstado");
        cliente.getPedido().setEstadoPedido(EstadoPedido.EN_PREPARACION);
        logMem("despues setEstado");

        cliente.resetearTiempo();
        logMem("despues resetearTiempo");

        cliente.setEstacionAsignada(mesaLibre);
        logMem("despues setEstacionAsignada");

        mesaLibre.asignarCliente(cliente);
        logMem("despues asignarCliente");

        caja.liberarCliente();
        logMem("despues liberarCaja");

        System.out.println("Pedido tomado. Cliente movido a mesa de retiro");
        return true;
    }

    private void logMem(String tag) {
        long used = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024*1024);
        System.out.println(tag + " - MemUsed: " + used + " MB");
    }

    public ResultadoEntrega entregarPedido(MesaRetiro mesa, Producto productoEntregado) {
        Cliente cliente = mesa.getCliente();
        if (cliente == null) {
            return new ResultadoEntrega(false, 0, "No hay cliente en esta mesa");
        }

        Pedido pedido = cliente.getPedido();
        ArrayList<Producto> productosEsperados = pedido.getProductosSolicitados();

// Verificar si el producto entregado está en la lista de esperados
        boolean correcto = false;
        for (Producto esperado : productosEsperados) {
            if (productoEntregado.getNombre().equals(esperado.getNombre())) {
                correcto = true;
                productosEsperados.remove(esperado); // Remover el producto entregado
                break;
            }
        }

        int puntos;

// Si aún quedan productos por entregar, no completar el pedido
        if (correcto && !productosEsperados.isEmpty()) {
            float porcentajeTiempo = cliente.getPorcentajeTiempo();
            if (porcentajeTiempo < 0.5f) {
                puntos = 50; // Puntos parciales por producto correcto
            } else if (porcentajeTiempo < 0.8f) {
                puntos = 35;
            } else {
                puntos = 25;
            }
            String mensaje = "Producto correcto. Faltan " + productosEsperados.size() + " más. +" + puntos;
            return new ResultadoEntrega(true, puntos, mensaje);
        }

        // Si ya entregó todos o se equivocó, completar el pedido
        if (correcto && productosEsperados.isEmpty()) {
            float porcentajeTiempo = cliente.getPorcentajeTiempo();
            if (porcentajeTiempo < 0.5f) {
                puntos = 100;
            } else if (porcentajeTiempo < 0.8f) {
                puntos = 75;
            } else {
                puntos = 50;
            }
            pedido.setEstadoPedido(EstadoPedido.COMPLETADO);
        } else {
            puntos = -25; // Penalización por producto incorrecto
            pedido.setEstadoPedido(EstadoPedido.COMPLETADO);
        }

        mesa.liberarCliente();
        gestorClientes.removerCliente(cliente);

        String mensaje = correcto ? "¡Pedido correcto! +" + puntos : "Pedido incorrecto. " + puntos;
        return new ResultadoEntrega(correcto, puntos, mensaje);
    }

    private MesaRetiro buscarMesaLibre() {
        for (MesaRetiro mesa : mesasRetiro) {
            if (!mesa.tieneCliente()) {
                return mesa;
            }
        }
        return null;
    }

    public void limpiar() {
        for (MesaRetiro mesa : mesasRetiro) {
            mesa.liberarCliente();
        }
    }


    public ArrayList<Cliente> getPedidosActivos() {
        return gestorClientes.getClientesEnPreparacion();
    }
}
