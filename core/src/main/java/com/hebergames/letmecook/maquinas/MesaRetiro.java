package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Cliente;
import com.hebergames.letmecook.entidades.GestorClientes;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;
import com.hebergames.letmecook.entregables.productos.Producto;

import java.util.ArrayList;

public class MesaRetiro extends EstacionTrabajo {

    private Cliente clienteEsperando;
    private GestorClientes gestorClientes; // Referencia local

    public MesaRetiro(Rectangle area) {
        super(area);
        this.clienteEsperando = null;
    }

    public void setGestorClientes(GestorClientes gestor) {
        this.gestorClientes = gestor;
    }

    public void agregarClienteEsperando(Cliente cliente) {
        if (clienteEsperando == null) {
            this.clienteEsperando = cliente;
        }
    }

    public void removerCliente(Cliente cliente) {
        if (clienteEsperando == cliente) {
            clienteEsperando = null;
        }
    }

    public Cliente buscarClientePorId(int idCliente) {
        if (clienteEsperando != null && clienteEsperando.getIdCliente() == idCliente) {
            return clienteEsperando;
        }
        return null;
    }

    public ArrayList<Cliente> getClientesEsperando() {
        ArrayList<Cliente> lista = new ArrayList<>();
        if (clienteEsperando != null) {
            lista.add(clienteEsperando);
        }
        return lista;
    }

    public boolean tieneClienteEsperando() {
        return clienteEsperando != null;
    }

    @Override
    public void alInteractuar() {
        Jugador jugador = getJugadorOcupante();
        if (jugador == null || gestorClientes == null) return;

        ObjetoAlmacenable item = jugador.getInventario();

        if (item == null || !(item instanceof Producto)) {
            System.out.println("Necesitas tener un producto para entregar");
            jugador.salirDeMenu();
            return;
        }

        boolean entregado = gestorClientes.entregarPedidoEnMesa(this, (Producto)item, jugador);

        if (entregado) {
            System.out.println("¡Pedido entregado correctamente!");
            jugador.sacarDeInventario();
        } else {
            System.out.println("Este pedido no corresponde a ningún cliente esperando aquí");
        }

        jugador.salirDeMenu();
    }

    @Override
    protected void iniciarMenu(Jugador jugador) {
        // Opcional
    }

    @Override
    public void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion) {
        // No aplica
    }

    @Override
    protected void dibujarMenu(SpriteBatch batch, Jugador jugador) {
        // Opcional
    }
}
