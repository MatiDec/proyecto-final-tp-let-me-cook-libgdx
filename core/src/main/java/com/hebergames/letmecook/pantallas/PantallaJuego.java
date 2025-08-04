package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.entidades.GestorClientes;
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.HiloClientes;
import com.hebergames.letmecook.eventos.HiloPrincipal;
import com.hebergames.letmecook.mapa.Mapa;
import com.hebergames.letmecook.maquinas.EstacionTrabajo;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaHeladera;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaMesa;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;
import com.hebergames.letmecook.utiles.GestorAudio;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;

public class PantallaJuego extends Pantalla {

    // Configuración de viewports
    private static final float MUNDO_ANCHO = 1920f;
    private static final float MUNDO_ALTO = 1080f;
    private static final float UI_ANCHO = 1920f;
    private static final float UI_ALTO = 1080f;
    private static final int TIEMPO_OBJETIVO = 180;//3 minutos y termina el juego

    private SpriteBatch batch;
    private Entrada entrada;
    private JugadorHost jugadorHost;
    private Texture jugadorSheet;
    private Animation<TextureRegion> animacionJugador;

    private Mapa mapaJuego;
    private ArrayList<EstacionTrabajo> estaciones;
    private Viewport viewportJuego;
    private Viewport viewportUI;
    private OrthographicCamera camaraJuego;
    private OrthographicCamera camaraUi;

    private ArrayList<ObjetoVisualizable> objetosUi;
    private Texto textoContador, textoInventarioActual;
    private float tiempoTranscurrido = 0;

    private PantallaPausa pantallaPausa;
    private PantallaHeladera pantallaHeladera;
    private PantallaMesa pantallaMesa;
    private boolean juegoEnPausa = false;
    private boolean heladeraAbierta = false;
    private boolean mesaAbierta = false;

    private GestorAudio gestorAudio;

    private GestorClientes gestorClientes;
    private HiloClientes hiloClientes;
    private HiloPrincipal hiloPrincipal;

    private Texture texturaClientes;
    private TextureRegion texturaClientePresencial;
    private TextureRegion texturaVirtualInactiva;
    private TextureRegion texturaVirtualActiva;

