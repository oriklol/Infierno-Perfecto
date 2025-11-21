package com.dojan.infiernoperfecto.entidades.enemigos.codicia;

import com.dojan.infiernoperfecto.ataques.enemigos.arraysLists.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;

public class EnemigoCodicia1 extends Enemigo {
    public EnemigoCodicia1() {
        super("Guson", 65, 10, 14, AtaquesComunes.ataquesBasicos());
    }
}
