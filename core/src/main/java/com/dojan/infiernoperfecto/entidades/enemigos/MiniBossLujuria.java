package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.enemigos.AtaquesMiniBoss;
import com.dojan.infiernoperfecto.entidades.Enemigo;

public class MiniBossLujuria extends Enemigo {
    public MiniBossLujuria() {
        super("Jujurion", 440, 67, 85, AtaquesMiniBoss.ataquesBasicos());
    }
}
