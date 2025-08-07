package com.dojan.infiernoperfecto.entidades.clases;


import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.entidades.Personaje;

import java.util.ArrayList;
import java.util.List;

public abstract class Clase{
    protected String nombre;
    private int vidaBase;
    private int danioBase;
    private int defensaBase;
    protected List<Ataque> ataques = new ArrayList<>();

    public Clase(String nombre, int vidaBase, int ataqueBase, int defensaBase, List<Ataque> ataques) {
        this.nombre = nombre;
        this.vidaBase = vidaBase;
        this.danioBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.ataques = ataques;
    }

    public int getDanioBase() {
        return danioBase;
    }

    public int getDefensaBase() {
        return defensaBase;
    }

    public int getVidaBase() {
        return vidaBase;
    }

    public List<Ataque> getAtaques() {
        return ataques;
    }
}
