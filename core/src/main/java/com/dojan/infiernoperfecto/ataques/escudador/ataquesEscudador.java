package com.dojan.infiernoperfecto.ataques.escudador;

import com.dojan.infiernoperfecto.ataques.Ataque;

import java.util.Arrays;
import java.util.List;

public class ataquesEscudador {
    public static List<Ataque> ataquesBasicos() {
        return Arrays.asList(
            new CastigoAlBarbaro(),
            new DanzaConElDiablo(),
            new SienteMiDolor(),
            new TodoONada()

        );
    }
}
