package com.dojan.infiernoperfecto.entidades.clases;


import java.util.ArrayList;
import java.util.List;

import com.dojan.infiernoperfecto.ataques.Ataque;

public abstract class Clase{
    protected String nombre;
    private int vidaBase;
    private int danioBase;
    private int defensaBase;
    private int feBase;
    protected List<Ataque> ataques = new ArrayList<>();

    public Clase(String nombre, int vidaBase, int ataqueBase, int defensaBase, int feBase, List<Ataque> ataques) {
        this.nombre = nombre;
        this.vidaBase = vidaBase;
        this.danioBase = ataqueBase;
        this.defensaBase = defensaBase;
        this.feBase = feBase;
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

    public int getFeBase() {
        return feBase;
    }
}
