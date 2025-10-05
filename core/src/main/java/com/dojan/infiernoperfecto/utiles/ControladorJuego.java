package com.dojan.infiernoperfecto.utiles;

import com.dojan.infiernoperfecto.pantallas.PantallaGameOver;
import com.dojan.infiernoperfecto.pantallas.PantallaLimbo;
import com.dojan.infiernoperfecto.pantallas.PantallaSiguienteNivel;

public class ControladorJuego {
    private static ControladorJuego instancia;

    private final int NIVEL_MAX = 4;
    private final int PISO_MAX = 9;
    private final int NIVEL_TIENDA = 3;

    private int nivelActual = 1;
    private int pisoActual = 1;

    private PantallaLimbo pantallaLimbo;
//    private PantallaTienda pantallaTienda;

    private ControladorJuego() {

    }

    public static ControladorJuego getInstance() {
        if (instancia == null) {
            instancia = new ControladorJuego();
        }
        return instancia;
    }

    private void cargarNivel() {
        // Actualizar variables globales
        Config.piso = pisoActual;
        Config.nivel = nivelActual;

        // Reutilizar o crear la pantalla de batalla
        if (pantallaLimbo == null) {
            pantallaLimbo = new PantallaLimbo();
        }

        // Reiniciar la pantalla con los nuevos datos
        pantallaLimbo.reiniciarNivel(pisoActual, nivelActual);

        // Cambiar a la pantalla de batalla
        Render.app.setScreen(pantallaLimbo);
    }

    public void iniciarJuego() {
        pisoActual = 1;
        nivelActual = 1;

        // Limpiar cache de pantallas para empezar limpio
        pantallaLimbo = null;
//        pantallaTienda = null;

        cargarNivel();
    }

    public void avanzarNivel() {
        this.nivelActual++;

        // Verificar si completamos todos los niveles del piso
        if (nivelActual > this.NIVEL_MAX) {
            // COMPLETASTE el piso
            Render.app.setScreen(new PantallaSiguienteNivel());
            this.pisoActual++;
            this.nivelActual = 1;

            if (pisoActual == this.NIVEL_TIENDA) {
                //irATienda();
            } else {
                // COMPLETASTE EL JUEGO

            }
        } else {
            // Todavía hay niveles en este piso
            cargarNivel();
        }
    }

//    private void irATienda() {
//        if (pantallaTienda == null) {
//            pantallaTienda = new PantallaTienda();
//        }
//
//        Render.app.setScreen(pantallaTienda);
//    }

    public void gameOver() {
        Render.app.setScreen(new PantallaGameOver());
    }


}
