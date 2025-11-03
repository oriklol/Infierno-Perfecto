package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.enemigos.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.Enemigo;

public class EnemigoFraude2 extends Enemigo {
    public EnemigoFraude2() {
        super("CangreSaura", 35, 20, 75, AtaquesComunes.ataquesBasicos());
    }
}
