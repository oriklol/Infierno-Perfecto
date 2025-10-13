package com.dojan.infiernoperfecto.entidades;

import com.dojan.infiernoperfecto.entidades.clases.Clase;

public class Jugador extends Personaje {
    private Clase clase;

    public Jugador(String nombre, Clase clase) {
        super(nombre, clase.getVidaBase(), clase.getDanioBase(), clase.getDefensaBase(), clase.getAtaques());
        this.clase = clase;
        // inicializar fe del personaje desde la clase
        this.setFeBase(clase.getFeBase());
    }

    public Clase getClase() {
        return clase;
    }
}
