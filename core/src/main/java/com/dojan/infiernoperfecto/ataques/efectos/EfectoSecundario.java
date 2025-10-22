package com.dojan.infiernoperfecto.ataques.efectos;

import com.dojan.infiernoperfecto.entidades.Personaje;

public abstract class EfectoSecundario {
    protected TipoEstadistica tipoEstadistica;
    protected int porcentajeMin;
    protected int porcentajeMax;
    protected int probabilidad;
    protected int turnosMin;
    protected int turnosMax;

    public EfectoSecundario(TipoEstadistica tipoEstadistica,int porcentajeMin, int porcentajeMax, int probabilidad, int turnosMin, int turnosMax) {
        this.tipoEstadistica = tipoEstadistica;
        this.porcentajeMin = porcentajeMin;
        this.porcentajeMax = porcentajeMax;
        this.probabilidad = probabilidad;
        this.turnosMin = turnosMin;
        this.turnosMax = turnosMax;
    }

    // Aplica el efecto en el objetivo y retorna una descripción del efecto para logs (o cadena vacía si no aplica)
    public abstract String aplicar(Personaje objetivo);

    public int getProbabilidad() {
        return probabilidad;
    }
}
