package com.dojan.infiernoperfecto.entidades.enemigos.fraude;

import com.dojan.infiernoperfecto.ataques.enemigos.arraysLists.AtaquesMiniBoss;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;

public class MiniBossFraude extends Enemigo {
    public MiniBossFraude() {
        super("SinRRostro", 180, 10, 22, AtaquesMiniBoss.ataquesBasicos());
    }
}
