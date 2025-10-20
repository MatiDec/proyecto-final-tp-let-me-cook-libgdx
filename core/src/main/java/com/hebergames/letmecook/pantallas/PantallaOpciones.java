package com.hebergames.letmecook.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.hebergames.letmecook.elementos.Texto;
import com.hebergames.letmecook.eventos.entrada.Entrada;
import com.hebergames.letmecook.eventos.entrada.TextoInteractuable;
import com.hebergames.letmecook.pantallas.juego.GestorConfiguracion;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

import java.util.ArrayList;
import java.util.List;

public class PantallaOpciones extends Pantalla {

    private SpriteBatch BATCH;
    private OrthographicCamera camara;
    private Viewport viewport;
    private Entrada entrada;

    private Texto tVolumen, tPantallaCompleta, tResolucion, tAplicar, tVolver;
    private List<Texto> opcionesResolucion = new ArrayList<>();

    private int volumenActual;
    private boolean pantallaCompleta;
    private String[] resoluciones = {"840x680", "1280x720", "1366x768", "1600x900", "1920x1080", "2560x1440"};
    private int indiceResolucion;
    private boolean menuResolucionesAbierto = false;

    @Override
    public void show() {
        BATCH = Render.batch;
        camara = new OrthographicCamera();
        viewport = new ScreenViewport(camara);

        GestorConfiguracion.cargar();

        entrada = new Entrada();
        Gdx.input.setInputProcessor(entrada);

        inicializarOpciones();
        posicionarTextos();
        registrarEntradas();
    }

    private void inicializarOpciones() {
        volumenActual = GestorConfiguracion.getInt("volumenMusica", 100);
        pantallaCompleta = GestorConfiguracion.getBoolean("pantallaCompleta", false);
        String resActual = GestorConfiguracion.get("resolucion", "1920x1080");

        indiceResolucion = 0;
        for (int i = 0; i < resoluciones.length; i++) {
            if (resoluciones[i].equals(resActual)) {
                indiceResolucion = i;
                break;
            }
        }

        tVolumen = new Texto(Recursos.FUENTE_MENU, 60, Color.WHITE, true);
        actualizarTextoVolumen();

        tPantallaCompleta = new Texto(Recursos.FUENTE_MENU, 60, Color.WHITE, true);
        actualizarTextoPantallaCompleta();

        tResolucion = new Texto(Recursos.FUENTE_MENU, 60, Color.WHITE, true);
        actualizarTextoResolucion();

        tAplicar = new Texto(Recursos.FUENTE_MENU, 60, Color.WHITE, true);
        tAplicar.setTexto("Aplicar");

        tVolver = new Texto(Recursos.FUENTE_MENU, 60, Color.WHITE, true);
        tVolver.setTexto("Volver");

        opcionesResolucion.clear();

        if (opcionesResolucion.isEmpty()) {
            for (String r : resoluciones) {
                Texto opcion = new Texto(Recursos.FUENTE_MENU, 50, Color.LIGHT_GRAY, true);
                opcion.setTexto(r);
                opcionesResolucion.add(opcion);
            }
        }
    }

    private void actualizarTextoVolumen() {
        String barra = generarBarra(volumenActual);
        tVolumen.setTexto("Volumen: " + barra + " " + volumenActual + "%");
    }

