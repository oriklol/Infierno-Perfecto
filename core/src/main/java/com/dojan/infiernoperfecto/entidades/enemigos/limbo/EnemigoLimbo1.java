package com.dojan.infiernoperfecto.entidades.enemigos.limbo;

import com.dojan.infiernoperfecto.ataques.enemigos.arraysLists.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;

public class EnemigoLimbo1 extends Enemigo {
    public EnemigoLimbo1() {
        super("Mini on", 45, 5, 8, AtaquesComunes.ataquesBasicos());
    }
}
