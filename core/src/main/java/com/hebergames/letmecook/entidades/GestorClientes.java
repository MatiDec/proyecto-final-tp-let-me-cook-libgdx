package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class GestorClientes {

    private ArrayList<Cliente> clientes;
    private ArrayList<Rectangle> ubicacionesClientes;
    private TextureRegion texturaClientePresencial;
    private TextureRegion texturaVirtualInactiva;
    private TextureRegion texturaVirtualActiva;

    private float tiempoSpawn;
    private float intervalosSpawn;
    private float tiempoToleraciaCliente;
    private int maxClientesSimultaneos;
    private Random random;

    public GestorClientes(ArrayList<Rectangle> ubicacionesClientes,
                          TextureRegion texturaClientePresencial,
                          TextureRegion texturaVirtualInactiva,
                          TextureRegion texturaVirtualActiva) {
        this.ubicacionesClientes = new ArrayList<>(ubicacionesClientes);
        this.texturaClientePresencial = texturaClientePresencial;
        this.texturaVirtualInactiva = texturaVirtualInactiva;
        this.texturaVirtualActiva = texturaVirtualActiva;

        this.clientes = new ArrayList<>();
        this.tiempoSpawn = 0f;
        this.intervalosSpawn = 5f; //Esto es cada cuanto spawnea
        this.tiempoToleraciaCliente = 15f; // Esto es cuanto tiempo se banca que tardes con el pedido
        this.maxClientesSimultaneos = 3; // Esto es el límite de clientes q puede haber
        this.random = new Random();
    }

    public void actualizar(float delta) {
        // Actualizar clientes existentes
        for (int i = clientes.size() - 1; i >= 0; i--) {
            Cliente cliente = clientes.get(i);
            cliente.actualizar(delta);

            // Remover clientes inactivos (Los que se agota el tiempo de tolerancia)
            if (!cliente.isActivo()) {
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

        // Decidir tipo de cliente (70% presencial, 30% virtual) Esto de acá genera de forma random si el cliente que va a aparecer es virtual o presencial
        Cliente nuevoCliente;
        if (random.nextFloat() < 0.7f) {
            nuevoCliente = new ClientePresencial(tiempoToleraciaCliente, ubicacionSeleccionada, texturaClientePresencial);
        } else {
            nuevoCliente = new ClienteVirtual(tiempoToleraciaCliente, ubicacionSeleccionada,
                texturaVirtualInactiva, texturaVirtualActiva);
        }

        nuevoCliente.aparecer();
        clientes.add(nuevoCliente);
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

    public void setTiempoToleraciaCliente(float tiempo) {
        this.tiempoToleraciaCliente = tiempo;
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
}
