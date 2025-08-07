package com.dojan.infiernoperfecto.ataques.magico;

import com.dojan.infiernoperfecto.ataques.Ataque;

import java.util.Arrays;
import java.util.List;

public class ataquesMagico {

    public static List<Ataque> ataquesBasicos() {
        return Arrays.asList(
            new BañoTotal(),
            new BolaDeFuego(),
            new Concentracion(),
            new HechizoBasico()

        );
    }
}
