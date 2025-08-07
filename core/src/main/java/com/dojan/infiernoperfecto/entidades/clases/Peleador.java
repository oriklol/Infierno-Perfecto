package com.dojan.infiernoperfecto.entidades.clases;

import com.dojan.infiernoperfecto.ataques.peleador.ataquesPeleador;

public class Peleador extends Clase{
    public Peleador() {
        super("Peleador", 12000, 15, 10, ataquesPeleador.ataquesBasicos());
    }
}
