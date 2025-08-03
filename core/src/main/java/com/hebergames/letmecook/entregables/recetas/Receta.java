package com.hebergames.letmecook.entregables.recetas;

import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.entregables.productos.Producto;

import java.util.ArrayList;

public abstract class Receta {
    protected ArrayList<String> ingredientesRequeridos;
    protected String nombre;

    public Receta(String nombre, ArrayList<String> ingredientesRequeridos) {
        this.nombre = nombre;
        this.ingredientesRequeridos = ingredientesRequeridos;
    }

    public boolean puedePreparar(ArrayList<Ingrediente> ingredientesDisponibles) {
        if (ingredientesDisponibles.size() != ingredientesRequeridos.size()) {
            return false;
        }

        ArrayList<String> nombresIngredientes = new ArrayList<>();
        for (Ingrediente ingrediente : ingredientesDisponibles) {
            nombresIngredientes.add(ingrediente.getNombre());
        }

        for (String requerido : ingredientesRequeridos) {
            if (!nombresIngredientes.contains(requerido)) {
                return false;
            }
        }

        return true;
    }

    public abstract Producto preparar();

    public String getNombre() { return nombre; }
    public ArrayList<String> getIngredientesRequeridos() { return ingredientesRequeridos; }
}