    //este metodo es un quilombo pero es el que hace el slider
    private String generarBarra(int valor) {
        int total = 20; // más segmentos → más precisión
        int llenos = (int) ((valor / 100f) * total);
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < total; i++) {
            sb.append(i < llenos ? "#" : "-");
        }
        sb.append("]");
        return sb.toString();
    }

    private void actualizarTextoPantallaCompleta() {
        tPantallaCompleta.setTexto("Pantalla completa: " + (pantallaCompleta ? "ON" : "OFF"));
    }

    private void actualizarTextoResolucion() {
        tResolucion.setTexto("Resolución: " + resoluciones[indiceResolucion] + " ▼");
    }

    private void registrarEntradas() {
        entrada.registrar(new TextoInteractuable(tVolumen, () -> {
            float xClick = entrada.getUltimoClickX();
            String textoCompleto = tVolumen.getTexto();
            int indexBarraStart = textoCompleto.indexOf("[");
            int indexBarraEnd = textoCompleto.indexOf("]") + 1;
            float anchoTexto = tVolumen.getAncho();
            float inicioBarra = tVolumen.getX() + (indexBarraStart / (float) textoCompleto.length()) * anchoTexto;
            float finBarra = tVolumen.getX() + (indexBarraEnd / (float) textoCompleto.length()) * anchoTexto;
            float anchoBarra = finBarra - inicioBarra;
            float relativo = (xClick - inicioBarra) / anchoBarra;
            if (relativo < 0) relativo = 0;
            if (relativo > 1) relativo = 1;
            volumenActual = (int) (relativo * 100);
            actualizarTextoVolumen();
            GestorConfiguracion.set("volumenMusica", String.valueOf(volumenActual));
        }));

        entrada.registrar(new TextoInteractuable(tPantallaCompleta, () -> {
            pantallaCompleta = !pantallaCompleta;
            actualizarTextoPantallaCompleta();
            GestorConfiguracion.set("pantallaCompleta", String.valueOf(pantallaCompleta));
        }));

        entrada.registrar(new TextoInteractuable(tResolucion, () -> {
            menuResolucionesAbierto = !menuResolucionesAbierto;
        }));

        for (int i = 0; i < opcionesResolucion.size(); i++) {
            final int idx = i;
            entrada.registrar(new TextoInteractuable(opcionesResolucion.get(i), () -> {
                if (menuResolucionesAbierto) {
                    indiceResolucion = idx;
                    actualizarTextoResolucion();
                    menuResolucionesAbierto = false;
                }
            }));
        }

        entrada.registrar(new TextoInteractuable(tAplicar, () -> {
            String res = resoluciones[indiceResolucion];
            GestorConfiguracion.set("resolucion", res);
            GestorConfiguracion.set("pantallaCompleta", String.valueOf(pantallaCompleta));

            String[] partes = res.split("x");
            int w = Integer.parseInt(partes[0]);
            int h = Integer.parseInt(partes[1]);

            if (pantallaCompleta) {
                Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
            } else {
                Gdx.graphics.setWindowedMode(w, h);
            }
        }));

        entrada.registrar(new TextoInteractuable(tVolver, () -> {
            cambiarPantalla(new PantallaMenu());
        }));
    }

    private void posicionarTextos() {
        float ancho = viewport.getWorldWidth();
        float alto = viewport.getWorldHeight();

        float centroX = ancho / 2f;
        float centroY = alto / 2f;

        float espaciado = Math.max(50, alto * 0.08f);
        float altura = tVolumen.getAlto();

        float inicio = centroY + (2 * (altura + espaciado));

        tVolumen.setPosition(centroX - tVolumen.getAncho() / 2f, inicio);
        tPantallaCompleta.setPosition(centroX - tPantallaCompleta.getAncho() / 2f, inicio - (altura + espaciado));
        tResolucion.setPosition(centroX - tResolucion.getAncho() / 2f, inicio - 2 * (altura + espaciado));
        tAplicar.setPosition(centroX - tAplicar.getAncho() / 2f, inicio - 3 * (altura + espaciado));
        tVolver.setPosition(centroX - tVolver.getAncho() / 2f, inicio - 4 * (altura + espaciado));

        posicionarOpcionesResolucion();
    }

    private void posicionarOpcionesResolucion() {
        if (opcionesResolucion.isEmpty()) return;

        float baseY = tResolucion.getY() - 70;
        float baseX = tResolucion.getX();

        for (int i = 0; i < opcionesResolucion.size(); i++) {
            Texto opcion = opcionesResolucion.get(i);
            opcion.setPosition(baseX, baseY - i * 60);
        }
    }

    @Override
    public void render(float delta) {
        viewport.apply();
        camara.update();
        entrada.actualizarEntradas();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        BATCH.setProjectionMatrix(camara.combined);
        BATCH.begin();

        tVolumen.dibujar();
        tPantallaCompleta.dibujar();
        tResolucion.dibujar();
        tAplicar.dibujar();
        tVolver.dibujar();

        if (menuResolucionesAbierto) {
            for (int i = 0; i < Math.min(opcionesResolucion.size(), resoluciones.length); i++) {
                Texto opcion = opcionesResolucion.get(i);
                opcion.setColor(i == indiceResolucion ? Color.YELLOW : Color.LIGHT_GRAY);
                opcion.dibujar();
            }
        }

        BATCH.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        posicionarTextos();
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {}
}
