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

    private void inicializarRecetas() {
        recetas.add(new RecetaHamburguesa());
        // desde acá se añaden más recetas
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
