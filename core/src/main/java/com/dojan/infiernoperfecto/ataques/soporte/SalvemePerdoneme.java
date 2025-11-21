package com.dojan.infiernoperfecto.ataques.soporte;

import com.dojan.infiernoperfecto.ataques.Ataque;

public class SalvemePerdoneme extends Ataque {
    public SalvemePerdoneme() {
        super("Salveme Perdoneme", 0, 60, 2, 80);
        // TODO: Si HP llega a 0 en los próximos 2 turnos, revive con 30% HP
    }
}
