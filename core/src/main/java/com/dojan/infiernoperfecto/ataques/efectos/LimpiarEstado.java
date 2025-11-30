package com.dojan.infiernoperfecto.ataques.efectos;

import com.dojan.infiernoperfecto.entidades.Personaje;

public class LimpiarEstado extends EfectoSecundario {

    public LimpiarEstado() {
        super(TipoEfecto.LIMPIAR_ESTADO, 0, 0, 100);
    }

    public LimpiarEstado(int probabilidad) {
        super(TipoEfecto.LIMPIAR_ESTADO, 0, 0, probabilidad);
    }

    @Override
    public String aplicar(Personaje objetivo) {
        if (objetivo.tieneEstadoAlterado()) {
            objetivo.limpiarEstadosAlterados();
            return objetivo.getNombre() + " se liberó de los efectos negativos";
        }
        return "";
    }
}
