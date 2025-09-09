package com.dojan.infiernoperfecto.batalla;

import java.util.List;

import com.dojan.infiernoperfecto.entidades.Enemigo;
import com.dojan.infiernoperfecto.entidades.Personaje;
import com.dojan.infiernoperfecto.utiles.Random;

public class Batalla {
    private final Personaje jugador;
    private final List<Enemigo> enemigos;
    private  int turno = 0;
    private String logCombate = "";

    public Batalla(Personaje jugador, List<Enemigo> enemigos){
        this.jugador = jugador;
        this.enemigos = enemigos;
    }

    /**
     * Procesa un solo turno de la batalla. Retorna true si la batalla sigue, false si terminó.
     * El primer turno (turno==0) es del jugador, los siguientes de los enemigos.
     */
    public boolean avanzarTurno(int opcE, int opcA) {
        if (!jugador.sigueVivo() || enemigos.isEmpty()) {
            return false; // batalla terminada
        }

        if (turno == 0) {
            System.out.println("Turno del jugador 1");
            turnoJugador(opcE, opcA);
        } else {
            System.out.println("Turno del enemigo " + turno);
            Enemigo enemigo = enemigos.get(turno - 1);
            if (enemigo.sigueVivo()) {
                turnoEnemigo(enemigo);
            }
        }

        turno++;
        if (turno > enemigos.size()) {
            turno = 0;
        }

        return jugador.sigueVivo() && !enemigos.isEmpty();
    }

    public boolean batallaTerminada() {
        return !jugador.sigueVivo() || enemigos.isEmpty();
    }

    private void turnoJugador(int opcE, int opcA) {
        Enemigo objetivo = enemigos.get(opcE);

        float danioReal = jugador.atacar(objetivo, opcA); // Solo una llamada
        logCombate = "Atacaste a " + objetivo.getNombre() + " e hiciste " + danioReal + " de daño.\n";

        if(!objetivo.sigueVivo()){
            System.out.println("murio el objetivo: "+objetivo.getNombre());
            enemigos.remove(objetivo);
        }
    }

    private void turnoEnemigo(Enemigo enemigo){
        int ataqueEnemigo = Random.generarEntero(enemigo.getAtaques().size());
        float danioReal = enemigo.atacar(jugador, ataqueEnemigo); // Usá el daño real
        logCombate += enemigo.getNombre() + " te hizo " + danioReal + " de daño.\n";
    }

    public String getLogCombate() {
        return logCombate;
    }

    public int getTurno() {
        return turno;
    }
}
