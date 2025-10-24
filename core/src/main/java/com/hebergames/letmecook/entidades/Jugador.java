package com.hebergames.letmecook.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.hebergames.letmecook.entregables.ObjetoAlmacenable;
import com.hebergames.letmecook.eventos.entrada.DatosEntrada;
import com.hebergames.letmecook.eventos.eventosaleatorios.EventoPisoMojado;
import com.hebergames.letmecook.eventos.eventosaleatorios.GestorEventosAleatorios;
import com.hebergames.letmecook.estaciones.EstacionTrabajo;
import com.hebergames.letmecook.utiles.GestorAnimacion;

import java.util.ArrayList;
import java.util.List;

public class Jugador {
    protected Vector2 posicion;
    protected Vector2 velocidad;
    protected TextureRegion frameActual;
    protected Animation<TextureRegion> animacion;
    protected float estadoTiempo;
    protected float anguloRotacion = 0f;
    private Rectangle hitbox;
    private final float ANCHO_HITBOX = 120;
    private final float ALTO_HITBOX = 120;
    private final float OFFSET_HITBOX_X = 0;
    private final float OFFSET_HITBOX_Y = 0;


    private boolean interactuarPresionado = false;
    private boolean estaEnMenu = false;
    private EstacionTrabajo estacionActual = null;

    private List<Rectangle> colisionables = new ArrayList<>();
    private List<Rectangle> interactuables = new ArrayList<>();
    private List<Jugador> otrosJugadores = new ArrayList<>();

    private ObjetoAlmacenable inventario;

    public final int DISTANCIA_MOVIMIENTO = 400;
    public final int DISTANCIA_CORRIENDO = 800;

    // Variables para el sistema de deslizamiento
    private boolean estaDeslizando = false;
    private Vector2 velocidadDeslizamiento = new Vector2(0, 0);
    private float tiempoDeslizamiento = 0f;
    private final float DURACION_DESLIZAMIENTO = 0.3f;
    private final float FACTOR_DESLIZAMIENTO = 1.5f;

    protected GestorAnimacion gestorAnimacion;
    private String objetoEnMano = "vacio";

    public Jugador(float x, float y, GestorAnimacion gestorAnimacion) {
        this(x, y, gestorAnimacion, false);
    }

    public Jugador(float x, float y, GestorAnimacion gestorAnimacion, boolean esJugadorPrincipal) {
        this.posicion = new Vector2(x, y);
        this.velocidad = new Vector2(0, 0);
        this.gestorAnimacion = gestorAnimacion;
        this.animacion = gestorAnimacion.getAnimacionPorObjeto(objetoEnMano);
        this.estadoTiempo = 0;
        this.hitbox = new Rectangle(x + OFFSET_HITBOX_X, y + OFFSET_HITBOX_Y, ANCHO_HITBOX, ALTO_HITBOX);
    }

