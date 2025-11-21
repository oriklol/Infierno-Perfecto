package com.dojan.infiernoperfecto.ataques.escudador;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.efectos.ModificacionEstadistica;
import com.dojan.infiernoperfecto.ataques.efectos.TipoEstadistica;

public class DanzaConElDiablo extends Ataque {
    public DanzaConElDiablo() {
        super("Danza con el Diablo", 0, 100, 3,
            new ModificacionEstadistica(TipoEstadistica.DEFENSA, -40, -60, 100, 2, 3));
        // Aumenta defensa propia 40-60% por 2-3 turnos
    }
}
