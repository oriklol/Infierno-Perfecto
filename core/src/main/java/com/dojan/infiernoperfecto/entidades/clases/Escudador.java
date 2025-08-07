package com.dojan.infiernoperfecto.entidades.clases;

import com.dojan.infiernoperfecto.ataques.escudador.ataquesEscudador;

public class Escudador extends Clase {
    public Escudador() {
        super("Escudador", 200, 10, 20, ataquesEscudador.ataquesBasicos());
    }
}