    @Override
    public void show() {

        hiloPrincipal = new HiloPrincipal();
        hiloPrincipal.start();

        batch = Render.batch;

        configurarTexturasJugador();
        configurarMapaYJugador();

        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);
        entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});

        estaciones = mapaJuego.getEstacionesTrabajo();
        mapaJuego.configurarEstadoMaquinas(estaciones);
        entrada.registrarEstacionesTrabajo(estaciones);


        pantallaPausa = new PantallaPausa(this);

        inicializarMusicaYSonido();

        configurarCamara();

        inicializarUI();
        inicializarClientes();
    }

    private void configurarCamara() {
        camaraJuego = new OrthographicCamera();
        camaraJuego.setToOrtho(false, 1920, 1080);
        camaraJuego.zoom = 1.7f;

        camaraUi = new OrthographicCamera();
        camaraUi.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        viewportJuego = new FitViewport(MUNDO_ANCHO, MUNDO_ALTO, camaraJuego);
        viewportUI = new ScreenViewport(camaraUi);

        entrada.setViewportJuego(viewportJuego);
        entrada.setViewportUI(viewportUI);
    }

    private void configurarTexturasJugador() {
        jugadorSheet = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/imagendepruebanomoral.png"));
        TextureRegion[][] tmp = TextureRegion.split(jugadorSheet, 32, 32);
        animacionJugador = new Animation<>(0.5f, tmp[0]);
    }

    private void configurarMapaYJugador() {
        mapaJuego = new Mapa("core/src/main/java/com/hebergames/letmecook/recursos/mapas/Prueba.tmx");
        jugadorHost = new JugadorHost(1000, 1000, animacionJugador);
        jugadorHost.setColisionables(mapaJuego.getRectangulosColision());
        jugadorHost.setInteractuables(mapaJuego.getRectangulosInteractuables());
    }

    private void inicializarMusicaYSonido() {
        gestorAudio = GestorAudio.getInstance();
        gestorAudio.cargarMusica("musica_fondo", Recursos.CANCION_FONDO);
        gestorAudio.reproducirCancion("musica_fondo", true);
        gestorAudio.pausarMusica();

        gestorAudio.cargarSonido("temporizador", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/tictac.ogg");
        gestorAudio.cargarSonido("coccion_perfecta", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/coccion_completa.ogg");
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
        gestorClientes.setTiempoToleraciaCliente(20f);
        gestorClientes.setMaxClientesSimultaneos(5);

        hiloClientes = new HiloClientes(gestorClientes);
        hiloClientes.start();
    }

    private void inicializarUI() {
        objetosUi = new ArrayList<>();

        textoContador = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);
        textoContador.setTexto("00:00");
        textoContador.setPosition(50, 50);

        textoInventarioActual = new Texto(Recursos.FUENTE_MENU, 32, Color.WHITE, true);
        textoInventarioActual.setTexto("Inventario: Nada");
        textoInventarioActual.setPosition(100, Gdx.graphics.getHeight() - 50);
        objetosUi.add(textoContador);
        objetosUi.add(textoInventarioActual);
    }

    private void renderizarJuego(float delta) {
        viewportJuego.apply();

        Vector2 posicionJugador = jugadorHost.getPosicion();
        camaraJuego.position.set(posicionJugador.x, posicionJugador.y, 0);
        camaraJuego.update();

        mapaJuego.render(camaraJuego);

        if (!juegoEnPausa) {
            if(!heladeraAbierta && !mesaAbierta) {
                jugadorHost.actualizar(delta);
                entrada.actualizarEntradas();
            }
            gestorAudio.reanudarMusica();
        }

        batch.setProjectionMatrix(camaraJuego.combined);
        batch.begin();
        jugadorHost.dibujar(batch);
        gestorClientes.dibujar(batch);
        for (EstacionTrabajo estacion : estaciones) {
            estacion.dibujarIndicador(batch);
        }
        batch.end();
    }

    private void renderizarUI() {
        viewportUI.apply();
        camaraUi.update();

        batch.setProjectionMatrix(camaraUi.combined);
        batch.begin();
        if(hiloPrincipal != null) {
            int segundos = hiloPrincipal.getSegundos();
            int minutos = segundos/60;
            int segundosRestantes = segundos % 60;
            String tiempoFormateado = String.format("%02d:%02d", minutos, segundosRestantes);
            textoContador.setTexto(tiempoFormateado);
            textoInventarioActual.setTexto("Inventario: " + jugadorHost.getNombreItemInventario());
        }

        for(ObjetoVisualizable obj : objetosUi) {
            obj.dibujarEnUi(batch);
        }

        batch.end();
    }

    private void actualizarPosicionesUI() {
        float margen = 50f;
        float uiWidth = viewportUI.getWorldWidth();
        float uiHeight = viewportUI.getWorldHeight();

        textoContador.setPosition(margen, margen);
        textoInventarioActual.setPosition(margen, uiHeight-margen);
    }

    public Vector2 getCoordenadasJuego(int screenX, int screenY) {
        Vector2 coordenadasJuego = new Vector2(screenX, screenY);
        viewportJuego.unproject(coordenadasJuego);
        return coordenadasJuego;
    }

    public Vector2 getCoordenadasUi(int screenX, int screenY) {
        Vector2 coordenadasUi = new Vector2(screenX, screenY);
        viewportUI.unproject(coordenadasUi);
        return coordenadasUi;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePausa();
        }

        renderizarJuego(delta);
        for (EstacionTrabajo estacion : estaciones) {
            estacion.actualizar(delta);
        }
        renderizarUI();

        if(hiloPrincipal != null && hiloPrincipal.getSegundos() >= TIEMPO_OBJETIVO) {
            terminarJuego(calcularPuntajeFinal());
            return;
        }

        if(heladeraAbierta && pantallaHeladera != null) {
            batch.setProjectionMatrix(camaraUi.combined);
            pantallaHeladera.render(delta);
        }

        if(mesaAbierta && pantallaMesa != null) {
            batch.setProjectionMatrix(camaraUi.combined);
            pantallaMesa.render(delta);
        }

        if (juegoEnPausa) {
            batch.setProjectionMatrix(camaraUi.combined);
            pantallaPausa.render(delta);
        }
    }

    private int calcularPuntajeFinal() {
        //Aca se va a calcular los puntos que consigue el jugador por cada nivel.
        return 1000;//Esto es para probar como se ve
    }

    public void togglePausa() {
        juegoEnPausa = !juegoEnPausa;

        if (juegoEnPausa) {
            pantallaPausa.show();
            gestorAudio.pausarMusica();
        } else {
            entrada = new Entrada();
            entrada.setViewportJuego(viewportJuego);
            entrada.setViewportUI(viewportUI);
            Gdx.input.setInputProcessor(entrada);
            entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});
            entrada.registrarEstacionesTrabajo(mapaJuego.getEstacionesTrabajo());
            gestorAudio.reanudarMusica();
        }
    }

    public void reanudarJuego() {
        juegoEnPausa = false;
        entrada = new Entrada();
        entrada.setViewportJuego(viewportJuego);
        entrada.setViewportUI(viewportUI);
        Gdx.input.setInputProcessor(entrada);
        entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});
        entrada.registrarEstacionesTrabajo(mapaJuego.getEstacionesTrabajo());
        gestorAudio.reanudarMusica();
    }

    public void abrirHeladera() {
        if (!heladeraAbierta) {
            heladeraAbierta = true;
            pantallaHeladera = new PantallaHeladera();
            pantallaHeladera.show();
        }
    }

    public void cerrarHeladera() {
        if (heladeraAbierta) {
            heladeraAbierta = false;
            if (pantallaHeladera != null) {
                pantallaHeladera.hide();
                pantallaHeladera = null;
            }

            entrada = new Entrada();
            entrada.setViewportJuego(viewportJuego);
            entrada.setViewportUI(viewportUI);
            Gdx.input.setInputProcessor(entrada);
            entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});
            entrada.registrarEstacionesTrabajo(mapaJuego.getEstacionesTrabajo());
        }
    }

    public void abrirMesa() {
        if(!mesaAbierta) {
            mesaAbierta = true;
            pantallaMesa = PantallaMesa.getInstancia();
            pantallaMesa.show();
        }
    }

    public void cerrarMesa() {
        if (mesaAbierta) {
            mesaAbierta = false;
            if (pantallaMesa != null) {
                pantallaMesa.hide();
            }

            entrada = new Entrada();
            entrada.setViewportJuego(viewportJuego);
            entrada.setViewportUI(viewportUI);
            Gdx.input.setInputProcessor(entrada);
            entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});
            entrada.registrarEstacionesTrabajo(mapaJuego.getEstacionesTrabajo());
        }
    }

    @Override
    public void resize(int width, int height) {
        viewportJuego.update(width, height);
        viewportUI.update(width, height, true);
        actualizarPosicionesUI();
    }

    @Override
    public void pause() {
        if (gestorAudio != null) {
            gestorAudio.pausarMusica();
        }
    }

    @Override
    public void resume() {
        if (gestorAudio != null && !juegoEnPausa) {
            gestorAudio.reanudarMusica();
        }
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        jugadorSheet.dispose();

        if (pantallaPausa != null) {
            pantallaPausa.dispose();
        }

        if (gestorAudio != null) {
            gestorAudio.dispose();
        }

        if (mapaJuego != null) {
            mapaJuego.dispose();
        }
    }

    public void terminarJuego(int puntaje) {
        detenerHilos(); // frena hilos y contadores
        gestorAudio.pausarMusica();

        int segundos = hiloPrincipal.getSegundos();
        int minutos = segundos / 60;
        int segundosRestantes = segundos % 60;
        String tiempoFormateado = String.format("%02d:%02d", minutos, segundosRestantes);

        pantallaMesa.resetearInstancia();

        Pantalla.cambiarPantalla(new PantallaFinal(tiempoFormateado, puntaje));
    }


    public void detenerHilos() {
        hiloPrincipal.detener();
        hiloClientes.detener();
    }
}
