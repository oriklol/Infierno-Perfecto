package com.dojan.infiernoperfecto.ataques.soporte;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.efectos.Curacion;

public class EnElNombreDelPadre extends Ataque {
    public EnElNombreDelPadre() {
        // Hace 40 de daño Y cura 40-50 HP al usuario, costo 50 Fe
        super("En el nombre del Padre", 40, 90, 2, new Curacion(40, 50), 50);
    }
}
