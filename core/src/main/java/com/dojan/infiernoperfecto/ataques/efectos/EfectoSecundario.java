package com.dojan.infiernoperfecto.ataques.efectos;

import com.dojan.infiernoperfecto.entidades.Personaje;

public abstract class EfectoSecundario {
    protected TipoEstadistica tipoEstadistica;
    protected TipoEfecto tipoEfecto;  // ← NUEVO
    protected int porcentajeMin;
    protected int porcentajeMax;
    protected int probabilidad;
    protected int turnosMin;
    protected int turnosMax;

    // Constructor para efectos con estadísticas (buffs/debuffs)
    public EfectoSecundario(TipoEstadistica tipoEstadistica, int porcentajeMin, int porcentajeMax,
                            int probabilidad, int turnosMin, int turnosMax) {
        this.tipoEstadistica = tipoEstadistica;
        this.tipoEfecto = TipoEfecto.MODIFICACION_STAT;
        this.porcentajeMin = porcentajeMin;
        this.porcentajeMax = porcentajeMax;
        this.probabilidad = probabilidad;
        this.turnosMin = turnosMin;
        this.turnosMax = turnosMax;
    }

    // Constructor para efectos especiales (curación, AOE, etc) ← NUEVO
    public EfectoSecundario(TipoEfecto tipoEfecto, int valorMin, int valorMax, int probabilidad) {
        this.tipoEfecto = tipoEfecto;
        this.porcentajeMin = valorMin;
        this.porcentajeMax = valorMax;
        this.probabilidad = probabilidad;
        this.turnosMin = 0;
        this.turnosMax = 0;
    }

    public abstract String aplicar(Personaje objetivo);

    public int getProbabilidad() {
        return probabilidad;
    }

    public TipoEfecto getTipoEfecto() {
        return tipoEfecto;
    }
}
