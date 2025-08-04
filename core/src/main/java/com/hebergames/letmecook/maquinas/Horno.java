package com.hebergames.letmecook.maquinas;

import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.pantallas.pantallasmaquinas.PantallaMaquina;

public class Horno extends EstacionTrabajo {

    public Horno(Rectangle area) {
        super(area);
        procesadora = new HornoProcesador(area); // Usar procesadora en lugar de pantalla
    }

    @Override
    protected PantallaMaquina crearPantallaMaquina() {
        return null;
    }

    @Override
    public void alInteractuar() {
        System.out.println("Interacción con horno realizada");
    }

}
