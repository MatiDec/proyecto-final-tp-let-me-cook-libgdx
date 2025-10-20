package com.hebergames.letmecook.entregables.productos;

import com.hebergames.letmecook.entregables.recetas.Receta;
import com.hebergames.letmecook.entregables.recetas.RecetaHamburguesa;
import com.hebergames.letmecook.entregables.recetas.TipoReceta;

import java.util.ArrayList;
import java.util.Random;

public class GestorProductos {

    private ArrayList<Receta> recetasDisponibles;
    private Random random;

    public GestorProductos() {
        recetasDisponibles = new ArrayList<>();
        random = new Random();
        cargarRecetas();
    }

    private void cargarRecetas() {
        for (TipoReceta tipo : TipoReceta.values()) {
            recetasDisponibles.add(tipo.crear());
        }
    }

    public Producto obtenerProductoAleatorio() {
        if(recetasDisponibles.isEmpty()) return null;
        int index = random.nextInt(recetasDisponibles.size());
        return recetasDisponibles.get(index).preparar();
    }

}
