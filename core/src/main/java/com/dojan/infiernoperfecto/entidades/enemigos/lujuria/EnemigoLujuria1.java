package com.dojan.infiernoperfecto.entidades.enemigos.lujuria;

import com.dojan.infiernoperfecto.ataques.enemigos.arraysLists.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;

public class EnemigoLujuria1 extends Enemigo {
    public EnemigoLujuria1() {
        super("Onseno", 50, 5, 20, AtaquesComunes.ataquesBasicos());
    }
}
