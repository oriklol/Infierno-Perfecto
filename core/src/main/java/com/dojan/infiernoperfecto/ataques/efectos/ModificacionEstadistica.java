package com.dojan.infiernoperfecto.ataques.efectos;

import com.dojan.infiernoperfecto.entidades.Personaje;
import com.dojan.infiernoperfecto.entidades.estados.EstadoEstadisticaModificada;
import com.dojan.infiernoperfecto.utiles.Random;

public class ModificacionEstadistica extends EfectoSecundario{
    private int porcentajeMin;
    private int porcentajeMax;

    public ModificacionEstadistica(TipoEstadistica tipoEstadistica, int porcentajeMin, int porcentajeMax, int turnosMin, int turnosMax) {
        this(tipoEstadistica, porcentajeMin, porcentajeMax, 100, turnosMin, turnosMax);
    }

    public ModificacionEstadistica(TipoEstadistica tipoEstadistica, int porcentajeMin, int porcentajeMax, int probabilidad, int turnosMin, int turnosMax) {
        super(tipoEstadistica, porcentajeMin,porcentajeMax, probabilidad, turnosMin, turnosMax);
        this.porcentajeMin = porcentajeMin;
        this.porcentajeMax = porcentajeMax;
    }

    @Override
    public String aplicar(Personaje objetivo){
        int porcentaje = Random.generarEntero(this.porcentajeMin,this.porcentajeMax);
        int turnos = Random.generarEntero(super.turnosMin,super.turnosMax);
        objetivo.aplicarEstadoAlterado(new EstadoEstadisticaModificada(this.tipoEstadistica, porcentaje, turnos));
        String mensaje = "Durante " + turnos + " turnos modifica " + this.tipoEstadistica.toString().toLowerCase() + " en " + porcentaje + "%";
        return mensaje;
    }
}
