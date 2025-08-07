package com.dojan.infiernoperfecto.ataques.enemigos;

import com.dojan.infiernoperfecto.ataques.Ataque;

import java.util.Arrays;
import java.util.List;

public class AtaquesMiniBoss {
    public static List<Ataque> ataquesBasicos() {
        return Arrays.asList(
            new Agrandamiento(),
            new Amenaza(),
            new Mordisco(),
            new DobleGolpe()
        );
    }
}
