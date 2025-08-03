package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;
import com.hebergames.letmecook.eventos.DatosEntrada;
import com.hebergames.letmecook.entregables.ingredientes.Ingrediente;
import com.hebergames.letmecook.utiles.Configuracion;

import java.util.ArrayList;
import java.util.List;

public class JugadorHost extends Jugador {

    private List<Rectangle> colisionables = new ArrayList<>();
    private List<Rectangle> interactuables = new ArrayList<>();

    private ObjetoAlmacenable inventario;

    public JugadorHost(float x, float y, Animation<TextureRegion> animacion) {
        super(x, y, animacion);
        Configuracion.getInstancia().setJugadorPrincipal(this);//Poner al jugador host como principal en la configuracion
    }

    @Override
    public void manejarEntrada(DatosEntrada datosEntrada) {
        float dx = 0, dy = 0;

        if (datosEntrada.estaPresionada(Input.Keys.W)) dy += DISTANCIA_MOVIMIENTO;
        if (datosEntrada.estaPresionada(Input.Keys.S)) dy -= DISTANCIA_MOVIMIENTO;
        if (datosEntrada.estaPresionada(Input.Keys.A)) dx -= DISTANCIA_MOVIMIENTO;
        if (datosEntrada.estaPresionada(Input.Keys.D)) dx += DISTANCIA_MOVIMIENTO;

        if (dx != 0 || dy != 0) {
            float angulo = (float) Math.toDegrees(Math.atan2(dy, dx)) - 90f; //linea turbianga que no se como cambiar
            setAnguloRotacion(angulo);
        }

        moverSiNoColisiona(dx, dy);
    }

    private boolean colisiona(Rectangle rect) {
        for (Rectangle obstaculo : colisionables) {
            if (obstaculo.overlaps(rect)) {
                return true; //quilombo mistico
            }
        }
        return false;
    }



    private void moverSiNoColisiona(float dx, float dy) {
        float anchoSprite = 32;
        float altoSprite = 32; //aca lo cambie porque no me di cuenta que antes aplicabamos 2 veces el movimiento, por eso lo vas a ver re lento al jugador

        float deltaTime = Gdx.graphics.getDeltaTime();
        float desplazamientoX = dx * deltaTime;
        float desplazamientoY = dy * deltaTime;

        float nuevaX = posicion.x + desplazamientoX;
        float nuevaY = posicion.y + desplazamientoY;

        Rectangle areaFutura = new Rectangle(nuevaX, nuevaY, anchoSprite, altoSprite);

        if (!colisiona(areaFutura)) {
            velocidad.set(dx, dy);
            return;
        }


        boolean puedeMoverX = false;
        boolean puedeMoverY = false;

        if (dx != 0) {
            Rectangle areaFuturaX = new Rectangle(nuevaX, posicion.y, anchoSprite, altoSprite);
            puedeMoverX = !colisiona(areaFuturaX);
        }

        if (dy != 0) {
            Rectangle areaFuturaY = new Rectangle(posicion.x, nuevaY, anchoSprite, altoSprite);
            puedeMoverY = !colisiona(areaFuturaY);
        }


        if (puedeMoverX) {
            velocidad.x = dx;
        } else {
            velocidad.x = 0;
        }


        if (puedeMoverY) {
            velocidad.y = dy;
        } else {
            velocidad.y = 0;
        }
    }

    public boolean tieneInventarioLleno() {
        return this.inventario != null;
    }

    public boolean guardarEnInventario(ObjetoAlmacenable ingrediente) {
        if(this.inventario == null) {
            this.inventario = ingrediente;
            return true;
        }
        return false;
    }

    public ObjetoAlmacenable sacarDeInventario() {
        ObjetoAlmacenable item = this.inventario;
        this.inventario = null;
        return item;
    }

    public ObjetoAlmacenable getInventario() {
        return this.inventario;
    }

    public String getNombreItemInventario() {
        if(inventario != null) {
            return this.inventario.getNombre();
        }
        return "Vacío";
    }

    public void setColisionables(List<Rectangle> colisionables) {
        this.colisionables = colisionables;
    }

    public void setInteractuables(List<Rectangle> interactuables) {
        this.interactuables = interactuables;
    }


}
