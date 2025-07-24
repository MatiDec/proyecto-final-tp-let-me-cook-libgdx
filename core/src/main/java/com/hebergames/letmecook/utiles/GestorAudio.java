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

    // Música actual
    private Music musicaActual;
    private String nombreMusicaActual;

    private GestorAudio() {
        canciones = new HashMap<>();
        sonidos = new HashMap<>();
    }

    // Patrón Singleton
    public static GestorAudio getInstance() {
        if (instancia == null) {
            instancia = new GestorAudio();
        }
        return instancia;
    }

    // MÉTODOS PARA CARGAR RECURSOS

    /**
     * Carga una canción desde un archivo
     * @param nombre Nombre identificador para la canción
     * @param rutaArchivo Ruta del archivo de la canción
     */
    public void cargarMusica(String nombre, String rutaArchivo) {
        try {
            Music musica = Gdx.audio.newMusic(Gdx.files.internal(rutaArchivo));
            canciones.put(nombre, musica);
            System.out.println("Música cargada: " + nombre);
        } catch (Exception e) {
            System.err.println("Error al cargar música: " + nombre + " - " + e.getMessage());
        }
    }

    /**
     * Carga un efecto de sonido desde un archivo
     * @param nombre Nombre identificador para el sonido
     * @param rutaArchivo Ruta del archivo de sonido
     */
    public void cargarSonido(String nombre, String rutaArchivo) {
        try {
            Sound sonido = Gdx.audio.newSound(Gdx.files.internal(rutaArchivo));
            sonidos.put(nombre, sonido);
            System.out.println("Sonido cargado: " + nombre);
        } catch (Exception e) {
            System.err.println("Error al cargar sonido: " + nombre + " - " + e.getMessage());
        }
    }

    // MÉTODOS PARA REPRODUCIR MÚSICA

    /**
     * Reproduce una canción en bucle
     * @param nombre Nombre de la música a reproducir
     */
    public void reproducirCancion(String nombre) {
        reproducirCancion(nombre, true);
    }

    /**
     * Reproduce una música
     * @param nombre Nombre de la música a reproducir
     * @param enBucle Si debe reproducirse en bucle
     */
    public void reproducirCancion(String nombre, boolean enBucle) {
        // Detener música actual si es diferente
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
            System.out.println("Reproduciendo música: " + nombre);
        } else {
            System.err.println("Música no encontrada: " + nombre);
        }
    }

    /**
     * Pausa la música actual
     */
    public void pausarMusica() {
        if (musicaActual != null && musicaActual.isPlaying()) {
            musicaActual.pause();
            System.out.println("Música pausada");
        }
    }

    /**
     * Reanuda la música pausada
     */
    public void reanudarMusica() {
        if (musicaActual != null && !musicaActual.isPlaying()) {
            musicaActual.play();
            System.out.println("Música reanudada");
        }
    }

    /**
     * Detiene la música actual
     */
    public void detenerMusica() {
        if (musicaActual != null) {
            musicaActual.stop();
            System.out.println("Música detenida: " + nombreMusicaActual);
        }
        musicaActual = null;
        nombreMusicaActual = null;
    }

    // MÉTODOS PARA REPRODUCIR SONIDOS

    /**
     * Reproduce un efecto de sonido una vez
     * @param nombre Nombre del sonido a reproducir
     */
    public void reproducirSonido(String nombre) {
        Sound sonido = sonidos.get(nombre);
        if (sonido != null) {
            sonido.play(volumenSonidos);
        } else {
            System.err.println("Sonido no encontrado: " + nombre);
        }
    }

    /**
     * Reproduce un sonido con volumen personalizado
     * @param nombre Nombre del sonido
     * @param volumen Volumen (0.0 a 1.0)
     */
    public void reproducirSonido(String nombre, float volumen) {
        Sound sonido = sonidos.get(nombre);
        if (sonido != null) {
            sonido.play(volumen);
        } else {
            System.err.println("Sonido no encontrado: " + nombre);
        }
    }

    // MÉTODOS PARA CONTROLAR VOLÚMENES

    /**
     * Establece el volumen general de la música
     * @param volumen Volumen (0.0 a 1.0)
     */
    public void setVolumenMusica(float volumen) {
        this.volumenMusica = Math.max(0.0f, Math.min(1.0f, volumen));
        if (musicaActual != null) {
            musicaActual.setVolume(this.volumenMusica);
        }
    }

    /**
     * Establece el volumen general de los sonidos
     * @param volumen Volumen (0.0 a 1.0)
     */
    public void setVolumenSonidos(float volumen) {
        this.volumenSonidos = Math.max(0.0f, Math.min(1.0f, volumen));
    }

    /**
     * Obtiene el volumen actual de la música
     */
    public float getVolumenMusica() {
        return volumenMusica;
    }

    /**
     * Obtiene el volumen actual de los sonidos
     */
    public float getVolumenSonidos() {
        return volumenSonidos;
    }

    // MÉTODOS DE UTILIDAD

    /**
     * Verifica si hay música reproduciéndose
     */
    public boolean isMusicaReproduciendo() {
        return musicaActual != null && musicaActual.isPlaying();
    }

    /**
     * Obtiene el nombre de la música actual
     */
    public String getNombreMusicaActual() {
        return nombreMusicaActual;
    }

    /**
     * Silencia/activa toda la música
     */
    public void mutearMusica(boolean mutear) {
        if (musicaActual != null) {
            musicaActual.setVolume(mutear ? 0.0f : volumenMusica);
        }
    }

    @Override
    public void dispose() {
        // Liberar recursos de música
        for (Music musica : canciones.values()) {
            musica.dispose();
        }
        canciones.clear();

        // Liberar recursos de sonidos
        for (Sound sonido : sonidos.values()) {
            sonido.dispose();
        }
        sonidos.clear();

        musicaActual = null;
        nombreMusicaActual = null;

        System.out.println("GestorAudio: Recursos liberados");
    }
}
