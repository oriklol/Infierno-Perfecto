package com.dojan.infiernoperfecto.ataques.soporte;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.efectos.Curacion;

public class BesoDeAngel extends Ataque {
    public BesoDeAngel() {
        // Cura 25-35 HP, costo 15 Fe
        super("Beso de Angel", 0, 100, 15, new Curacion(25, 35), 15);
    }
}
