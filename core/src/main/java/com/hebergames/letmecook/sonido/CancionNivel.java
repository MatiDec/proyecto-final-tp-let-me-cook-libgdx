package com.hebergames.letmecook.sonido;

public enum CancionNivel {
    NIVEL_1("musica_nivel_1", "core/src/main/java/com/hebergames/letmecook/recursos/audio/musica/musicaFondo1.ogg"),
    NIVEL_2("musica_nivel_2", "core/src/main/java/com/hebergames/letmecook/recursos/audio/musica/musicaFondo1.ogg");
    //TODO NO OLVIDAR CAMBIAR LAS RUTAS POR LA MÚSICA NUEVA
    //NIVEL_3("musica_nivel_3", "core/src/main/java/com/hebergames/letmecook/recursos/audio/musica/nivel3.ogg"),
    //NIVEL_4("musica_nivel_4", "core/src/main/java/com/hebergames/letmecook/recursos/audio/musica/nivel4.ogg");

    private String identificador;
    private String ruta;

    CancionNivel(String identificador, String ruta) {
        this.identificador = identificador;
        this.ruta = ruta;
    }

    public String getIdentificador() { return this.identificador; }

    public String getRuta() { return this.ruta; }
}
