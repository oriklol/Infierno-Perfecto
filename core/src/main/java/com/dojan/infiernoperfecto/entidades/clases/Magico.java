package com.dojan.infiernoperfecto.entidades.clases;

import com.dojan.infiernoperfecto.ataques.magico.ataquesMagico;

public class Magico extends Clase{
    public Magico() {
        super("Magico", 80, 25, 5, 120, ataquesMagico.ataquesBasicos());
    }
}
