package com.dojan.infiernoperfecto.logica;

import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;
import com.dojan.infiernoperfecto.entidades.Personaje;
import com.dojan.infiernoperfecto.entidades.ResultadoAtaque;

import java.util.List;

public class ServicioCombate {

    /**
     * Ejecuta un ataque y retorna el resultado.
     * ESTA FUNCIÓN IRÁN AL SERVIDOR.
     */
    public ResultadoCombate ejecutarAtaque(
        Personaje atacante,
        Personaje objetivo,
        int ataqueIndex
    ) {
        // Validaciones
        if (!validarAtaque(atacante, objetivo, ataqueIndex)) {
            return ResultadoCombate.invalido("Ataque no válido");
        }

        // Ejecutar (código que ya tenés en Personaje.atacar)
        ResultadoAtaque resultado = atacante.atacar(objetivo, ataqueIndex);

        // Construir resultado completo
        return new ResultadoCombate(
            resultado.getDanio(),
            resultado.getEfectoMensaje(),
            !objetivo.sigueVivo(),
            objetivo.getVidaActual(),
            atacante.getFeActual()
        );
    }

    private boolean validarAtaque(Personaje atacante, Personaje objetivo, int ataqueIndex) {
        // Verificar que el objetivo esté vivo
        if (!objetivo.sigueVivo()) {
            return false;
        }

        // Verificar que el ataque exista
        if (ataqueIndex < 0 || ataqueIndex >= atacante.getAtaques().size()) {
            return false;
        }

        Ataque ataque = atacante.getAtaques().get(ataqueIndex);

        // Verificar usos
        if (ataque.getCantUsos() <= 0) {
            return false;
        }

        // Verificar Fe
        if (ataque.getCostoFe() > atacante.getFeActual()) {
            return false;
        }

        return true;
    }

    public boolean verificarFinBatalla(Personaje jugador, List<Enemigo> enemigos) {
        return !jugador.sigueVivo() || enemigos.isEmpty();
    }
}
