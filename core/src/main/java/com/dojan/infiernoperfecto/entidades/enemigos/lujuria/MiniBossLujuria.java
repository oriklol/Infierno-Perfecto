package com.dojan.infiernoperfecto.entidades.enemigos.lujuria;

import com.dojan.infiernoperfecto.ataques.enemigos.arraysLists.AtaquesMiniBoss;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;

public class MiniBossLujuria extends Enemigo {
    public MiniBossLujuria() {
        super("Jujurion", 280, 18, 28, AtaquesMiniBoss.ataquesBasicos());
    }
}
