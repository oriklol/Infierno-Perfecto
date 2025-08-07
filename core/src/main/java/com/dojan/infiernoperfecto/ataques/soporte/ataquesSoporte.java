package com.dojan.infiernoperfecto.ataques.soporte;

import com.dojan.infiernoperfecto.ataques.Ataque;

import java.util.Arrays;
import java.util.List;

public class ataquesSoporte {
    public static List<Ataque> ataquesBasicos() {
        return Arrays.asList(
            new BesoDeAngel(),
            new EnElNombreDelPadre(),
            new RotacionAurea(),
            new LibrameDeAmor(),
            new SalvemePerdoneme()

        );
    }
}
