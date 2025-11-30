package com.dojan.infiernoperfecto.ataques.magico;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.efectos.RecuperarFe;

public class Concentracion extends Ataque {
    public Concentracion() {
        // Recupera 20-30 Fe, sin costo
        super("Concentracion", 0, 100, 5, new RecuperarFe(20, 30));
    }
}
