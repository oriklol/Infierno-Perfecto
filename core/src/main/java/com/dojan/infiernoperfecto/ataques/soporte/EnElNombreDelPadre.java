package com.dojan.infiernoperfecto.ataques.soporte;

import com.dojan.infiernoperfecto.ataques.Ataque;

public class EnElNombreDelPadre extends Ataque {
    public EnElNombreDelPadre() {
        super("En el nombre del Padre", 40, 90, 2, 50);
        // Daño 40 al enemigo, costo 50 Fe
        // TODO: También cura 40 HP al usarlo
    }
}
