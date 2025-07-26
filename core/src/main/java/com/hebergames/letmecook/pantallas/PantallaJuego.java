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
import com.hebergames.letmecook.entidades.JugadorHost;
import com.hebergames.letmecook.eventos.Entrada;
import com.hebergames.letmecook.eventos.HiloClientes;
import com.hebergames.letmecook.mapa.Mapa;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;
import com.hebergames.letmecook.utiles.GestorAudio;
import com.badlogic.gdx.graphics.OrthographicCamera;

import java.util.ArrayList;


public class PantallaJuego extends Pantalla {

    private SpriteBatch batch;
    private Entrada entrada;
    private JugadorHost jugadorHost;
    private Texture jugadorSheet;
    private Animation<TextureRegion> animacionJugador;


    private Mapa mapaJuego;
    private OrthographicCamera camara;
    private OrthographicCamera camaraUi;


    private PantallaPausa pantallaPausa;
    private boolean juegoEnPausa = false;

    private GestorAudio gestorAudio;

    private GestorClientes gestorClientes;
    private HiloClientes hiloClientes;
    private Texture texturaClientes;
    private TextureRegion texturaClientePresencial;
    private TextureRegion texturaVirtualInactiva;
    private TextureRegion texturaVirtualActiva;

    @Override
    public void show() {
        batch = Render.batch;

        jugadorSheet = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/imagendepruebanomoral.png"));
        TextureRegion[][] tmp = TextureRegion.split(jugadorSheet, 32, 32);
        animacionJugador = new Animation<>(0.5f, tmp[0]);


        mapaJuego = new Mapa("core/src/main/java/com/hebergames/letmecook/recursos/mapas/Prueba.tmx"); //aca estaba mal el orden
        jugadorHost = new JugadorHost(100, 100, animacionJugador);

        jugadorHost.setColisionables(mapaJuego.getRectangulosColision());
        jugadorHost.setInteractuables(mapaJuego.getRectangulosInteractuables()); // si también quieres incluirlos


        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);
        entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});

        pantallaPausa = new PantallaPausa(this);

        //Música de fondo en el nivel.
        gestorAudio = GestorAudio.getInstance();
        gestorAudio.cargarMusica("musica_fondo", Recursos.CANCION_FONDO);
        gestorAudio.reproducirCancion("musica_fondo", true);
        gestorAudio.pausarMusica();

        camara = new OrthographicCamera();
        camara.setToOrtho(false, 1920, 1080); //aca va la medida del mapa



        camaraUi = new OrthographicCamera();
        camaraUi.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Inicializar texturas de clientes
        texturaClientes = new Texture(Gdx.files.internal("core/src/main/java/com/hebergames/letmecook/recursos/imagenes/clientes.jpg"));
        TextureRegion[][] tmpClientes = TextureRegion.split(texturaClientes, 32, 32);
        texturaClientePresencial = tmpClientes[0][0]; // Primera sprite para cliente presencial
        texturaVirtualInactiva = tmpClientes[0][1];   // Segunda sprite para virtual inactivo
        texturaVirtualActiva = tmpClientes[0][2];     // Tercera sprite para virtual activo

        // Obtener ubicaciones de clientes del mapa (añadir capa "Clientes" en Tiled)
        ArrayList<Rectangle> ubicacionesClientes = mapaJuego.getRectangulosClientes();

        // Inicializar gestor de clientes (Si no está lo de arriba esto no funciona)
        gestorClientes = new GestorClientes(ubicacionesClientes,
            texturaClientePresencial,
            texturaVirtualInactiva,
            texturaVirtualActiva);

        // Configurar parámetros de spawn (opcionales)
        gestorClientes.setIntervalosSpawn(6f); // Cliente cada 6 segundos
        gestorClientes.setTiempoToleraciaCliente(20f); // 20 segundos de tolerancia
        gestorClientes.setMaxClientesSimultaneos(5); // Máximo 5 clientes

        // Inicializar y empezar hilo de clientes
        hiloClientes = new HiloClientes(gestorClientes);
        hiloClientes.start();

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePausa();
        }

        Vector2 posicionJugador = jugadorHost.getPosicion();
        camara.position.set(posicionJugador.x, posicionJugador.y, 0);
        camara.update();
        mapaJuego.render(camara);

        if (!juegoEnPausa) {
            jugadorHost.actualizar(delta);
            entrada.actualizarEntradas();
            gestorAudio.reanudarMusica();
        }

        // Renderizar el juego siempre (para que se vea de fondo)

        batch.setProjectionMatrix(camara.combined); //gracias chatty que esto no lo sabia
        batch.begin();
        jugadorHost.dibujar(batch);

        gestorClientes.dibujar(batch);

        batch.end();



        if (juegoEnPausa) {
            batch.setProjectionMatrix(camaraUi.combined);
            pantallaPausa.render(delta);
        }

    }

    public void togglePausa() {
        juegoEnPausa = !juegoEnPausa;

        if (juegoEnPausa) {
            pantallaPausa.show();
            gestorAudio.pausarMusica();
            if(hiloClientes != null) {
                hiloClientes.pausar();
            }
        } else {
            entrada = new Entrada();
            Gdx.input.setInputProcessor(entrada);
            entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});
            gestorAudio.reanudarMusica();
            if(hiloClientes != null) {
                hiloClientes.reanudar();
            }
        }
    }

    public void reanudarJuego() {
        juegoEnPausa = false;
        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);
        entrada.registrarJugador(jugadorHost, new int[]{Input.Keys.W, Input.Keys.A, Input.Keys.S, Input.Keys.D});

        gestorAudio.reanudarMusica();

        if(hiloClientes != null) {
            hiloClientes.reanudar();
        }
    }

    public boolean isJuegoEnPausa() {
        return this.juegoEnPausa;
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {
        // Pausar música cuando la aplicación se minimiza
        if (gestorAudio != null) {
            gestorAudio.pausarMusica();
        }

        if(hiloClientes != null) {
            hiloClientes.pausar();
        }

    }

    @Override
    public void resume() {
        // Reanudar música cuando la aplicación vuelve al foco
        if (gestorAudio != null && !juegoEnPausa) {
            gestorAudio.reanudarMusica();
        }

        if (hiloClientes != null && !juegoEnPausa) {
            hiloClientes.reanudar();
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
        // Liberar recursos de audio
        if (gestorAudio != null) {
            gestorAudio.dispose();
        }

        if (mapaJuego != null) {
            mapaJuego.dispose();
        }

        if (hiloClientes != null) {
            hiloClientes.detener();
            try {
                hiloClientes.join(1000);//Esperar máximo 1 segundo, anda a saber qué es Join
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
