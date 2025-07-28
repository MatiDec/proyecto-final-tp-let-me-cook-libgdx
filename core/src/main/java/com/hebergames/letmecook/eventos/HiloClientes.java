package com.hebergames.letmecook.eventos;

import com.hebergames.letmecook.entidades.GestorClientes;

public class HiloClientes extends Thread {

    private GestorClientes gestorClientes;
    private boolean ejecutando;
    private boolean pausado;
    private final Object pauseLock = new Object();

    //toda esta parte del código es un arreglo para que no se utilice tanta cpu en el proceso del juego
    private static final int FPS_TARGET = 60;
    private static final long FRAME_TIME = 1000 / FPS_TARGET; // en milisegundos

    public HiloClientes(GestorClientes gestorClientes) {
        this.gestorClientes = gestorClientes;
        this.ejecutando = false;
        this.pausado = false;
        this.setName("HiloClientes");//Esto le pone un nombre al hilo, sirve para el manejo de múltiples hilos sin confundirlos
        this.setDaemon(true);//esta línea no sé que hace pero salva el quilombo de los hilos
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
            float delta = (tiempoActual - tiempoAnterior) / 1000f; // Convertir a segundos
            tiempoAnterior = tiempoActual;

            if (gestorClientes != null) {
                gestorClientes.actualizar(delta);
            }

            // Control de FPS por lo del uso de cpu
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

    public void pausar() {
        pausado = true;
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
