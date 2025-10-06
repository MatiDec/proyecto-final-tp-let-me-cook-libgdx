package com.hebergames.letmecook.maquinas;

import com.hebergames.letmecook.entregables.ObjetoAlmacenable;
import java.util.function.Supplier;

/**
 * Representa una opción dentro del menú de una estación de trabajo.
 * Permite configurar opciones escalables con número, nombre y acción asociada.
 */
public class OpcionMenu {
    private int numero;
    private String nombre;
    private Supplier<ObjetoAlmacenable> creadorObjeto;
    private Runnable accion;
    private TipoOpcion tipo;

    /**
     * Tipos de opciones disponibles
     */
    public enum TipoOpcion {
        CREAR_OBJETO,    // Para crear y dar un objeto al jugador
        ACCION_SIMPLE    // Para ejecutar una acción sin dar objeto
    }

    /**
     * Constructor para opciones que crean objetos (ej: tomar ingrediente de heladera)
     * @param numero El número de la opción (1-9)
     * @param nombre El nombre descriptivo que se mostrará
     * @param creadorObjeto Función que crea el objeto cuando se selecciona
     */
    public OpcionMenu(int numero, String nombre, Supplier<ObjetoAlmacenable> creadorObjeto) {
        this.numero = numero;
        this.nombre = nombre;
        this.creadorObjeto = creadorObjeto;
        this.tipo = TipoOpcion.CREAR_OBJETO;
    }

    /**
     * Constructor para opciones que ejecutan acciones simples (ej: preparar receta)
     * @param numero El número de la opción (1-9)
     * @param nombre El nombre descriptivo que se mostrará
     * @param accion La acción a ejecutar cuando se selecciona
     */
    public OpcionMenu(int numero, String nombre, Runnable accion) {
        this.numero = numero;
        this.nombre = nombre;
        this.accion = accion;
        this.tipo = TipoOpcion.ACCION_SIMPLE;
    }

    /**
     * Obtiene el número de la opción
     */
    public int getNumero() {
        return this.numero;
    }

    /**
     * Obtiene el nombre descriptivo de la opción
     */
    public String getNombre() {
        return this.nombre;
    }

    /**
     * Obtiene el texto completo para mostrar en el menú
     * @return String en formato "numero. nombre"
     */
    public String getTextoMenu() {
        return numero + ". " + nombre;
    }

    /**
     * Crea el objeto asociado a esta opción
     * @return El objeto creado, o null si es una acción simple
     */
    public ObjetoAlmacenable crearObjeto() {
        if (tipo == TipoOpcion.CREAR_OBJETO && creadorObjeto != null) {
            return creadorObjeto.get();
        }
        return null;
    }

    /**
     * Ejecuta la acción asociada a esta opción
     */
    public void ejecutarAccion() {
        if (tipo == TipoOpcion.ACCION_SIMPLE && accion != null) {
            accion.run();
        }
    }

    /**
     * Obtiene el tipo de opción
     */
    public TipoOpcion getTipo() {
        return tipo;
    }

    /**
     * Verifica si esta opción crea un objeto
     */
    public boolean esCreadorObjeto() {
        return tipo == TipoOpcion.CREAR_OBJETO;
    }

    /**
     * Verifica si esta opción ejecuta una acción simple
     */
    public boolean esAccionSimple() {
        return tipo == TipoOpcion.ACCION_SIMPLE;
    }
}
