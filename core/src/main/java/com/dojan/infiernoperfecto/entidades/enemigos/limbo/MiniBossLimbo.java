package com.dojan.infiernoperfecto.entidades.enemigos.limbo;

import com.dojan.infiernoperfecto.ataques.enemigos.arraysLists.AtaquesMiniBoss;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;

public class MiniBossLimbo extends Enemigo {
    public MiniBossLimbo() {
        super("Sabueso", 150, 12, 20, AtaquesMiniBoss.ataquesBasicos());
    }
}
