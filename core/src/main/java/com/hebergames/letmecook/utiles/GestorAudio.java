package com.hebergames.letmecook.utiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Disposable;
import java.util.HashMap;
import java.util.Map;

public class GestorAudio implements Disposable {

    private static GestorAudio instancia;

    private Map<String, Music> canciones;
    private Map<String, Sound> sonidos;

    // Control de volúmenes (Por defecto, esto luego lo va a poder cambiar el usuario)
    private float volumenMusica = 0.6f;
    private float volumenSonidos = 0.6f;

    private Music musicaActual;
    private String nombreMusicaActual;

    private GestorAudio() {
        canciones = new HashMap<>();
        sonidos = new HashMap<>();
    }

    //Patrón de diseño Singleton, se asegura de que una clase tenga una única instancia en
    // todo el programa y provee un punto de acceso global a esa única instancia, haciendola static.
    public static GestorAudio getInstance() {
        if (instancia == null) {
            instancia = new GestorAudio();
        }
        return instancia;
    }

    public void cargarMusica(String nombre, String rutaArchivo) {
        try {
            Music musica = Gdx.audio.newMusic(Gdx.files.internal(rutaArchivo));
            canciones.put(nombre, musica);
            System.out.println("Música cargada: " + nombre);
        } catch (Exception e) {
            System.err.println("Error al cargar música: " + nombre + " - " + e.getMessage());
        }
    }

    public void cargarSonido(String nombre, String rutaArchivo) {
        try {
            Sound sonido = Gdx.audio.newSound(Gdx.files.internal(rutaArchivo));
            sonidos.put(nombre, sonido);
            System.out.println("Sonido cargado: " + nombre);
        } catch (Exception e) {
            System.err.println("Error al cargar sonido: " + nombre + " - " + e.getMessage());
        }
    }

    public void reproducirCancion(String nombre) {
        reproducirCancion(nombre, true);
    }

    public void reproducirCancion(String nombre, boolean enBucle) {

        if (musicaActual != null && !nombre.equals(nombreMusicaActual)) {
            detenerMusica();
        }

        Music musica = canciones.get(nombre);
        if (musica != null) {
            musica.setVolume(volumenMusica);
            musica.setLooping(enBucle);
            musica.play();
            musicaActual = musica;
            nombreMusicaActual = nombre;
        } else {
            System.err.println("Música no encontrada: " + nombre);
        }
    }

    public void pausarMusica() {
        if (musicaActual != null && musicaActual.isPlaying()) {
            musicaActual.pause();
        }
    }

    public void reanudarMusica() {
        if (musicaActual != null && !musicaActual.isPlaying()) {
            musicaActual.play();
        }
    }

    public void detenerMusica() {
        if (musicaActual != null) {
            musicaActual.stop();
        }
        musicaActual = null;
        nombreMusicaActual = null;
    }

    public void reproducirSonido(String nombre) {
        Sound sonido = sonidos.get(nombre);
        if (sonido != null) {
            sonido.play(volumenSonidos);
        } else {
            System.err.println("Sonido no encontrado: " + nombre);
        }
    }

    public void reproducirSonido(String nombre, float volumen) {
        Sound sonido = sonidos.get(nombre);
        if (sonido != null) {
            sonido.play(volumen);
        } else {
            System.err.println("Sonido no encontrado: " + nombre);
        }
    }

    public void setVolumenMusica(float volumen) {
        this.volumenMusica = Math.max(0.0f, Math.min(1.0f, volumen));
        if (musicaActual != null) {
            musicaActual.setVolume(this.volumenMusica);
        }
    }

    public void setVolumenSonidos(float volumen) {
        this.volumenSonidos = Math.max(0.0f, Math.min(1.0f, volumen));
    }

    public float getVolumenMusica() {
        return volumenMusica;
    }

    public float getVolumenSonidos() {
        return volumenSonidos;
    }

    public boolean isMusicaReproduciendo() {
        return musicaActual != null && musicaActual.isPlaying();
    }

    public String getNombreMusicaActual() {
        return nombreMusicaActual;
    }

    public void mutearMusica(boolean mutear) {
        if (musicaActual != null) {
            musicaActual.setVolume(mutear ? 0.0f : volumenMusica);
        }
    }

    @Override
    public void dispose() {

        for (Music musica : canciones.values()) {
            musica.dispose();
        }
        canciones.clear();

        for (Sound sonido : sonidos.values()) {
            sonido.dispose();
        }
        sonidos.clear();

        musicaActual = null;
        nombreMusicaActual = null;

    }
}
