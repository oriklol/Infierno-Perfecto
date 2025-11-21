package com.dojan.infiernoperfecto.entidades.enemigos.traicion;

import com.dojan.infiernoperfecto.ataques.enemigos.arraysLists.AtaquesBossFinal;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;

public class BossFinal extends Enemigo {
    public BossFinal() {
        super("Caos", 400, 20, 35, AtaquesBossFinal.ataquesBasicos());
    }
}
