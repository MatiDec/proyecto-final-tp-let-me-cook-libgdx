package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.pedidos.ResultadoEntrega;
import com.hebergames.letmecook.utiles.Recursos;

public abstract class EstacionTrabajo {
    public Rectangle area;
    protected MaquinaProcesadora procesadora;

    private final static float DIFERENCIA = 150f;

    private Jugador jugadorOcupante = null;

    private boolean fueraDeServicio = false;
    private static Texture texturaError;
    private static TextureRegion iconoError;
    private static boolean texturaErrorCargada = false;

    public EstacionTrabajo(Rectangle area) {
        this.area = area;
    }

    // esto es una de las cosas que después voy a cambiar al refactorizar y limpiar el código
    private static void cargarTexturaError() {
        if (!texturaErrorCargada) {
            try {
                texturaError = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/error_icon.png"));
                iconoError = new TextureRegion(texturaError);
                texturaErrorCargada = true;
            } catch (Exception e) {
                System.err.println("No se pudo cargar icono de error: " + e.getMessage());
            }
        }
    }

    public boolean fueClickeada(float x, float y) {
        return area.contains(x, y);
    }

    public boolean estaCerca(float jugadorX, float jugadorY) {
        float centroMaquinaX = area.x + area.width / 2f;
        float centroMaquinaY = area.y + area.height / 2f;

        float centroJugadorX = jugadorX + Recursos.MEDIDA_TILE / 2f;
        float centroJugadorY = jugadorY + Recursos.MEDIDA_TILE / 2f;

        float dx = centroJugadorX - centroMaquinaX;
        float dy = centroJugadorY - centroMaquinaY;

        double distancia = Math.sqrt(dx * dx + dy * dy);

        return distancia <= DIFERENCIA;
    }


    public final void interactuarConJugador(Jugador jugador) {
        if (jugador == null) {
            System.out.println("ERROR: Jugador es null");
            return;
        }

        if (fueraDeServicio) {
            System.out.println("¡Esta máquina está fuera de servicio!");
            return;
        }

        if (!puedeInteractuar(jugador)) {
            System.out.println("Estas demasiado lejos para interactuar con la maquina.");
            return;
        }

        // Si la máquina está ocupada por otro jugador
        if (jugadorOcupante != null && jugadorOcupante != jugador) {
            System.out.println("Maquina ocupada por otro jugador.");
            return;
        }

        // Si el jugador no está ocupando esta estación, ocuparla Y entrar en menú
        if (jugadorOcupante != jugador) {
            ocupar(jugador);
        }

        // SIEMPRE asegurar que el jugador esté en menú cuando interactúa
        if (!jugador.estaEnMenu()) {
            jugador.entrarEnMenu(this);
        }

        // Ejecutar la interacción específica de cada máquina
        alInteractuar();

        if (this instanceof CajaRegistradora) {
            CajaRegistradora caja = (CajaRegistradora) this;
            if (caja.tomarPedido()) {
                jugador.salirDeMenu();
                alLiberar();
                jugadorOcupante = null;
            }
        }

        // Lógica específica para MesaRetiro
        if (this instanceof MesaRetiro) {
            MesaRetiro mesa = (MesaRetiro) this;
            if (mesa.tieneCliente() && jugador.getInventario() instanceof Producto) {
                Producto productoJugador = (Producto) jugador.getInventario();
                ResultadoEntrega resultado = mesa.entregarProducto(productoJugador);
                jugador.sacarDeInventario();
                System.out.println(resultado.getMensaje());

                jugador.salirDeMenu();
                alLiberar();
                jugadorOcupante = null;
            }
        }
    }

