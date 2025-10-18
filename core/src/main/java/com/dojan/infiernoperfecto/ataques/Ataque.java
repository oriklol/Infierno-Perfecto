package com.dojan.infiernoperfecto.ataques;

import com.dojan.infiernoperfecto.ataques.efectos.EfectoSecundario;
import com.dojan.infiernoperfecto.utiles.Random;

public abstract class Ataque {
    protected String nombre;
    protected int danio;
    protected int precision;
    protected int cantUsos;
    protected EfectoSecundario efectoSecundario;
    protected int costoFe;

    //sin efecto secundario y sin costo de Fe
    public Ataque(String nombre, int danio, int precision, int cantUsos) {
        this.nombre = nombre;
        this.danio = danio;
        this.precision = precision;
        this.cantUsos = cantUsos;
        this.efectoSecundario = null;
    }

    //con efecto secundario pero sin costo de Fe
    public Ataque(String nombre, int danio, int precision, int cantUsos, EfectoSecundario efectoSecundario) {
        this.nombre = nombre;
        this.danio = danio;
        this.precision = precision;
        this.cantUsos = cantUsos;
        this.efectoSecundario = efectoSecundario;
    }

    //con efecto secundario y con costo de Fe
    public Ataque(String nombre, int danio, int precision, int cantUsos, EfectoSecundario efectoSecundario, int costoFe) {
        this.nombre = nombre;
        this.danio = danio;
        this.precision = precision;
        this.cantUsos = cantUsos;
        this.efectoSecundario = efectoSecundario;
        this.costoFe = costoFe;
    }

    //sin efecto secundario pero con costo de Fe
    public Ataque(String nombre, int danio, int precision, int cantUsos, int costoFe) {
        this.nombre = nombre;
        this.danio = danio;
        this.precision = precision;
        this.cantUsos = cantUsos;
        this.efectoSecundario = null;
        this.costoFe = costoFe;
    }

    public boolean verificarAcierto(){
        return Random.verificarAcierto(precision);
    }

    public int getDanio() {
        return danio;
    }

    public void restarUso(){
        if(cantUsos>0){
            this.cantUsos--;
        }
    }

    public int getCantUsos() {
        return cantUsos;
    }

    public int getProbabilidadAcierto() {
        return precision;
    }

    public EfectoSecundario getEfectoSecundario() {
        return efectoSecundario;
    }

    public String getNombre() {
        return this.nombre;
    }

    public int getCostoFe() {
        return costoFe;
    }


    //public abstract void usar(Personaje atacante, Personaje objetivo);
}
