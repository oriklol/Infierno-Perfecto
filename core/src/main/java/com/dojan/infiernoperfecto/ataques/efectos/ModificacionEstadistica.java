package com.dojan.infiernoperfecto.ataques.efectos;

import com.dojan.infiernoperfecto.entidades.Personaje;
import com.dojan.infiernoperfecto.entidades.estados.EstadoEstadisticaModificada;
import com.dojan.infiernoperfecto.utiles.Random;

public class ModificacionEstadistica extends EfectoSecundario {

    public ModificacionEstadistica(TipoEstadistica tipoEstadistica, int porcentajeMin,
                                   int porcentajeMax, int turnosMin, int turnosMax) {
        this(tipoEstadistica, porcentajeMin, porcentajeMax, 100, turnosMin, turnosMax);
    }

    public ModificacionEstadistica(TipoEstadistica tipoEstadistica, int porcentajeMin,
                                   int porcentajeMax, int probabilidad, int turnosMin, int turnosMax) {
        super(tipoEstadistica, porcentajeMin, porcentajeMax, probabilidad, turnosMin, turnosMax);
    }

    @Override
    public String aplicar(Personaje objetivo){
        int porcentaje = Random.generarEntero(Math.abs(this.porcentajeMin), Math.abs(this.porcentajeMax));

        // Mantener el signo original (negativo = buff, positivo = debuff)
        if (this.porcentajeMin < 0) {
            porcentaje = -porcentaje;
        }

        int turnos = Random.generarEntero(super.turnosMin, super.turnosMax);
        objetivo.aplicarEstadoAlterado(new EstadoEstadisticaModificada(this.tipoEstadistica, porcentaje, turnos));

        String accion = (porcentaje < 0) ? "aumenta" : "reduce";
        String mensaje = "Durante " + turnos + " turnos " + accion + " " +
            this.tipoEstadistica.toString().toLowerCase() + " en " +
            Math.abs(porcentaje) + "%";
        return mensaje;
    }

    public int getPorcentajeMin() {
        return porcentajeMin;
    }
}
