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
import com.hebergames.letmecook.entidades.GestorClientes;
import com.hebergames.letmecook.entidades.Jugador;
import com.hebergames.letmecook.eventos.HiloClientes;
import com.hebergames.letmecook.mapa.Mapa;
import com.hebergames.letmecook.maquinas.CajaRegistradora;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;
import com.hebergames.letmecook.maquinas.MesaRetiro;
import com.hebergames.letmecook.pedidos.Pedido;
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
    private Mapa mapaJuego;
    private ArrayList<EstacionTrabajo> estaciones;

    //gestores
    private GestorViewport gestorViewport;
    private GestorUIJuego gestorUI;
    private GestorEntradaJuego gestorEntrada;
    private GestorPantallasOverlay gestorOverlays;
    private GestorAudio gestorAudio;
    private GestorClientes gestorClientes;
    private GestorAnimacion gestorAnimacionJ1;
    private GestorAnimacion gestorAnimacionJ2;
    private GestorTiempoJuego gestorTiempo;

    private HiloClientes hiloClientes;

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
    private ArrayList<CajaRegistradora> cajasRegistradoras;
    private ArrayList<MesaRetiro> mesasRetiro;

    @Override
    public void show() {
        inicializarCore();
        inicializarGestores();
        configurarJugadorYMapa();
        GestorJugadores.getInstancia().setJugadores(jugadores);
        configurarEntradaJugadores();
        inicializarAudio();
        inicializarClientes();
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
    }

    private void configurarJugadorYMapa() {
        configurarTexturasJugadores();
        mapaJuego = new Mapa("core/src/main/java/com/hebergames/letmecook/recursos/mapas/Prueba.tmx");

        // Crear Jugador 1
        jugador1 = new Jugador(1000, 872, gestorAnimacionJ1);
        jugador1.setColisionables(mapaJuego.getRectangulosColision());
        jugador1.setInteractuables(mapaJuego.getRectangulosInteractuables());
        jugadores.add(jugador1);

        // Crear Jugador 2 si está en modo multijugador
        if (MODO_MULTIJUGADOR) {
            jugador2 = new Jugador(1000, 1000, gestorAnimacionJ2);
            jugador2.setColisionables(mapaJuego.getRectangulosColision());
            jugador2.setInteractuables(mapaJuego.getRectangulosInteractuables());
            jugadores.add(jugador2);
        }

        estaciones = mapaJuego.getEstacionesTrabajo();
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
        gestorAudio.cargarMusica("musica_fondo", Recursos.CANCION_FONDO);
        gestorAudio.reproducirCancion("musica_fondo", true);
        gestorAudio.pausarMusica();
        gestorAudio.cargarSonido("temporizador", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/tictac.ogg");
        gestorAudio.cargarSonido("coccion_perfecta", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/coccion_completa.ogg");

        PantallaPausa pantallaPausa = new PantallaPausa(this);
        gestorOverlays = new GestorPantallasOverlay(pantallaPausa, gestorAudio);
    }

    private void inicializarClientes() {
        texturaClientes = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/clientes.jpg"));
        TextureRegion[][] tmpClientes = TextureRegion.split(texturaClientes, 32, 32);
        texturaClientePresencial = tmpClientes[0][0];
        texturaVirtualInactiva = tmpClientes[0][1];
        texturaVirtualActiva = tmpClientes[0][2];

        ArrayList<Rectangle> ubicacionesClientes = mapaJuego.getRectangulosClientes();

        gestorClientes = new GestorClientes(ubicacionesClientes,
            texturaClientePresencial,
            texturaVirtualInactiva,
            texturaVirtualActiva);

        gestorClientes.setIntervalosSpawn(6f);
        gestorClientes.setTiempoToleranciaCliente(20f);
        gestorClientes.setMaxClientesSimultaneos(5);

        cajasRegistradoras = new ArrayList<>();
        mesasRetiro = new ArrayList<>();

        for (EstacionTrabajo estacion : estaciones) {
            if (estacion instanceof CajaRegistradora) {
                cajasRegistradoras.add((CajaRegistradora) estacion);
            } else if (estacion instanceof MesaRetiro) {
                mesasRetiro.add((MesaRetiro) estacion);
            }
        }

        gestorClientes.registrarCajasRegistradoras(cajasRegistradoras);
        gestorClientes.registrarMesasRetiro(mesasRetiro);

        for (CajaRegistradora caja : cajasRegistradoras) {
            caja.setGestorClientes(gestorClientes);
        }

        for (MesaRetiro mesa : mesasRetiro) {
            mesa.setGestorClientes(gestorClientes);
        }

        hiloClientes = new HiloClientes(gestorClientes);
        hiloClientes.start();

        pedidosEnEspera = gestorClientes.getPedidosActivos();
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
            togglePausa();
        }
    }

    private void renderizarJuego(float delta) {
        gestorViewport.getViewportJuego().apply();

        // La cámara sigue al jugador 1 (puedes cambiar esto para seguir a ambos o al centro)
        Vector2 posicionJugador = jugador1.getPosicion();
        gestorViewport.actualizarCamaraDinamica(jugador1, jugador2);

        mapaJuego.render(gestorViewport.getCamaraJuego());

        if (!gestorOverlays.isJuegoEnPausa()) {
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

        // Dibujar todos los jugadores
        for (Jugador jugador : jugadores) {
            jugador.dibujar(batch);
        }


        gestorClientes.dibujar(batch);

        for (EstacionTrabajo estacion : estaciones) {
            estacion.actualizar(delta);
            estacion.dibujarIndicador(batch);
        }
        batch.end();
    }

    private void renderizarUI() {
        gestorViewport.getViewportUI().apply();
        gestorViewport.actualizarCamaraUI();

        gestorUI.actualizarTiempo(gestorTiempo.getSegundos());
        gestorUI.actualizarPedidos(gestorClientes.getGestorPedidos().getPedidosActivos());

        String itemJ2 = (jugador2 != null) ? jugador2.getNombreItemInventario() : null;
        gestorUI.actualizarInventario(
            jugador1.getNombreItemInventario(),
            itemJ2 // Pasa null si jugador2 no existe
        );

        batch.setProjectionMatrix(gestorViewport.getCamaraUI().combined);
        batch.begin();
        gestorUI.dibujar(batch);
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
        //cálculo de puntos
        return 1000;
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

        if (gestorAudio != null) {
            gestorAudio.dispose();
        }

        if (mapaJuego != null) {
            mapaJuego.dispose();
        }
    }

    public void terminarJuego(int puntaje) {
        detenerHilos();
        gestorAudio.pausarMusica();

        Pantalla.cambiarPantalla(new PantallaFinal(gestorTiempo.getTiempoFormateado(), puntaje));
    }

    public void detenerHilos() {
        gestorTiempo.detener();
        hiloClientes.detener();
    }
}
