package com.dojan.infiernoperfecto.entidades.clases;

import com.dojan.infiernoperfecto.ataques.soporte.ataquesSoporte;

public class Soporte extends Clase{
    public Soporte() {
        super("Soporte", 150, 5, 10, 80, ataquesSoporte.ataquesBasicos());
    }
}
