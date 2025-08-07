package com.dojan.infiernoperfecto.entidades.estados;

public abstract class EstadoAlterado {
    protected int turnos;

    public EstadoAlterado(int turnos){
        this.turnos = turnos;
    }

    public void reducirTurno(){
        --this.turnos;
    }

    public int getTurnos() {
        return turnos;
    }
}
