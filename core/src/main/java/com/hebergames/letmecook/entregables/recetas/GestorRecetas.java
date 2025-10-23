package com.hebergames.letmecook.entregables.recetas;

import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.entregables.productos.CategoriaProducto;

import java.util.ArrayList;

public class GestorRecetas {
    private static GestorRecetas instancia;
    private ArrayList<Receta> recetas;

    private GestorRecetas() {
        recetas = new ArrayList<>();
        cargarRecetas();
    }

    public void cargarRecetas() {
        recetas.clear();
        // Cargar todas las recetas desde el enum
        for (TipoReceta tipo : TipoReceta.values()) {
            recetas.add(tipo.crear());
        }
    }

    public static GestorRecetas getInstance() {
        if (instancia == null) {
            instancia = new GestorRecetas();
        }
        return instancia;
    }

    public Receta buscarReceta(ArrayList<Ingrediente> ingredientes) {
        for (Receta receta : recetas) {
            if (receta.puedePreparar(ingredientes)) {
                return receta;
            }
        }
        return null;
    }

    public ArrayList<Receta> getRecetasPorCategoria(CategoriaProducto categoria) {
        ArrayList<Receta> recetasFiltradas = new ArrayList<>();
        for (Receta receta : recetas) {
            if (receta.getCategoria() == categoria) {
                recetasFiltradas.add(receta);
            }
        }
        return recetasFiltradas;
    }

    public ArrayList<Receta> getRecetas() {
        return this.recetas;
    }
}
