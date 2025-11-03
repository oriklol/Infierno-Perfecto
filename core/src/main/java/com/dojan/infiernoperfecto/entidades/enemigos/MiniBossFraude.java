package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.enemigos.AtaquesMiniBoss;
import com.dojan.infiernoperfecto.entidades.Enemigo;

public class MiniBossFraude extends Enemigo {
    public MiniBossFraude() {
        super("Sabueso", 300, 60, 65, AtaquesMiniBoss.ataquesBasicos());
    }
}
