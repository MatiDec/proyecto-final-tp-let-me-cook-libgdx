package com.hebergames.letmecook.pantallas;

import java.io.*;
import java.util.Properties;

public class GestorConfiguracion {

    public static final String ARCHIVO_CONFIG = "core/src/main/java/com/hebergames/letmecook/configuracion/configuracion.txt";
    private static Properties propiedades = new Properties();

    // Cargar configuración
    public static void cargar() {
        File archivo = new File(ARCHIVO_CONFIG);
        if (archivo.exists()) {
            try (FileInputStream fis = new FileInputStream(archivo)) {
                propiedades.load(fis);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Guardar configuración
    public static void guardar() {
        try (FileOutputStream fos = new FileOutputStream(ARCHIVO_CONFIG)) {
            propiedades.store(fos, "Configuraciones del juego");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Set y Get genéricos
    public static void set(String clave, String valor) {
        propiedades.setProperty(clave, valor);
        guardar();
    }

    public static String get(String clave, String defecto) {
        return propiedades.getProperty(clave, defecto);
    }

    public static boolean getBoolean(String clave, boolean defecto) {
        return Boolean.parseBoolean(get(clave, String.valueOf(defecto)));
    }

    public static int getInt(String clave, int defecto) {
        try {
            return Integer.parseInt(get(clave, String.valueOf(defecto)));
        } catch (NumberFormatException e) {
            return defecto;
        }
    }
}
