package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.enemigos.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.Enemigo;

public class EnemigoFraude1 extends Enemigo {
    public EnemigoFraude1() {
        super("Bebon", 65, 60, 35, AtaquesComunes.ataquesBasicos());
    }
}
