package com.dojan.infiernoperfecto.ataques.peleador;

import com.dojan.infiernoperfecto.ataques.Ataque;

import java.util.Arrays;
import java.util.List;

public class ataquesPeleador {

    public static List<Ataque> ataquesBasicos() {
        return Arrays.asList(
            new Afilamiento(),
            new Espadazo(),
            new GranGolpe(),
            new UltimoAliento()

        );
    }
}
