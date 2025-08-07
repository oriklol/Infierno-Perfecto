package com.dojan.infiernoperfecto.ataques.enemigos;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.efectos.ModificacionEstadistica;
import com.dojan.infiernoperfecto.ataques.efectos.TipoEstadistica;

public class Amenaza extends Ataque {
    public Amenaza() {
        super("Amenaza", 0, 70, 20, new ModificacionEstadistica(TipoEstadistica.DANIO,20,40,100,2,4));
    }
}
