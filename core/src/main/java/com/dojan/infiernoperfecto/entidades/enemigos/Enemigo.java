package com.dojan.infiernoperfecto.entidades.enemigos;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.entidades.Personaje;

import java.util.ArrayList;
import java.util.List;

public abstract class Enemigo extends Personaje {
    private String nombre;
    private int vidaBase;
    private int defensaBase;
    private int danioBase;
    protected List<Ataque> ataques = new ArrayList<>();

    public Enemigo(String nombre, int vida, int defensa, int danio, List<Ataque> ataques) {
        super(nombre, vida, danio, defensa, ataques);
    }
}
