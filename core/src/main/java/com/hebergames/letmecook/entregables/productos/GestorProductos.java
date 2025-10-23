package com.hebergames.letmecook.entregables.productos;

import com.hebergames.letmecook.entregables.productos.bebidas.Cafe;
import com.hebergames.letmecook.entregables.productos.bebidas.Gaseosa;
import com.hebergames.letmecook.entregables.productos.bebidas.TamanoBebida;
import com.hebergames.letmecook.entregables.recetas.TipoReceta;
import com.hebergames.letmecook.utiles.GestorTexturas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GestorProductos {

    private ArrayList<TipoProducto> productosDisponibles;
    private ArrayList<TipoReceta> recetasDisponibles;
    private Random random;

    public GestorProductos() {
        productosDisponibles = new ArrayList<>();
        recetasDisponibles = new ArrayList<>();
        random = new Random();
        cargarProductos();
    }


    private void cargarProductos() {
        // Cargar recetas (productos que requieren preparación)
        recetasDisponibles.addAll(Arrays.asList(TipoReceta.values()));

        // Cargar productos simples (bebidas)
        productosDisponibles.add(TipoProducto.CAFE);
        productosDisponibles.add(TipoProducto.GASEOSA);
    }

    public Producto obtenerProductoAleatorioPorCategorias(CategoriaProducto... categoriasPermitidas) {
        if (categoriasPermitidas == null || categoriasPermitidas.length == 0) {
            return obtenerProductoAleatorio();
        }

        ArrayList<TipoReceta> recetasFiltradas = new ArrayList<>();
        ArrayList<TipoProducto> productosFiltrados = new ArrayList<>();

        // Filtrar recetas por categoría
        for (TipoReceta receta : recetasDisponibles) {
            for (CategoriaProducto cat : categoriasPermitidas) {
                if (receta.getTipoProducto().getCategoria() == cat) {
                    recetasFiltradas.add(receta);
                    break;
                }
            }
        }

        // Filtrar productos simples por categoría
        for (TipoProducto producto : productosDisponibles) {
            for (CategoriaProducto cat : categoriasPermitidas) {
                if (producto.getCategoria() == cat) {
                    productosFiltrados.add(producto);
                    break;
                }
            }
        }

        // Calcular total de opciones
        int totalOpciones = recetasFiltradas.size() + productosFiltrados.size();

        if (totalOpciones == 0) {
            return obtenerProductoAleatorio(); // Fallback
        }

        // Decidir aleatoriamente entre receta o producto simple
        int seleccion = random.nextInt(totalOpciones);

        if (seleccion < recetasFiltradas.size()) {
            // Generar producto de receta
            return recetasFiltradas.get(seleccion).crear().preparar();
        } else {
            // Generar bebida
            int indexProducto = seleccion - recetasFiltradas.size();
            return generarBebidaAleatoria(productosFiltrados.get(indexProducto));
        }
    }

    public Producto obtenerProductoAleatorio() {
        // Decidir aleatoriamente si generar receta o bebida
        boolean generarReceta = random.nextBoolean();

        if (generarReceta && !recetasDisponibles.isEmpty()) {
            // Generar producto de receta
            int index = random.nextInt(recetasDisponibles.size());
            return recetasDisponibles.get(index).crear().preparar();
        } else if (!productosDisponibles.isEmpty()) {
            // Generar bebida aleatoria
            int index = random.nextInt(productosDisponibles.size());
            TipoProducto tipo = productosDisponibles.get(index);

            return generarBebidaAleatoria(tipo);
        }

        return null;
    }

    private Producto generarBebidaAleatoria(TipoProducto tipo) {
        // Seleccionar tamaño aleatorio
        TamanoBebida[] tamanos = TamanoBebida.values();
        TamanoBebida tamano = tamanos[random.nextInt(tamanos.length)];

        if (tipo == TipoProducto.CAFE) {
            // Seleccionar tipo de café aleatorio
            String[] tiposCafe = Cafe.getTiposCafe().keySet().toArray(new String[0]);
            String tipoCafe = tiposCafe[random.nextInt(tiposCafe.length)];
            return new Cafe(tipoCafe, tamano);

        } else if (tipo == TipoProducto.GASEOSA) {
            // Seleccionar tipo de gaseosa aleatorio
            String[] tiposGaseosa = Gaseosa.getTiposGaseosa().keySet().toArray(new String[0]);
            String tipoGaseosa = tiposGaseosa[random.nextInt(tiposGaseosa.length)];
            return new Gaseosa(tipoGaseosa, tamano);
        }

        return tipo.crear();
    }
}