    private boolean puedeInteractuar(Jugador jugador) {
//        float centroMaquinaX = area.x + area.width / 2f;
//        float centroMaquinaY = area.y + area.height / 2f;
//
//        float centroJugadorX = jugador.getPosicion().x + Recursos.MEDIDA_TILE / 2f;
//        float centroJugadorY = jugador.getPosicion().y + Recursos.MEDIDA_TILE / 2f; //sacar numeros magicos
//
//        float dx = centroJugadorX - centroMaquinaX;
//        float dy = centroJugadorY - centroMaquinaY;

        //double distancia = Math.sqrt(dx * dx + dy * dy);

        //return distancia <= DIFERENCIA;
        //mi logica de esto, si la maquina mide 128 y el jugador mide 128 sus centros estan a 64px de sus bordes, entonces la suma ya te da 128
        //lo que es una tile entonces le tengo que sumar 128 de la tile para que funque
        return estaCerca(jugador.getPosicion().x, jugador.getPosicion().y);
    }

    public float calcularDistanciaA(float x, float y) {
        float centroMaquinaX = area.x + area.width / 2f;
        float centroMaquinaY = area.y + area.height / 2f;

        float dx = x - centroMaquinaX;
        float dy = y - centroMaquinaY;

        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    public void manejarProcesamiento(Jugador jugador) {
        System.out.println("DEBUG: manejarProcesamiento() iniciado");
        System.out.println("DEBUG: Jugador obtenido: " + (jugador != null ? "OK" : "NULL"));

        if (procesadora == null) {
            System.out.println("DEBUG: Esta estación no tiene procesadora");
            return;
        }

        System.out.println("DEBUG: procesadora.tieneProcesandose(): " + procesadora.tieneProcesandose());

        // Si hay algo procesándose, retirar
        if (procesadora.tieneProcesandose()) {
            System.out.println("DEBUG: Ya hay algo procesándose, intentando retirar");

            if (!jugador.tieneInventarioLleno()) {
                Ingrediente resultado = procesadora.obtenerResultado();
                if (resultado != null) {
                    jugador.guardarEnInventario(resultado);
                    System.out.println("Retirado: " + resultado.getNombre());
                }
            } else {
                System.out.println("Inventario lleno, no se puede retirar");
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

    public void dibujarIndicadorError(SpriteBatch batch) {
        if (fueraDeServicio) {
            if (!texturaErrorCargada) {
                cargarTexturaError();
            }
            if (iconoError != null) {
                float x = area.x + area.width / 2 - 16;
                float y = area.y + area.height + 10;
                batch.draw(iconoError, x, y, 32, 32);
            }
        }
    }

    public void dibujar(SpriteBatch batch, Jugador jugador) {
        if (jugadorOcupante == jugador && jugador.estaEnMenu()) {
            dibujarMenu(batch, jugador);
        }
    }

    public Jugador getJugadorOcupante() {
        return this.jugadorOcupante;
    }

    public void ocupar(Jugador jugador) {
        if (jugador != null) {
            this.jugadorOcupante = jugador;
        }
    }

    public void verificarDistanciaYLiberar() {
        if (jugadorOcupante != null) {

            if (!estaCerca(jugadorOcupante.getPosicion().x,
                jugadorOcupante.getPosicion().y)) {
                System.out.println("DEBUG: Liberando estación, jugador se alejó");
                jugadorOcupante.salirDeMenu();
                alLiberar();
                jugadorOcupante = null;
            }
        }
    }

    public void setFueraDeServicio(boolean fuera) {
        this.fueraDeServicio = fuera;
    }

    public boolean estaFueraDeServicio() {
        return fueraDeServicio;
    }

    public static void disposeTexturaError() {
        if (texturaError != null) {
            texturaError.dispose();
            texturaErrorCargada = false;
        }
    }

    protected abstract void alLiberar();
    protected abstract void iniciarMenu(Jugador jugador);
    public abstract void manejarSeleccionMenu(Jugador jugador, int numeroSeleccion);
    protected abstract void dibujarMenu(SpriteBatch batch, Jugador jugador);

    public abstract void alInteractuar();

}
