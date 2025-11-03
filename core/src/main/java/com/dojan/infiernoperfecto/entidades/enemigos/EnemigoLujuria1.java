package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.enemigos.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.Enemigo;

public class EnemigoLujuria1 extends Enemigo {
    public EnemigoLujuria1() {
        super("Onseno", 40, 30, 75, AtaquesComunes.ataquesBasicos());
    }
}
