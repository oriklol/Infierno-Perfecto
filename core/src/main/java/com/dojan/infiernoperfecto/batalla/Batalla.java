package com.dojan.infiernoperfecto.batalla;

import java.util.ArrayList;
import java.util.List;

import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;
import com.dojan.infiernoperfecto.entidades.Personaje;
import com.dojan.infiernoperfecto.entidades.ResultadoAtaque;
import com.dojan.infiernoperfecto.utiles.Random;

public class Batalla {
    private final Personaje jugador;
    private final List<Enemigo> enemigos;
    private int turno = 0;
    private String logCombate = "";
    private final List<Integer> enemigosMuertosEsteturno = new ArrayList<>();

    public Batalla(Personaje jugador, List<Enemigo> enemigos){
        this.jugador = jugador;
        this.enemigos = enemigos;
    }

    public void saltarTurnoJugador() {
        if (turno == 0) {
            turno = 1;
        }
    }

    /*
    Procesa un solo turno de la batalla. Retorna true si la batalla sigue, false si terminó.
    El primer turno (turno==0) es del jugador, los siguientes de los enemigos.
     */
    public boolean avanzarTurno(int opcE, int opcA) {
        enemigosMuertosEsteturno.clear();
        logCombate = "";

        if (turno == 0) {
            turnoJugador(opcE, opcA);
        } else {
            int indiceEnemigo = turno - 1;
            if (indiceEnemigo < enemigos.size()) {
                Enemigo enemigo = enemigos.get(indiceEnemigo);
                if (enemigo.sigueVivo()) {
                    turnoEnemigo(enemigo);
                }
            }
        }

        turno++;

        // Al final de cada ronda completa, eliminar enemigos muertos
        if (turno > enemigos.size()) {
            //enemigos.removeIf(enemigo -> !enemigo.sigueVivo());
            turno = 0;
            return !jugador.sigueVivo() || enemigos.isEmpty();
        }

        return false;
    }

    public boolean batallaTerminada() {
        return !jugador.sigueVivo() || enemigos.isEmpty();
    }

    private void turnoJugador(int opcE, int opcA) {
        Enemigo objetivo = enemigos.get(opcE);

        ResultadoAtaque resultado = jugador.atacar(objetivo, opcA);
        float danioReal = resultado.getDanio();
        logCombate = "Atacaste a " + objetivo.getNombre() + " he hiciste " + danioReal + " de daño.\n";
        if (resultado.getEfectoMensaje() != null && !resultado.getEfectoMensaje().isEmpty()) {
            logCombate += resultado.getEfectoMensaje() + "\n";
        }

        if(!objetivo.sigueVivo()){
            System.out.println("murio el objetivo: "+objetivo.getNombre());
            enemigosMuertosEsteturno.add(opcE);
        }
    }

    private void turnoEnemigo(Enemigo enemigo){
        int ataqueEnemigo = Random.generarEntero(enemigo.getAtaques().size());
        ResultadoAtaque resultado = enemigo.atacar(jugador, ataqueEnemigo);
        float danioReal = resultado.getDanio();
        String nombreAtaque = "";
        if (enemigo.getAtaques() != null && !enemigo.getAtaques().isEmpty() && ataqueEnemigo >= 0 && ataqueEnemigo < enemigo.getAtaques().size()) {
            nombreAtaque = enemigo.getAtaques().get(ataqueEnemigo).getNombre();
        }
        if (!nombreAtaque.isEmpty()) {
            logCombate += enemigo.getNombre() + " usó " + nombreAtaque + " y te hizo " + danioReal + " de daño.\n";
        } else {
            logCombate += enemigo.getNombre() + " te hizo " + danioReal + " de daño.\n";
        }
        // Si el ataque aplicó un efecto con mensaje, agregarlo en la línea siguiente
        if (resultado.getEfectoMensaje() != null && !resultado.getEfectoMensaje().isEmpty()) {
            logCombate += resultado.getEfectoMensaje() + "\n";
        }
    }

    public String getLogCombate() {
        return logCombate;
    }

    public int getTurno() {
        return turno;
    }

    public List<Integer> getEnemigosMuertosEsteTurno() {
        return new ArrayList<>(enemigosMuertosEsteturno);
    }

    public Personaje getJugador() {
        return jugador;
    }

    public List<Enemigo> getEnemigos() {
        return enemigos;
    }
}
