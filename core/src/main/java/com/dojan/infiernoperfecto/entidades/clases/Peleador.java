package com.dojan.infiernoperfecto.entidades.clases;

import com.dojan.infiernoperfecto.ataques.peleador.ataquesPeleador;

public class Peleador extends Clase{
    public Peleador() {
        super("Peleador", 2000, 215, 10, 50,200, ataquesPeleador.ataquesBasicos());
    } //la vida estaba en 12000
}
