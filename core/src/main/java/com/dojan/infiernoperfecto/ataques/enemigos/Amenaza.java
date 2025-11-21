package com.dojan.infiernoperfecto.ataques.enemigos;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.efectos.ModificacionEstadistica;
import com.dojan.infiernoperfecto.ataques.efectos.TipoEstadistica;

public class Amenaza extends Ataque {
    public Amenaza() {
        super("Amenaza", 0, 80, 10,
            new ModificacionEstadistica(TipoEstadistica.DANIO, 15, 25, 100, 2, 3));
        // Reduce daño del jugador 15-25% por 2-3 turnos
    }
}
