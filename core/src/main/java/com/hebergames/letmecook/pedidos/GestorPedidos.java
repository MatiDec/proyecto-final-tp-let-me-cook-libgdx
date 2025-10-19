package com.hebergames.letmecook.pedidos;

import com.hebergames.letmecook.entidades.Cliente;
import com.hebergames.letmecook.entidades.GestorClientes;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.maquinas.CajaRegistradora;
import com.hebergames.letmecook.maquinas.MesaRetiro;

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

    //BORRAME MDYT SI LO VES, PENSA EN TAPIA, PENSA EN SULCA, PENSA EN TUS NOVIOS
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
        Producto productoEsperado = pedido.getProductoSolicitado();

        boolean correcto = productoEntregado.getNombre().equals(productoEsperado.getNombre());
        int puntos = 0;

        if (correcto) {
            // Calcular puntos según tiempo restante
            float porcentajeTiempo = cliente.getPorcentajeTiempo();
            if (porcentajeTiempo < 0.5f) {
                puntos = 100; // Entrega rápida
            } else if (porcentajeTiempo < 0.8f) {
                puntos = 75; // Entrega normal
            } else {
                puntos = 50; // Entrega justa a tiempo
            }
            pedido.setEstadoPedido(EstadoPedido.COMPLETADO);
        } else {
            puntos = -25; // Penalización por producto incorrecto
            pedido.setEstadoPedido(EstadoPedido.COMPLETADO);
        }

        // Limpiar todo
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
