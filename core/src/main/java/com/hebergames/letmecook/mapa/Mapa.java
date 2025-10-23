package com.hebergames.letmecook.mapa;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.estaciones.*;
import com.hebergames.letmecook.estaciones.bebidas.Cafetera;
import com.hebergames.letmecook.estaciones.bebidas.Fuente;
import com.hebergames.letmecook.estaciones.procesadoras.Freidora;
import com.hebergames.letmecook.estaciones.procesadoras.Horno;
import com.hebergames.letmecook.estaciones.procesadoras.Tostadora;

import java.util.ArrayList;

public class Mapa {

    private TiledMap mapa;
    private OrthogonalTiledMapRenderer renderer;
    private String nombreSucursal;

    public Mapa(String ruta, String nombreSucursal) {
        TmxMapLoader loader = new TmxMapLoader();
        this.mapa = loader.load(ruta);
        this.renderer = new OrthogonalTiledMapRenderer(mapa);
        this.nombreSucursal = nombreSucursal;
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

    public ArrayList<Rectangle> getTilesCaminables() {
        ArrayList<Rectangle> tiles = new ArrayList<>();

        // Obtener dimensiones del mapa
        int mapWidth = mapa.getProperties().get("width", Integer.class);
        int mapHeight = mapa.getProperties().get("height", Integer.class);
        int tileWidth = mapa.getProperties().get("tilewidth", Integer.class);
        int tileHeight = mapa.getProperties().get("tileheight", Integer.class);

        ArrayList<Rectangle> colisionables = getRectangulosColision();

        // Generar tiles caminables (excluir colisionables)
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Rectangle tile = new Rectangle(x * tileWidth, y * tileHeight, tileWidth, tileHeight);

                boolean esColisionable = false;
                for (Rectangle colision : colisionables) {
                    if (colision.overlaps(tile)) {
                        esColisionable = true;
                        break;
                    }
                }

                if (!esColisionable) {
                    tiles.add(tile);
                }
            }
        }

        return tiles;
    }

    public ArrayList<Rectangle> obtenerLimites() {
        ArrayList<Rectangle> limites = new ArrayList<>();
        MapObjects objetos = mapa.getLayers().get("Colisionables").getObjects();

        for (MapObject objeto : objetos) {
            if (objeto instanceof RectangleMapObject) {
                String tipo = objeto.getProperties().get("tipo", String.class);
                if (tipo != null && tipo.equalsIgnoreCase("Limite")) {
                    limites.add(((RectangleMapObject) objeto).getRectangle());
                }
            }
        }

        return limites;
    }

    public String getNombreSucursal() { return this.nombreSucursal; }

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
                case "Basurero":
                    estaciones.add(new Basurero(rect));
                    break;
                case "CajaVirtual":
                    estaciones.add(new CajaVirtual(rect));
                    break;
            }
        }

        return estaciones;
    }

    public String getNombre() { return this.nombreSucursal; }
}
