package com.dojan.infiernoperfecto.entidades.estados;

import com.dojan.infiernoperfecto.ataques.efectos.TipoEstadistica;

import java.io.PrintStream;

public class EstadoEstadisticaModificada extends EstadoAlterado{
    private TipoEstadistica tipoEstadistica;
    private int porcentaje;

    public EstadoEstadisticaModificada(TipoEstadistica tipoEstadistica, int porcentaje, int turnos) {
        super(turnos);
        this.tipoEstadistica = tipoEstadistica;
        this.porcentaje = porcentaje;
    }

    //BORRA LUEGO
    public void mostrarInformacion() {
        PrintStream var10000 = System.out;
        String var10001 = this.tipoEstadistica.toString().toLowerCase();
        var10000.println("El Objetivo ha sido afectado por una modificación de la estadística " + var10001 + " en un " + this.porcentaje + "% y restan " + this.turnos + " turnos.");
    }

    public TipoEstadistica getTipoEstadistica() {
        return this.tipoEstadistica;
    }

    public int getPorcentaje() {
        return this.porcentaje;
    }
}
