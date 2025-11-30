package com.dojan.infiernoperfecto.ataques.efectos;

import com.dojan.infiernoperfecto.entidades.Personaje;

/**
 * Este efecto requiere modificaciones en la clase Batalla
 * para afectar a múltiples enemigos.
 * Por ahora es un placeholder.
 */
public class DanioATE extends EfectoSecundario {

    public DanioATE(int porcentajeDanio) {
        super(TipoEfecto.DANIO_AOE, porcentajeDanio, porcentajeDanio, 100);
    }

    @Override
    public String aplicar(Personaje objetivo) {
        // Esta mecánica requiere acceso a la lista de enemigos
        // Se implementará en una fase posterior
        return "Efecto AOE (implementación pendiente)";
    }
}
