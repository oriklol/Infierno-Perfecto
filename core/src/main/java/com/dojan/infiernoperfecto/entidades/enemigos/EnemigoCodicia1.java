package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.enemigos.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.Enemigo;

public class EnemigoCodicia1 extends Enemigo {
    public EnemigoCodicia1() {
        super("Guson", 75, 60, 35, AtaquesComunes.ataquesBasicos());
    }
}
