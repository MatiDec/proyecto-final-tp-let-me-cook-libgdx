package com.hebergames.letmecook.estaciones.procesadoras;

public enum TipoCoccion {

    HORNO("Horneando", "coccion_horno", MetodoCoccion.HORNO),
    FREIDORA("Friendo", "coccion_freidora", MetodoCoccion.FREIDORA),
    TOSTADORA("Tostando", "coccion_tostadora", MetodoCoccion.TOSTADORA);

    private final String accionRealizada;
    private final String sonido;
    private final MetodoCoccion metodoCoccion;

    TipoCoccion(String verboGerundio, String sonido, MetodoCoccion metodoCoccion) {
        this.accionRealizada = verboGerundio;
        this.sonido = sonido;
        this.metodoCoccion = metodoCoccion;
    }

    public String getAccionRealizada() { return this.accionRealizada; }
    public String getSonido() { return this.sonido; }
    public MetodoCoccion getMetodoCoccion() { return this.metodoCoccion; }
}
