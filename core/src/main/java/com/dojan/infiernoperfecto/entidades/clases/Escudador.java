package com.dojan.infiernoperfecto.entidades.clases;

import com.dojan.infiernoperfecto.ataques.escudador.ataquesEscudador;

public class Escudador extends Clase {
    public Escudador() {
        super("Escudador", 180, 8, 25, 40, 200, ataquesEscudador.ataquesBasicos());
    }
}
