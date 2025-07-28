package com.hebergames.letmecook.eventos;

public class HiloPrincipal extends Thread {

    private int segundosTranscurridos = 0;
    private boolean corriendo = true;
    private boolean pausado = false;

    public HiloPrincipal() {
        this.setDaemon(true);//esta línea no sé que hace pero salva el quilombo de los hilos
    }

    public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void reanudar() {
        pausado = false;
        notify();
    }

    public synchronized void detener() {
        corriendo = false;
        reanudar();
        notify();
    }

    public synchronized int getSegundos() {
        return segundosTranscurridos;
    }

    @Override
    public void run() {
        try {
            while (corriendo) {
                synchronized (this) {
                    while (pausado) {
                        wait();
                    }
                }
                Thread.sleep(1000);
                segundosTranscurridos++;
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
