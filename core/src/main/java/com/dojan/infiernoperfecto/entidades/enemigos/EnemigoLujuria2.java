package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.enemigos.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.Enemigo;

public class EnemigoLujuria2 extends Enemigo {
    public EnemigoLujuria2() {
        super("Mascara", 80, 40, 35, AtaquesComunes.ataquesBasicos());
    }
}