    public void actualizar(float delta) {
        if (velocidad.isZero(0.01f) && !estaDeslizando) {
            frameActual = animacion.getKeyFrame(0, true);
            return;
        }

        if (velocidad.x != 0 || velocidad.y != 0 || estaDeslizando) {
            estadoTiempo += delta;
        } else {
            estadoTiempo = 0;
        }
        frameActual = animacion.getKeyFrame(estadoTiempo, true);

        if (estaDeslizando) {
            tiempoDeslizamiento += delta;

            float progreso = tiempoDeslizamiento / DURACION_DESLIZAMIENTO;
            float factorReduccion = Math.max(0f, 1f - progreso);

            velocidad.set(velocidadDeslizamiento.x * factorReduccion,
                velocidadDeslizamiento.y * factorReduccion);

            float desplazamientoX = velocidad.x * delta;
            float desplazamientoY = velocidad.y * delta;

            GestorEventosAleatorios gestorEventos = GestorEventosAleatorios.getInstancia();
            EventoPisoMojado eventoPiso = gestorEventos.getEventoPisoMojado();

            if (eventoPiso != null && eventoPiso.estaSobrePisoMojado(posicion.x, posicion.y)) {
                desplazamientoX *= 2f;
                desplazamientoY *= 2f;
            }

            if (colisionMovimiento(desplazamientoX, desplazamientoY)) {
                estaDeslizando = false;
                tiempoDeslizamiento = 0f;
                velocidad.set(0, 0);
                velocidadDeslizamiento.set(0, 0);
            } else {
                posicion.add(desplazamientoX, desplazamientoY);
                hitbox.setPosition(posicion.x + OFFSET_HITBOX_X, posicion.y + OFFSET_HITBOX_Y);

                if (tiempoDeslizamiento >= DURACION_DESLIZAMIENTO) {
                    estaDeslizando = false;
                    tiempoDeslizamiento = 0f;
                    velocidad.set(0, 0);
                    velocidadDeslizamiento.set(0, 0);
                }
            }
            return;
        }

        float desplazamientoX = velocidad.x * delta;
        float desplazamientoY = velocidad.y * delta;

        if (colisionMovimiento(desplazamientoX, desplazamientoY)) {
            velocidad.set(0, 0);
        } else {
            posicion.add(desplazamientoX, desplazamientoY);
            hitbox.setPosition(posicion.x + OFFSET_HITBOX_X, posicion.y + OFFSET_HITBOX_Y);
        }
    }

    public void dibujar(SpriteBatch batch) {
        TextureRegion frame = frameActual;

        float x = posicion.x;
        float y = posicion.y;
        float width = 128;
        float height = 128;
        float originX = width / 2f;
        float originY = height / 2f;

        batch.draw(frame, x, y, originX, originY, width, height, 1f, 1f, anguloRotacion);
    }

    public Vector2 getPosicion() {
        return this.posicion;
    }

    public void manejarEntrada(DatosEntrada datosEntrada) {
        if (estaDeslizando) {
            return;
        }

        float dx = 0, dy = 0;

        if (datosEntrada.arriba) dy += DISTANCIA_MOVIMIENTO;
        if (datosEntrada.abajo) dy -= DISTANCIA_MOVIMIENTO;
        if (datosEntrada.izquierda) dx -= DISTANCIA_MOVIMIENTO;
        if (datosEntrada.derecha) dx += DISTANCIA_MOVIMIENTO;

        if (datosEntrada.correr && (dx != 0 || dy != 0)) {
            float multiplicador = (float) DISTANCIA_CORRIENDO / DISTANCIA_MOVIMIENTO;
            dx *= multiplicador;
            dy *= multiplicador;
        }

        if (dx != 0 || dy != 0) {
            float angulo = (float) Math.toDegrees(Math.atan2(dy, dx)) - 90f;
            setAnguloRotacion(angulo);
            moverSiNoColisiona(dx, dy, datosEntrada.correr);
        } else {
            velocidad.set(0, 0);
        }
    }

    private boolean colisiona(Rectangle rect) {
        for (Rectangle obstaculo : colisionables) {
            if (obstaculo.overlaps(rect)) {
                return true;
            }
        }

        for (Jugador otro : otrosJugadores) {
            if (otro != this && otro.getHitbox().overlaps(rect))
            {
                return true;
            }
        }
        return false;
    }

