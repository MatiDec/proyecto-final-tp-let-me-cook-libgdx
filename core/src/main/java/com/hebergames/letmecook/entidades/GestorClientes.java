package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entregables.productos.GestorProductos;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.maquinas.CajaRegistradora;
import com.hebergames.letmecook.maquinas.MesaRetiro;
import com.hebergames.letmecook.pedidos.EstadoPedido;
import com.hebergames.letmecook.pedidos.GestorPedidos;
import com.hebergames.letmecook.pedidos.Pedido;
import com.hebergames.letmecook.pedidos.PedidoConTiempo;

import java.util.ArrayList;
import java.util.Random;

public class GestorClientes {

    private ArrayList<Pedido> pedidosActivos;

    private ArrayList<Cliente> clientes;
    private ArrayList<Rectangle> ubicacionesClientes;
    private TextureRegion texturaClientePresencial;
    private TextureRegion texturaVirtualInactiva;
    private TextureRegion texturaVirtualActiva;
    private ArrayList<CajaRegistradora> cajasRegistradoras;
    private ArrayList<MesaRetiro> mesasRetiro;
    private GestorPedidos gestorPedidos;

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
        this.cajasRegistradoras = new ArrayList<>();
        this.mesasRetiro = new ArrayList<>();
        this.gestorPedidos = new GestorPedidos();
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

        gestorPedidos.actualizar(delta);

        // Verificar pedidos expirados
        for (int i = clientes.size() - 1; i >= 0; i--) {
            Cliente cliente = clientes.get(i);
            PedidoConTiempo pedido = gestorPedidos.buscarPedidoPorCliente(cliente.getIdCliente());

            if (pedido != null && !pedido.isActivo()) {
                // Pedido expirado, cliente se va enojado
                if (cliente.getMesaRetiroAsignada() != null) {
                    cliente.getMesaRetiroAsignada().removerCliente(cliente);
                }
                cliente.desaparecer();
                removerPedido(cliente);
                clientes.remove(i);
            }
        }

    }

    private void spawnearCliente() {
        if (cajasRegistradoras.isEmpty()) return;

        // Buscar caja registradora libre
        CajaRegistradora cajaLibre = null;
        for (CajaRegistradora caja : cajasRegistradoras) {
            if (!caja.tieneClienteEsperando()) {
                cajaLibre = caja;
                break;
            }
        }

        if (cajaLibre == null) {
            System.out.println("Todas las cajas están ocupadas");
            return; // Todas las cajas ocupadas
        }

        // Crear cliente en la ubicación de la caja
        Rectangle ubicacionCaja = new Rectangle(cajaLibre.area);
        Cliente nuevoCliente;

        if (random.nextFloat() < 0.7f) {
            nuevoCliente = new Cliente(tiempoToleranciaCliente, ubicacionCaja, texturaClientePresencial);
        } else {
            nuevoCliente = new Cliente(tiempoToleranciaCliente, ubicacionCaja,
                texturaVirtualInactiva, texturaVirtualActiva);
        }

        nuevoCliente.aparecer();
        nuevoCliente.setCajaAsignada(cajaLibre);
        cajaLibre.asignarCliente(nuevoCliente);

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

    public void tomarPedidoEnCaja(CajaRegistradora caja, Jugador jugador) {
        if (!caja.tieneClienteEsperando()) return;

        Cliente cliente = caja.getClienteAsignado();
        if (cliente == null) return;

        // Asignar pedido si no lo tiene
        if (!yaTienePedido(cliente)) {
            asignarPedido(cliente);
        }

        // Obtener el pedido del cliente
        Pedido pedido = null;
        for (Pedido p : pedidosActivos) {
            if (p.getIdClienteSolicitante() == cliente.getIdCliente()) {
                pedido = p;
                break;
            }
        }

        if (pedido == null) return;

        // Cambiar estado del pedido
        pedido.setEstadoPedido(EstadoPedido.EN_PREPARACION);

        // Agregar pedido al gestor con temporizador
        gestorPedidos.agregarPedido(pedido);

        // Marcar pedido como tomado - CAMBIO AQUÍ:
        caja.marcarPedidoTomado();

        // Buscar mesa de retiro libre
        MesaRetiro mesaLibre = null;
        for (MesaRetiro mesa : mesasRetiro) {
            if (mesa.getClientesEsperando().isEmpty()) {
                mesaLibre = mesa;
                break;
            }
        }

        if (mesaLibre != null) {
            // Mover cliente a mesa de retiro libre
            cliente.moverAMesaRetiro(new Rectangle(mesaLibre.area));
            cliente.setMesaRetiroAsignada(mesaLibre);
            mesaLibre.agregarClienteEsperando(cliente);
        } else {
            System.out.println("No hay mesas de retiro disponibles");
        }

        caja.liberarCliente();
    }

    public boolean entregarPedidoEnMesa(MesaRetiro mesa, Producto producto, Jugador jugador) {
        if (!mesa.tieneClienteEsperando()) {
            System.out.println("No hay cliente esperando en esta mesa");
            return false;
        }

        Cliente cliente = mesa.getClientesEsperando().get(0); // Ahora siempre habrá solo uno
        PedidoConTiempo pedidoConTiempo = gestorPedidos.buscarPedidoPorCliente(cliente.getIdCliente());

        if (pedidoConTiempo != null && pedidoConTiempo.isActivo()) {
            Pedido pedido = pedidoConTiempo.getPedido();

            // Verificar si el producto coincide con el pedido
            if (pedido.getProductoSolicitado().getNombre().equals(producto.getNombre())) {
                // Pedido correcto entregado
                pedido.setEstadoPedido(EstadoPedido.COMPLETADO);
                gestorPedidos.removerPedido(cliente.getIdCliente());
                mesa.removerCliente(cliente);
                cliente.desaparecer();
                clientes.remove(cliente);
                removerPedido(cliente);

                int puntos = calcularPuntos(pedidoConTiempo.getPorcentajeTiempo());
                System.out.println("¡Pedido entregado! Puntos: " + puntos);

                return true;
            } else {
                System.out.println("Producto incorrecto para este pedido");
            }
        }

        return false;
    }

    private int calcularPuntos(float porcentajeTiempo) {
        // Más puntos si se entrega rápido
        if (porcentajeTiempo > 0.75f) return 100;
        if (porcentajeTiempo > 0.5f) return 75;
        if (porcentajeTiempo > 0.25f) return 50;
        return 25;
    }

    public void registrarCajasRegistradoras(ArrayList<CajaRegistradora> cajas) {
        this.cajasRegistradoras = new ArrayList<>(cajas);
    }

    public void registrarMesasRetiro(ArrayList<MesaRetiro> mesas) {
        this.mesasRetiro = new ArrayList<>(mesas);
    }

    public GestorPedidos getGestorPedidos() {
        return gestorPedidos;
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
