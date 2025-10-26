package com.hebergames.letmecook.sonido;

public enum SonidoJuego {
    TEMPORIZADOR("temporizador", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/tictac.ogg"),
    COCCION_PERFECTA("coccion_perfecta", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/coccion_completa.ogg"),
    CLIENTE_LLEGA("cliente_llega", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/cliente_llega.ogg"),
    PEDIDO_ENTREGADO("pedido_entregado", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/pedido_correcto.ogg"),
    PEDIDO_INCORRECTO("pedido_incorrecto", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/pedido_mal.ogg"),
    ITEM_RECOGIDO("item_recogido", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/pickup.ogg"),
    NIVEL_COMPLETADO("nivel_completado", "core/src/main/java/com/hebergames/letmecook/recursos/audio/sonidos/nivel_completo.ogg");

    private final String IDENTIFICADOR;
    private final String RUTA;

    SonidoJuego(String IDENTIFICADOR, String RUTA) {
        this.IDENTIFICADOR = IDENTIFICADOR;
        this.RUTA = RUTA;
    }

    public String getIdentificador() { return this.IDENTIFICADOR; }

    public String getRuta() { return this.RUTA; }
}
