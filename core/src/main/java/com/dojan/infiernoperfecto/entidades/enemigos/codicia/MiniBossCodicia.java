package com.dojan.infiernoperfecto.entidades.enemigos.codicia;

import com.dojan.infiernoperfecto.ataques.enemigos.arraysLists.AtaquesMiniBoss;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;

public class MiniBossCodicia extends Enemigo {
    public MiniBossCodicia() {
        super("Glormiga", 220, 15, 25, AtaquesMiniBoss.ataquesBasicos());
    }
}
