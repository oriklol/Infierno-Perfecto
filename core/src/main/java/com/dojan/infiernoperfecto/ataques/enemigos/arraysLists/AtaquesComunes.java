package com.dojan.infiernoperfecto.ataques.enemigos.arraysLists;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.enemigos.Agrandamiento;
import com.dojan.infiernoperfecto.ataques.enemigos.Amenaza;
import com.dojan.infiernoperfecto.ataques.enemigos.Mordisco;
import com.dojan.infiernoperfecto.ataques.enemigos.Rasgunio;

import java.util.Arrays;
import java.util.List;

public class AtaquesComunes {

    public static List<Ataque> ataquesBasicos() {
        return Arrays.asList(
            new Agrandamiento(),
            new Amenaza(),
            new Mordisco(),
            new Rasgunio()
        );
    }

    public static List<Ataque> soloAgrandamiento() {
        return Arrays.asList(
            new Agrandamiento()
        );
    }

}
