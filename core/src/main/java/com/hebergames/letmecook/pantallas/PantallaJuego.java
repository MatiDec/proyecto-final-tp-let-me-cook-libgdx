package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hebergames.letmecook.entidades.Cliente;
import com.hebergames.letmecook.entidades.GestorClientes;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.entregables.productos.Producto;
import com.hebergames.letmecook.entregables.recetas.GestorRecetas;
import com.hebergames.letmecook.entregables.recetas.Receta;
import com.hebergames.letmecook.eventos.*;
import com.hebergames.letmecook.mapa.*;
import com.hebergames.letmecook.maquinas.CajaRegistradora;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;
import com.hebergames.letmecook.maquinas.MesaRetiro;
import com.hebergames.letmecook.pedidos.GestorPedidos;
import com.hebergames.letmecook.pedidos.Pedido;
import com.hebergames.letmecook.sonido.CancionNivel;
import com.hebergames.letmecook.sonido.GestorAudio;
import com.hebergames.letmecook.sonido.SonidoJuego;
import com.hebergames.letmecook.utiles.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PantallaJuego extends Pantalla {

    private static final int TIEMPO_OBJETIVO = 90; // 3 minutos
    private static final boolean MODO_MULTIJUGADOR = true; // Cambiar a false para un solo jugador

    private SpriteBatch batch;
    private Jugador jugador1;
    private Jugador jugador2;
    private ArrayList<Jugador> jugadores;
    private GestorMapa gestorMapa;
    private ArrayList<EstacionTrabajo> estaciones;

    private GestorClientes gestorClientes;
    private GestorPedidos gestorPedidos;
    private HiloClientes hiloClientes;
    private GestorPuntaje gestorPuntaje;

    //gestores
    private GestorViewport gestorViewport;
    private GestorUIJuego gestorUI;
    private GestorEntradaJuego gestorEntrada;
    private GestorPantallasOverlay gestorOverlays;
    private GestorAudio gestorAudio;
    private GestorAnimacion gestorAnimacionJ1;
    private GestorAnimacion gestorAnimacionJ2;
    private GestorTiempoJuego gestorTiempo;
    private GestorPartida gestorPartida;
    private NivelPartida nivelActual;
    private PantallaCalendario pantallaCalendario;

    //texturas y animaciones
    private Texture jugadorSheet;
    private Texture texturaClientes;
    private TextureRegion texturaClientePresencial;
    private TextureRegion texturaVirtualInactiva;
    private TextureRegion texturaVirtualActiva;
    private Map<String, Animation<TextureRegion>> animacionesConItem = new HashMap<>();
    private Animation<TextureRegion> animacionJugadorNormal;

    //pedidos
    private ArrayList<Pedido> pedidosEnEspera;

    @Override
    public void show() {
        gestorPartida = GestorPartida.getInstancia();

        if(gestorPartida.getNivelActual() == null) {
            ArrayList<String> rutasMapas = new ArrayList<>();
            rutasMapas.add("core/src/main/java/com/hebergames/letmecook/recursos/mapas/Prueba.tmx");
            rutasMapas.add("core/src/main/java/com/hebergames/letmecook/recursos/mapas/PruebaMoral.tmx");
            //Acá van a tener q ir añadiendose todos los mapas

            gestorPartida.generarNuevaPartida(rutasMapas, rutasMapas.size());
        }

        nivelActual = gestorPartida.getNivelActual();

        inicializarCore();
        inicializarGestores();
        configurarJugadorYMapa();
        GestorJugadores.getInstancia().setJugadores(jugadores);
        configurarEntradaJugadores();
        inicializarClientes();
        inicializarAudio();
        inicializarSistemaPedidos();
    }

    private void inicializarClientes() {
        GestorTexturas.getInstance().cargarTexturas();
    }

    private void inicializarCore() {
        batch = Render.batch;
        gestorTiempo = new GestorTiempoJuego(TIEMPO_OBJETIVO);
        jugadores = new ArrayList<>();
    }

    private void inicializarGestores() {
        gestorViewport = new GestorViewport();
        gestorUI = new GestorUIJuego();
        gestorAudio = GestorAudio.getInstance();
        gestorPuntaje = new GestorPuntaje();
    }

    private void configurarJugadorYMapa() {
        configurarTexturasJugadores();

        gestorMapa = new GestorMapa();
        gestorMapa.setMapaActual(nivelActual.getMapa());

        estaciones = gestorMapa.getEstaciones();

        jugador1 = new Jugador(1000, 872, gestorAnimacionJ1);
        gestorMapa.asignarColisionesYInteracciones(jugador1);
        jugadores.add(jugador1);

        if (MODO_MULTIJUGADOR) {
            jugador2 = new Jugador(1000, 872, gestorAnimacionJ2);
            gestorMapa.asignarColisionesYInteracciones(jugador2);
            jugadores.add(jugador2);
        }

        GestorJugadores.getInstancia().setJugadores(jugadores);

    }

    private void configurarTexturasJugadores() {
        // Gestor de animación para Jugador 1
        gestorAnimacionJ1 = new GestorAnimacion(
            "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/Jugador.png",
            32, 32, 0.2f
        );

        // Gestor de animación para Jugador 2 (puedes usar otra textura diferente)
        if (MODO_MULTIJUGADOR) {
            gestorAnimacionJ2 = new GestorAnimacion(
                "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/Jugador.png", // Cambia si tienes otra textura
                32, 32, 0.2f
            );
        }
    }

    private void configurarEntradaJugadores() {
        gestorEntrada = new GestorEntradaJuego(jugadores, estaciones);
        gestorEntrada.configurarEntrada(
            gestorViewport.getViewportJuego(),
            gestorViewport.getViewportUI()
        );
    }

    private void inicializarAudio() {
        gestorAudio.cargarTodasLasMusicasNiveles();
        gestorAudio.cargarTodosLosSonidos();

        CancionNivel cancionNivel = nivelActual.getCancionNivel();
        gestorAudio.reproducirMusicaNivel(cancionNivel);
        gestorAudio.pausarMusica();

        PantallaPausa pantallaPausa = new PantallaPausa(this);
        pantallaCalendario = new PantallaCalendario(this);
        gestorOverlays = new GestorPantallasOverlay(pantallaPausa, pantallaCalendario, gestorAudio);
    }

    private void inicializarSistemaPedidos() {
        // Filtrar cajas y mesas de las estaciones
        ArrayList<CajaRegistradora> cajas = new ArrayList<>();
        ArrayList<MesaRetiro> mesas = new ArrayList<>();

        for (EstacionTrabajo estacion : estaciones) {
            if (estacion instanceof CajaRegistradora) {
                cajas.add((CajaRegistradora) estacion);
            } else if (estacion instanceof MesaRetiro) {
                mesas.add((MesaRetiro) estacion);
            }
        }

        // Usar GestorRecetas para obtener productos disponibles
        GestorRecetas gestorRecetas = GestorRecetas.getInstance();
        ArrayList<Producto> productosDisponibles = new ArrayList<>();

        for (Receta receta : gestorRecetas.getRecetas()) {
            productosDisponibles.add(receta.preparar());
        }

        TurnoTrabajo turnoActual = nivelActual.getTurno();

        gestorClientes = new GestorClientes(cajas, productosDisponibles, 15f, turnoActual);
        gestorPedidos = new GestorPedidos(gestorClientes, mesas);

        // Asignar gestor a las cajas
        for (CajaRegistradora caja : cajas) {
            caja.setGestorPedidos(gestorPedidos);
        }

        // Asignar gestor y callback a las mesas
        for (MesaRetiro mesa : mesas) {
            mesa.setGestorPedidos(gestorPedidos);
            mesa.setCallbackPuntaje(gestorPuntaje);
        }

        // Iniciar hilo de clientes DESPUÉS de cargar texturas
        hiloClientes = new HiloClientes(gestorClientes);
        hiloClientes.start();

        GestorEventosAleatorios gestorEventos = GestorEventosAleatorios.getInstancia();
        gestorEventos.reset();

        // Registrar eventos de máquinas rotas
        for (EstacionTrabajo estacion : estaciones) {
            // No aplicar a cajas ni mesas de retiro
            if (!(estacion instanceof CajaRegistradora) && !(estacion instanceof MesaRetiro)) {
                gestorEventos.registrarEventoPosible(new EventoMaquinaRota(estacion));
            }
        }

        // Registrar evento de piso mojado
        ArrayList<Rectangle> tilesCaminables = gestorMapa.getTilesCaminables();
        if (!tilesCaminables.isEmpty()) {
            gestorEventos.registrarEventoPosible(new EventoPisoMojado(tilesCaminables));
        }

        // Activar eventos para la primera ronda
        gestorEventos.iniciarRonda();
    }



    @Override
    public void render(float delta) {
        limpiarPantalla();
        manejarInput();
        renderizarJuego(delta);
        renderizarUI();
        renderizarOverlays(delta);
        verificarFinDeJuego();
    }

    private void limpiarPantalla() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void manejarInput() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (gestorOverlays.isCalendarioVisible()) {
                gestorOverlays.toggleCalendario();
            } else {
                togglePausa();
            }
        }

        if (Gdx.input.isKeyJustPressed(Input.Keys.TAB)) {
            if (gestorOverlays.isJuegoEnPausa()) {
                togglePausa();
            }
            gestorOverlays.toggleCalendario();

            if (!gestorOverlays.isCalendarioVisible()) {
                gestorEntrada.configurarEntrada(
                    gestorViewport.getViewportJuego(),
                    gestorViewport.getViewportUI()
                );
            }
        }
    }

    private void renderizarJuego(float delta) {
        gestorViewport.getViewportJuego().apply();

        // La cámara sigue al jugador 1 (puedes cambiar esto para seguir a ambos o al centro)
        Vector2 posicionJugador = jugador1.getPosicion();
        gestorViewport.actualizarCamaraDinamica(jugador1, jugador2);

        gestorMapa.renderizar(gestorViewport.getCamaraJuego());


        if (!gestorOverlays.isJuegoEnPausa() && !gestorOverlays.isCalendarioVisible()) {
            for (Jugador jugador : jugadores) {
                jugador.actualizar(delta);
            }
            for (EstacionTrabajo estacion : estaciones) {
                estacion.verificarDistanciaYLiberar();
            }
            gestorEntrada.actualizarEntradas();
            gestorAudio.reanudarMusica();
        }

        batch.setProjectionMatrix(gestorViewport.getCamaraJuego().combined);
        batch.begin();

        gestorMapa.actualizarEstaciones(delta);
        gestorMapa.dibujarIndicadores(batch);

        // Dibujar todos los jugadores
        for (Jugador jugador : jugadores) {
            jugador.dibujar(batch);
        }

        if (gestorClientes != null) {
            for (Cliente cliente : gestorClientes.getClientesActivos()) {
                cliente.dibujar(batch);
            }
        }

        batch.end();
    }

    private void renderizarUI() {
        gestorViewport.getViewportUI().apply();
        gestorViewport.actualizarCamaraUI();

        gestorUI.actualizarTiempo(gestorTiempo.getSegundos());

        String itemJ2 = (jugador2 != null) ? jugador2.getNombreItemInventario() : null;
        gestorUI.actualizarInventario(
            jugador1.getNombreItemInventario(),
            itemJ2 // Pasa null si jugador2 no existe
        );
        if (gestorPedidos != null) {
            gestorUI.actualizarPedidosActivos(gestorPedidos.getPedidosActivos());
        }
        gestorUI.actualizarPuntaje(gestorPuntaje.getPuntajeActual());

        batch.setProjectionMatrix(gestorViewport.getCamaraUI().combined);
        batch.begin();
        gestorUI.dibujar(batch);

        if (gestorPedidos != null) {
            gestorUI.dibujarPedidos(batch, gestorPedidos.getPedidosActivos(),
                gestorViewport.getViewportUI().getWorldWidth(),
                gestorViewport.getViewportUI().getWorldHeight());
        }
        for (Jugador jugador : jugadores) {
            if (jugador.estaEnMenu()) {
                EstacionTrabajo estacion = jugador.getEstacionActual();
                if (estacion != null) {
                    estacion.dibujar(batch, jugador);
                }
            }
        }
        batch.end();
    }

    public void agregarPuntos(int puntos) {
        gestorPuntaje.agregarPuntos(puntos);
    }

    private void renderizarOverlays(float delta) {
        batch.setProjectionMatrix(gestorViewport.getCamaraUI().combined);
        gestorOverlays.renderOverlays(delta, batch);
    }

    private void verificarFinDeJuego() {
        if (gestorTiempo.haTerminadoTiempo()) {
            terminarJuego(calcularPuntajeFinal());
        }
    }

    private int calcularPuntajeFinal() {
        return gestorPuntaje.getPuntajeActual();
    }

    public void togglePausa() {
        gestorOverlays.togglePausa();

        if (!gestorOverlays.isJuegoEnPausa()) {
            gestorEntrada.configurarEntrada(
                gestorViewport.getViewportJuego(),
                gestorViewport.getViewportUI()
            );
        }
    }

    public void reanudarJuego() {
        gestorOverlays.reanudarJuego();
        gestorEntrada.configurarEntrada(
            gestorViewport.getViewportJuego(),
            gestorViewport.getViewportUI()
        );
    }

    public Animation<TextureRegion> getAnimacionConItem(String nombreItem) {
        Animation<TextureRegion> animacion = animacionesConItem.get(nombreItem);
        if (animacion == null) {
            try {
                String ruta = "core/src/main/java/com/hebergames/letmecook/recursos/imagenes/imagendepruebanomoral" + nombreItem.toLowerCase() + ".png";
                Texture textura = new Texture(Gdx.files.internal(ruta));
                TextureRegion[][] tmp = TextureRegion.split(textura, 32, 32);
                animacion = new Animation<>(0.5f, tmp[0]);
                animacionesConItem.put(nombreItem, animacion);
            } catch (Exception e) {
                Gdx.app.error("Spritesheet", "No se pudo cargar la animacion para: " + nombreItem, e);
                return animacionJugadorNormal;
            }
        }
        return animacion;
    }

    public Animation<TextureRegion> getAnimacionNormal() {
        return animacionJugadorNormal;
    }

    public Vector2 getCoordenadasJuego(int screenX, int screenY) {
        return gestorViewport.convertirCoordenadasJuego(screenX, screenY);
    }

    public Vector2 getCoordenadasUi(int screenX, int screenY) {
        return gestorViewport.convertirCoordenadasUI(screenX, screenY);
    }

    public Jugador getJugador1() {
        return jugador1;
    }

    public Jugador getJugador2() {
        return jugador2;
    }

    public List<Jugador> getJugadores() {
        return jugadores;
    }

    @Override
    public void resize(int width, int height) {
        gestorViewport.resize(width, height);
        gestorUI.actualizarPosiciones(
            gestorViewport.getViewportUI().getWorldWidth(),
            gestorViewport.getViewportUI().getWorldHeight()
        );
    }

    @Override
    public void pause() {
        if (gestorAudio != null) {
            gestorAudio.pausarMusica();
        }
    }

    @Override
    public void resume() {
        if (gestorAudio != null && !gestorOverlays.isJuegoEnPausa()) {
            gestorAudio.reanudarMusica();
        }
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        if (jugadorSheet != null) {
            jugadorSheet.dispose();
        }

        gestorOverlays.dispose();

        if (gestorUI != null) {
            gestorUI.dispose(); // Agregar esta línea
        }

        if (gestorAudio != null) {
            gestorAudio.dispose();
        }

        if (gestorMapa != null) {
            gestorMapa.dispose();
        }

        EstacionTrabajo.disposeTexturaError();
        GestorEventosAleatorios.getInstancia().reset();

    }

    public void terminarJuego(int puntaje) {
        detenerHilos();
        GestorEventosAleatorios.getInstancia().finalizarRonda();
        gestorAudio.detenerMusica();
        gestorAudio.reproducirSonido(SonidoJuego.NIVEL_COMPLETADO);

        boolean hayMasNiveles = gestorPartida.avanzarNivel(puntaje);

        if (hayMasNiveles) {
            Pantalla.cambiarPantalla(new PantallaJuego());
        } else {
            gestorAudio.detenerMusica();
            int puntajeTotal = gestorPartida.getPuntajeTotalPartida();
            Pantalla.cambiarPantalla(new PantallaFinal(gestorTiempo.getTiempoFormateado(), puntajeTotal));
            gestorPartida.resetearPartida();
        }
    }

    public void detenerHilos() {
        gestorTiempo.detener();
        if(hiloClientes != null) {
            hiloClientes.detener();
        }
    }
}