    private void moverSiNoColisiona(float dx, float dy, boolean estaCorriendo) {
        GestorEventosAleatorios gestorEventos = GestorEventosAleatorios.getInstancia();
        EventoPisoMojado eventoPiso = gestorEventos.getEventoPisoMojado();

        if (eventoPiso != null && eventoPiso.estaSobrePisoMojado(posicion.x, posicion.y)) {
            dx *= 2f;
            dy *= 2f;
        }
        float deltaTime = Gdx.graphics.getDeltaTime();
        float desplazamientoX = dx * deltaTime;
        float desplazamientoY = dy * deltaTime;

        float nuevaX = hitbox.x + desplazamientoX;
        float nuevaY = hitbox.y + desplazamientoY;

        Rectangle areaFutura = new Rectangle(nuevaX, nuevaY, hitbox.width, hitbox.height);

        if (!colisiona(areaFutura)) {
            velocidad.set(dx, dy);
            return;
        }

        boolean puedeMoverX = false;
        boolean puedeMoverY = false;

        if (dx != 0) {
            Rectangle areaFuturaX = new Rectangle(hitbox.x + desplazamientoX, hitbox.y, hitbox.width, hitbox.height);
            puedeMoverX = !colisiona(areaFuturaX);
        }

        if (dy != 0) {
            Rectangle areaFuturaY = new Rectangle(hitbox.x, hitbox.y + desplazamientoY, hitbox.width, hitbox.height);
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

    private boolean colisionMovimiento(float dx, float dy) {
        float distancia = Math.max(Math.abs(dx), Math.abs(dy));
        int pasos = Math.max(2, (int)((distancia + (ANCHO_HITBOX / 4f) - 1) / (ANCHO_HITBOX / 4f)));

        float pasoX = dx / pasos;
        float pasoY = dy / pasos;

        float px = hitbox.x;
        float py = hitbox.y;

        for (int i = 0; i < pasos; i++) {
            px += pasoX;
            py += pasoY;
            Rectangle area = new Rectangle(px, py, hitbox.width, hitbox.height);
            if (colisiona(area)) {
                return true;
            }
        }
        return false;
    }




    /**
     * Inicia el deslizamiento del jugador en la dirección actual
     */
    public void iniciarDeslizamiento() {
        if (velocidad.len() > 0 && !estaDeslizando) {
            estaDeslizando = true;
            tiempoDeslizamiento = 0f;

            // Guardar la dirección y velocidad actual con un multiplicador
            velocidadDeslizamiento.set(velocidad).scl(FACTOR_DESLIZAMIENTO);
        }
    }

    public boolean tieneInventarioLleno() {
        return this.inventario != null;
    }

    public boolean guardarEnInventario(ObjetoAlmacenable objeto) {
        if (this.inventario == null) {
            this.inventario = objeto;
            setObjetoEnMano(objeto.getNombre());
            return true;
        }
        return false;
    }

    public ObjetoAlmacenable sacarDeInventario() {
        ObjetoAlmacenable item = this.inventario;
        this.inventario = null;
        setObjetoEnMano("vacio");
        return item;
    }

    public void descartarInventario(){
        this.inventario = null;
        setObjetoEnMano("vacio");
    }

    public boolean isInteractuarPresionado() {
        return interactuarPresionado;
    }

    public boolean estaEnMenu() {
        return estaEnMenu;
    }

    public void entrarEnMenu(EstacionTrabajo estacion) {
        this.estaEnMenu = true;
        this.estacionActual = estacion;
    }

    public void salirDeMenu() {
        this.estaEnMenu = false;
        this.estacionActual = null;
    }

    public EstacionTrabajo getEstacionActual() {
        return estacionActual;
    }

    public boolean esJugador1() {
        return true;
    }

    public ObjetoAlmacenable getInventario() {
        return this.inventario;
    }

    public String getNombreItemInventario() {
        if (inventario != null) {
            return this.inventario.getNombre();
        }
        return "Vacío";
    }

    public void setOtrosJugadores(List<Jugador> otrosJugadores) { this.otrosJugadores = otrosJugadores; }

    public void setColisionables(List<Rectangle> colisionables) {
        this.colisionables = colisionables;
    }

    public void setInteractuables(List<Rectangle> interactuables) {
        this.interactuables = interactuables;
    }

    public void setAnguloRotacion(float angulo) {
        this.anguloRotacion = angulo;
    }

    public void setObjetoEnMano(String nombreObjeto) {
        if (!nombreObjeto.equalsIgnoreCase(this.objetoEnMano)) {
            this.objetoEnMano = nombreObjeto;
            this.animacion = gestorAnimacion.getAnimacionPorObjeto(nombreObjeto);
            this.estadoTiempo = 0;
        }
    }

    public Rectangle getHitbox() { return this.hitbox; }

    public String getObjetoEnMano() {
        return objetoEnMano;
    }

    public boolean estaDeslizando() {
        return estaDeslizando;
    }
}
