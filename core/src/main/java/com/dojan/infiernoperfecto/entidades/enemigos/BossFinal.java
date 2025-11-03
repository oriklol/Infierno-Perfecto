package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.enemigos.AtaquesMiniBoss;
import com.dojan.infiernoperfecto.entidades.Enemigo;

public class BossFinal extends Enemigo {
    public BossFinal() {
        super("Caos", 500, 70, 80, AtaquesMiniBoss.ataquesBasicos());
    }
}
