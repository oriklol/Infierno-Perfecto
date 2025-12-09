package com.dojan.infiernoperfecto.ataques.soporte;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.magico.Concentracion;

import java.util.Arrays;
import java.util.List;

public class ataquesSoporte {
    public static List<Ataque> ataquesBasicos() {
        return Arrays.asList(
            new BesoDeAngel(),
            new EnElNombreDelPadre(),
            //new RotacionAurea(), // no terminado
            new LibrameDeAmor(),
            new Concentracion() // ataque del magico para recuperar fe
            //new SalvemePerdoneme() // no terminado

        );
    }
}
