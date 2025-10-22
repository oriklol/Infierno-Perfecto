package com.dojan.infiernoperfecto.batalla;

import java.util.ArrayList;
import java.util.List;

import com.dojan.infiernoperfecto.entidades.Enemigo;
import com.dojan.infiernoperfecto.entidades.Personaje;
import com.dojan.infiernoperfecto.utiles.Random;

public class Batalla {
    private final Personaje jugador;
    private final List<Enemigo> enemigos;
    private int turno = 0;
    private String logCombate = "";
    private final List<Integer> enemigosMuertosEsteturno = new ArrayList<>(); // ← NUEVO

    public Batalla(Personaje jugador, List<Enemigo> enemigos){
        this.jugador = jugador;
        this.enemigos = enemigos;
    }

    /**
     * Procesa un solo turno de la batalla. Retorna true si la batalla sigue, false si terminó.
     * El primer turno (turno==0) es del jugador, los siguientes de los enemigos.
     */
    public boolean avanzarTurno(int opcE, int opcA) {
        enemigosMuertosEsteturno.clear(); // ← NUEVO: Limpiar la lista al inicio del turno

        if (turno == 0) {
            turnoJugador(opcE, opcA);
        } else {
            // Ajustar el índice porque pueden haber muerto enemigos
            int indiceEnemigo = turno - 1;
            if (indiceEnemigo < enemigos.size()) {
                Enemigo enemigo = enemigos.get(indiceEnemigo);
                if (enemigo.sigueVivo()) {
                    turnoEnemigo(enemigo);
                }
            }
        }

        turno++;
        // Cuando termina la ronda de enemigos, antes de volver al jugador, verifica si la batalla terminó
        if (turno > enemigos.size()) {
            turno = 0;
            // Verifica si la batalla terminó solo aquí
            return !jugador.sigueVivo() || enemigos.isEmpty();
        }
        // Mientras se ejecutan los turnos enemigos, nunca termina la batalla
        return false;
    }

    public boolean batallaTerminada() {
        return !jugador.sigueVivo() || enemigos.isEmpty();
    }

    private void turnoJugador(int opcE, int opcA) {
        Enemigo objetivo = enemigos.get(opcE);

        float danioReal = jugador.atacar(objetivo, opcA);
        logCombate = "Atacaste a " + objetivo.getNombre() + " he hiciste " + danioReal + " de daño.\n";

        if(!objetivo.sigueVivo()){
            System.out.println("murio el objetivo: "+objetivo.getNombre());
            enemigosMuertosEsteturno.add(opcE); // ← NUEVO: Guardar el índice en lugar de eliminar
            // NO ELIMINAR AQUÍ: enemigos.remove(objetivo);
        }
    }

    private void turnoEnemigo(Enemigo enemigo){
        int ataqueEnemigo = Random.generarEntero(enemigo.getAtaques().size());
        float danioReal = enemigo.atacar(jugador, ataqueEnemigo);
        logCombate += enemigo.getNombre() + " te hizo " + danioReal + " de daño.\n";
    }

    public String getLogCombate() {
        return logCombate;
    }

    public int getTurno() {
        return turno;
    }

    // ← NUEVO: Método para obtener los enemigos que murieron este turno
    public List<Integer> getEnemigosMuertosEsteTurno() {
        return new ArrayList<>(enemigosMuertosEsteturno);
    }
}
