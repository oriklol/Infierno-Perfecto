package com.dojan.infiernoperfecto.ataques.peleador;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.efectos.ModificacionEstadistica;
import com.dojan.infiernoperfecto.ataques.efectos.TipoEstadistica;

public class Afilamiento extends Ataque {
    public Afilamiento() {
        // Aumenta daño propio 30-50% por 3-4 turnos
        // NOTA: Usar valores NEGATIVOS para BUFFS (aumentos)
        super("Afilamiento", 0, 100, 3,
            new ModificacionEstadistica(TipoEstadistica.DANIO, -30, -50, 100, 3, 4));
    }
}
