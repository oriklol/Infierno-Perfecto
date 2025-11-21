package com.dojan.infiernoperfecto.entidades.clases;

import com.dojan.infiernoperfecto.ataques.soporte.ataquesSoporte;

public class Soporte extends Clase{
    public Soporte() {
        super("Soporte", 120, 5, 12, 100, 200, ataquesSoporte.ataquesBasicos());
    }
}
