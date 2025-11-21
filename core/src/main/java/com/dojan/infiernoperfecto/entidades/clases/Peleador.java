package com.dojan.infiernoperfecto.entidades.clases;

import com.dojan.infiernoperfecto.ataques.peleador.ataquesPeleador;

public class Peleador extends Clase{
    public Peleador() {
        super("Peleador", 120, 15, 8, 50, 200, ataquesPeleador.ataquesBasicos());
    } // Modificar estadisticas ataqueBase y vidaBase en caso de qe se quiera probar el juego sin probleas
}
