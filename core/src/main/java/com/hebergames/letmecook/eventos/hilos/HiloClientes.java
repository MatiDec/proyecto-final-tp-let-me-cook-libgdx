package com.hebergames.letmecook.eventos.hilos;

import com.hebergames.letmecook.entidades.clientes.GestorClientes;

public class HiloClientes extends Thread {

    private final GestorClientes GESTOR_CLIENTES;
    private boolean ejecutando;
    private boolean pausado;
    private final Object pauseLock = new Object();

    private static final int FPS_TARGET = 60;
    private static final long FRAME_TIME = 1000 / FPS_TARGET;

    public HiloClientes(GestorClientes GESTOR_CLIENTES) {
        this.GESTOR_CLIENTES = GESTOR_CLIENTES;
        this.ejecutando = false;
        this.pausado = false;
        this.setName("HiloClientes");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        ejecutando = true;
        long tiempoAnterior = System.currentTimeMillis();

        while (ejecutando) {
            synchronized (pauseLock) {
                if (pausado) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }

            long tiempoActual = System.currentTimeMillis();
            float delta = (tiempoActual - tiempoAnterior) / 1000f;
            tiempoAnterior = tiempoActual;

            if (GESTOR_CLIENTES != null) {
                GESTOR_CLIENTES.actualizar(delta);
            }

            try {
                long tiempoRestante = FRAME_TIME - (System.currentTimeMillis() - tiempoActual);
                if (tiempoRestante > 0) {
                    Thread.sleep(tiempoRestante);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void reanudar() {
        synchronized (pauseLock) {
            pausado = false;
            pauseLock.notifyAll();
        }
    }

    public void detener() {
        ejecutando = false;
        reanudar();
        this.interrupt();
    }

}
