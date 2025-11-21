package com.dojan.infiernoperfecto.ataques.enemigos.arraysLists;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.enemigos.Agrandamiento;
import com.dojan.infiernoperfecto.ataques.enemigos.Amenaza;
import com.dojan.infiernoperfecto.ataques.enemigos.DobleGolpe;
import com.dojan.infiernoperfecto.ataques.enemigos.Mordisco;

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
