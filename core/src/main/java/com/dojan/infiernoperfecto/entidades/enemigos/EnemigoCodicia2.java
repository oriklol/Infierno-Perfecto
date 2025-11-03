package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.enemigos.AtaquesComunes;
import com.dojan.infiernoperfecto.entidades.Enemigo;

public class EnemigoCodicia2 extends Enemigo {
    public EnemigoCodicia2() {
        super("UltraBean", 75, 60, 35, AtaquesComunes.ataquesBasicos());
    }
}
