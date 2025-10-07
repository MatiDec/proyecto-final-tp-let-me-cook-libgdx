package com.hebergames.letmecook.mapa;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.maquinas.*;

import java.util.ArrayList;

public class Mapa {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;

    public Mapa(String ruta) {
        TmxMapLoader loader = new TmxMapLoader();
        mapa = loader.load(ruta);
        renderer = new OrthogonalTiledMapRenderer(mapa);
    }

    private ArrayList<Rectangle> obtenerRectangulosDeCapa(String nombreCapa) {
        ArrayList<Rectangle> rectangulos = new ArrayList<>();
        MapObjects objetos = mapa.getLayers().get(nombreCapa).getObjects(); //gracias video del chabon que hizo un pokemon

        for (MapObject objeto : objetos) {
            if (objeto instanceof RectangleMapObject) { //gracias chatty
                rectangulos.add(((RectangleMapObject) objeto).getRectangle());
            }
        }

        return rectangulos;
    }

    public ArrayList<Rectangle> getRectangulosColision() {
        return obtenerRectangulosDeCapa("Colisionables");
    }

    public ArrayList<Rectangle> getRectangulosInteractuables() {
        return obtenerRectangulosDeCapa("Interactuables");
    }

    public ArrayList<Rectangle> getRectangulosClientes() {
        return obtenerRectangulosDeCapa("Clientes");
    }

    public void render(OrthographicCamera camara) {
        renderer.setView(camara);
        renderer.render();
    }

    public void dispose() {
        mapa.dispose();
        renderer.dispose();
    }

    public TiledMap getMapa() {
        return mapa;
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return renderer;
    }

    public ArrayList<EstacionTrabajo> getEstacionesTrabajo() {
        ArrayList<EstacionTrabajo> estaciones = new ArrayList<>();
        MapObjects objetos = mapa.getLayers().get("Interactuables").getObjects();



        for (MapObject objeto : objetos) {
            String tipo = objeto.getName();


            Rectangle rect;

            if (objeto instanceof RectangleMapObject) {
                rect = ((RectangleMapObject) objeto).getRectangle();
            } else {
                float x = (Float) objeto.getProperties().get("x");
                float y = (Float) objeto.getProperties().get("y");
                float width = (Float) objeto.getProperties().get("width");
                float height = (Float) objeto.getProperties().get("height");
                rect = new Rectangle(x, y, width, height);
            }

            switch (tipo) {
                case "Horno":
                    estaciones.add(new Horno(rect));
                    break;
                case "Cafetera":
                    estaciones.add(new Cafetera(rect));
                    break;
                case "Freidora":
                    estaciones.add(new Freidora(rect));
                    break;
                case "Tostadora":
                    estaciones.add(new Tostadora(rect));
                    break;
                case "Fuente":
                    estaciones.add(new Fuente(rect));
                    break;
                case "AreaEnsamblaje":
                    estaciones.add(new Mesa(rect));
                    break;
                case "Heladera":
                    estaciones.add(new Heladera(rect));
                    break;
                case "CajaRegistradora":
                    estaciones.add(new CajaRegistradora(rect));
                    break;
                case "MesaRetiro":
                    estaciones.add(new MesaRetiro(rect));
                    break;
            }
        }

        return estaciones;
    }

}
