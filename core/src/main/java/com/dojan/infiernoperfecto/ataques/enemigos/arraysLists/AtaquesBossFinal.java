package com.dojan.infiernoperfecto.ataques.enemigos.arraysLists;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.enemigos.Agrandamiento;
import com.dojan.infiernoperfecto.ataques.enemigos.Amenaza;
import com.dojan.infiernoperfecto.ataques.enemigos.DobleGolpe;
import com.dojan.infiernoperfecto.ataques.enemigos.GolpeBrutal;

import java.util.Arrays;
import java.util.List;

public class AtaquesBossFinal {
    public static List<Ataque> ataquesBasicos() {
        return Arrays.asList(
            new Agrandamiento(),
            new Amenaza(),
            new DobleGolpe(),
            new GolpeBrutal()
        );
    }
}
