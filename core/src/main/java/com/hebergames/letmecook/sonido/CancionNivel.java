package com.hebergames.letmecook.sonido;

public enum CancionNivel {
    NIVEL_1("musica_nivel_1", "core/src/main/java/com/hebergames/letmecook/recursos/audio/musica/musicaFondo1.ogg"),
    NIVEL_2("musica_nivel_2", "core/src/main/java/com/hebergames/letmecook/recursos/audio/musica/musicaFondo1.ogg");
    //TODO NO OLVIDAR CAMBIAR LAS RUTAS POR LA MÚSICA NUEVA

    private final String IDENTIFICADOR;
    private final String RUTA;

    CancionNivel(String IDENTIFICADOR, String RUTA) {
        this.IDENTIFICADOR = IDENTIFICADOR;
        this.RUTA = RUTA;
    }

    public String getIdentificador() { return this.IDENTIFICADOR; }

    public String getRuta() { return this.RUTA; }
}
