package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaMaquina;
import com.hebergames.letmecook.utiles.Configuracion;
import com.hebergames.letmecook.utiles.GestorAudio;

public abstract class EstacionTrabajo {
    protected Rectangle area;
    protected PantallaMaquina pantallaMaquina;
    protected boolean fueraDeServicio;
    protected MaquinaProcesadora procesadora;

    public EstacionTrabajo(Rectangle area) {
        this.area = area;
        this.fueraDeServicio = false;
    }

    public boolean fueClickeada(float x, float y) {
        return area.contains(x, y);
    }

    public final void interactuar() {
        System.out.println("DEBUG: EstacionTrabajo.interactuar() llamado");
        System.out.println("DEBUG: fueraDeServicio: " + fueraDeServicio);
        System.out.println("DEBUG: procesadora != null: " + (procesadora != null));

        if (fueraDeServicio) {
            GestorAudio.getInstance().reproducirSonido("error");
            System.out.println("Máquina fuera de servicio");
            return;
        }

        // Intentar procesar directamente si es una máquina procesadora
        if (procesadora != null) {
            System.out.println("DEBUG: Llamando a manejarProcesamiento()");
            manejarProcesamiento();
            return;
        }

        // Si no es procesadora, usar pantalla tradicional
        if (pantallaMaquina == null) {
            pantallaMaquina = crearPantallaMaquina();
        }

        if (pantallaMaquina != null) {
            pantallaMaquina.show();
        }

        alInteractuar();
    }

    private void manejarProcesamiento() {
        System.out.println("DEBUG: manejarProcesamiento() iniciado");

        JugadorHost jugador = Configuracion.getInstancia().getJugadorPrincipal();
        System.out.println("DEBUG: Jugador obtenido: " + (jugador != null ? "OK" : "NULL"));

        // --- Retiro solo si el jugador hace Shift + Click ---
        boolean retirar = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
            || Gdx.input.isKeyPressed(Input.Keys.SHIFT_RIGHT);

        System.out.println("DEBUG: Shift presionado: " + retirar);
        System.out.println("DEBUG: procesadora.tieneProcesandose(): " + procesadora.tieneProcesandose());

        if (procesadora.tieneProcesandose()) {
            System.out.println("DEBUG: Ya hay algo procesándose");
            if (retirar) {
                if (!jugador.tieneInventarioLleno()) {
                    Ingrediente resultado = procesadora.obtenerResultado();
                    if (resultado != null) {
                        jugador.guardarEnInventario(resultado);
                        System.out.println("Retirado: " + resultado.getNombre());
                    }
                } else {
                    System.out.println("Inventario lleno, no se puede retirar");
                }
            } else {
                System.out.println("El horno está cocinando. Mantén SHIFT para retirar.");
            }
            return;
        }

        // Si no hay nada procesándose, intentar iniciar proceso
        ObjetoAlmacenable objetoInventario = jugador.getInventario();
        System.out.println("DEBUG: Objeto en inventario: " + objetoInventario);
        System.out.println("DEBUG: Tipo del objeto: " + (objetoInventario != null ? objetoInventario.getClass().getSimpleName() : "null"));

        if (objetoInventario instanceof Ingrediente) {
            Ingrediente ingrediente = (Ingrediente) objetoInventario;
            System.out.println("DEBUG: Ingrediente válido encontrado: " + ingrediente.getNombre());
            System.out.println("DEBUG: Llamando a procesadora.iniciarProceso()");

            if (procesadora.iniciarProceso(ingrediente)) {
                jugador.sacarDeInventario();
                System.out.println("DEBUG: Proceso iniciado exitosamente!");
                System.out.println("Iniciando proceso con: " + ingrediente.getNombre());
            } else {
                System.out.println("DEBUG: procesadora.iniciarProceso() devolvió false");
                System.out.println("Este ingrediente no se puede procesar aquí");
            }
        } else {
            System.out.println("DEBUG: No hay ingrediente válido - objetoInventario no es Ingrediente");
            System.out.println("No tienes un ingrediente válido para procesar");
        }

        alInteractuar();
    }

    public void actualizar(float delta) {
        if (procesadora != null) {
            procesadora.actualizarProceso(delta);
        }
    }

    public void dibujarIndicador(SpriteBatch batch) {
        if (procesadora != null) {
            procesadora.dibujarIndicador(batch);
        }
    }

    public void setFueraDeServicio(boolean fueraDeServicio) {
        this.fueraDeServicio = fueraDeServicio;
    }

    public boolean estaFueraDeServicio() {
        return fueraDeServicio;
    }

    protected abstract PantallaMaquina crearPantallaMaquina();

    public abstract void alInteractuar();
}
