package com.dojan.infiernoperfecto.ataques.soporte;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.ataques.efectos.LimpiarEstado;

public class LibrameDeAmor extends Ataque {
    public LibrameDeAmor() {
        // Limpia estados alterados, 80% precisión, 4 usos, 20 Fe
        super("Librame de Amor", 0, 80, 4, new LimpiarEstado(), 20);
    }
}
