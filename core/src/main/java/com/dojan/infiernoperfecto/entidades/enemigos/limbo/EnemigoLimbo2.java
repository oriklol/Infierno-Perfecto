package com.dojan.infiernoperfecto.entidades.enemigos.limbo;

import com.dojan.infiernoperfecto.ataques.enemigos.arraysLists.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;

public class EnemigoLimbo2 extends Enemigo {
    public EnemigoLimbo2() {
        super("Esbirro", 60, 8, 12, AtaquesComunes.ataquesBasicos());
    }
}
