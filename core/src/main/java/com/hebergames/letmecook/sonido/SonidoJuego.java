package com.hebergames.letmecook.sonido;

import com.hebergames.letmecook.utiles.Recursos;

public enum SonidoJuego {
    TEMPORIZADOR("temporizador", Recursos.RUTA_AUDIO + "sonidos/tictac.ogg"),
    COCCION_PERFECTA("coccion_perfecta", Recursos.RUTA_AUDIO + "sonidos/coccion_completa.ogg"),
    CLIENTE_LLEGA("cliente_llega", Recursos.RUTA_AUDIO + "sonidos/cliente_llega.ogg"),
    PEDIDO_ENTREGADO("pedido_entregado", Recursos.RUTA_AUDIO + "sonidos/pedido_correcto.ogg"),
    PEDIDO_INCORRECTO("pedido_incorrecto",Recursos.RUTA_AUDIO + "sonidos/pedido_mal.ogg"),
    ITEM_RECOGIDO("item_recogido", Recursos.RUTA_AUDIO + "sonidos/pickup.ogg"),
    DESPIDO("despido", Recursos.RUTA_AUDIO + "sonidos/despido.ogg"),
    NIVEL_COMPLETADO("nivel_completado", Recursos.RUTA_AUDIO + "sonidos/nivel_completo.ogg");

    private final String IDENTIFICADOR;
    private final String RUTA;

    SonidoJuego(String IDENTIFICADOR, String RUTA) {
        this.IDENTIFICADOR = IDENTIFICADOR;
        this.RUTA = RUTA;
    }

    public String getIdentificador() { return this.IDENTIFICADOR; }

    public String getRuta() { return this.RUTA; }
}
