package com.dojan.infiernoperfecto.utiles;

import com.dojan.infiernoperfecto.pantallas.PantallaGameOver;
import com.dojan.infiernoperfecto.pantallas.PantallaSiguientePiso;
import com.dojan.infiernoperfecto.pantallas.PantallaTienda;
import com.dojan.infiernoperfecto.pantallas.PantallaVictoria;
import com.dojan.infiernoperfecto.pantallas.niveles.unjugador.PantallaCodicia;
import com.dojan.infiernoperfecto.pantallas.niveles.unjugador.PantallaFraude;
import com.dojan.infiernoperfecto.pantallas.niveles.unjugador.PantallaLimbo;
import com.dojan.infiernoperfecto.pantallas.niveles.unjugador.PantallaLujuria;
import com.dojan.infiernoperfecto.pantallas.niveles.unjugador.PantallaTraicion;
import com.dojan.infiernoperfecto.pantallas.niveles.multijugador.PantallaLimboMulti;
import com.dojan.infiernoperfecto.pantallas.niveles.multijugador.PantallaFraudeMulti;
import com.dojan.infiernoperfecto.pantallas.niveles.multijugador.PantallaCodiciaMulti;
import com.dojan.infiernoperfecto.pantallas.niveles.multijugador.PantallaLujuriaMulti;
import com.dojan.infiernoperfecto.pantallas.niveles.multijugador.PantallaTraicionMulti;

public class ControladorJuego {

    private static ControladorJuego instancia;

    private final int NIVEL_MAX = 4;
    private final int PISO_MAX = 5;
    private final int NIVEL_TIENDA = 3;

    private int nivelActual = 1;
    private int pisoActual = 1;
    
    private boolean modoMultijugador = false;

    private PantallaLimbo pantallaLimbo;
    private PantallaFraude pantallaFraude;  // ← AGREGAR
    private PantallaCodicia pantallaCodicia;
    private PantallaLujuria pantallaLujuria;
    private PantallaTraicion pantallaTraicion;
    private PantallaTienda pantallaTienda;

    private static final int MONEDAS_GANADAS_POR_NIVEL = 100;

    private ControladorJuego() {

    }

    public static ControladorJuego getInstance() {
        if (instancia == null) {
            instancia = new ControladorJuego();
        }
        return instancia;
    }
    
    public void setModoMultijugador(boolean modo) {
        this.modoMultijugador = modo;
    }
    
    public boolean isModoMultijugador() {
        return this.modoMultijugador;
    }


    // fuuncion para cargar el nivel actual
    public void cargarNivel() {
        // Actualizar variables globales
        if (modoMultijugador) {
            this.pisoActual = Config.piso;
            this.nivelActual = Config.nivel;
        } else {
            Config.piso = pisoActual;
            Config.nivel = nivelActual;
        }

        if (modoMultijugador) {
            // LÓGICA MULTIJUGADOR
            switch (pisoActual) {
                case 1:
                    PantallaLimboMulti pantallaLimboMulti = new PantallaLimboMulti(); 
                    Render.app.setScreen(pantallaLimboMulti);
                    break;
                case 2:
                    PantallaFraudeMulti pantallaFraudeMulti = new PantallaFraudeMulti();
                    Render.app.setScreen(pantallaFraudeMulti);
                    break;
                case 3:
                     PantallaCodiciaMulti pantallaCodiciaMulti = new PantallaCodiciaMulti();
                     Render.app.setScreen(pantallaCodiciaMulti);
                     break;
                case 4:
                     PantallaLujuriaMulti pantallaLujuriaMulti = new PantallaLujuriaMulti();
                     Render.app.setScreen(pantallaLujuriaMulti);
                     break;
                case 5:
                     PantallaTraicionMulti pantallaTraicionMulti = new PantallaTraicionMulti();
                     Render.app.setScreen(pantallaTraicionMulti);
                     break;
                default:
                    // Fallback
                    PantallaLimboMulti pantallaDefault = new PantallaLimboMulti(); 
                    Render.app.setScreen(pantallaDefault);
                    break;
            }
            return; // Salir, no cargar lógica single player
        }

        // LÓGICA UN JUGADOR
        switch (pisoActual) {
            case 1:
                // PISO 1: LIMBO
                if (pantallaLimbo == null) {
                    pantallaLimbo = new PantallaLimbo();
                }   pantallaLimbo.reiniciarNivel(pisoActual, nivelActual);
                Render.app.setScreen(pantallaLimbo);
                break;
            case 2:
                // PISO 2: FRAUDE
                if (pantallaFraude == null) {
                    pantallaFraude = new PantallaFraude();
                }   pantallaFraude.reiniciarNivel(pisoActual, nivelActual);
                Render.app.setScreen(pantallaFraude);
                break;
            case 3:
                // PISO 3: Codicia
                if (pantallaCodicia == null) {
                    pantallaCodicia = new PantallaCodicia();
                }   pantallaCodicia.reiniciarNivel(pisoActual, nivelActual);
                Render.app.setScreen(pantallaCodicia);
                break;
            case 4:
                // PISO 4 LUJURIA
                if (pantallaLujuria == null) {
                    pantallaLujuria = new PantallaLujuria();
                }   pantallaLujuria.reiniciarNivel(pisoActual, nivelActual);
                Render.app.setScreen(pantallaLujuria);
                break;
            case 5:
                // PISO 5: TRAICION
                if (pantallaTraicion == null) {
                    pantallaTraicion = new PantallaTraicion();
                }   pantallaTraicion.reiniciarNivel(pisoActual, nivelActual);
                Render.app.setScreen(pantallaTraicion);
                break;
            default:
                break;
        }

    }

    // Iniciar una nueva partida
    public void iniciarJuego() {
        pisoActual = 1;
        nivelActual = 1;

        // Limpiar cache de pantallas para empezar limpio
        pantallaLimbo = null;
        pantallaFraude = null;  // ← AGREGAR
        pantallaTienda = null;

        cargarNivel();
    }

    // Avanzar al siguiente nivel
    public void avanzarNivel() {
        this.nivelActual++;
        Config.personajeSeleccionado.setMonedasActuales(MONEDAS_GANADAS_POR_NIVEL);

        if (pisoActual == PISO_MAX) {
            // Al derrotar al boss final, ir a pantalla de victoria
            Render.app.setScreen(new PantallaVictoria());  // Pantalla final del juego
            return;  // Salir sin avanzar más
        }

        // Verificar si completamos todos los niveles del piso
        if (nivelActual > this.NIVEL_MAX) {
            // COMPLETASTE el piso actual
            Render.app.setScreen(new PantallaSiguientePiso());  // ← Muestra la pantalla

            this.pisoActual++;
            this.nivelActual = 1;

            // Limpiar pantalla anterior
            if (pisoActual == 2) {
                pantallaLimbo = null;
            } else if (pisoActual == 3) {
                pantallaFraude = null;
            } else if (pisoActual == 4) {
                pantallaCodicia = null;
            } else if (pisoActual == 5) {
                pantallaLujuria = null;
            } else if (pisoActual == 6) {
                pantallaTraicion = null;
            }

            // NO llamar cargarNivel() aquí, lo hace continuarDespuesDePiso()

        } else if (nivelActual == NIVEL_TIENDA) {
            irATienda();
        } else {
            cargarNivel();
        }
    }

    // Continuar después de completar un piso
    public void continuarDespuesDePiso() {
        cargarNivel();  // Carga el nivel 1 del nuevo piso
    }

    // fucion para ir a tienda
    public void irATienda() {

        if (pantallaTienda == null) {
            pantallaTienda = new PantallaTienda();
        }

        Render.app.setScreen(pantallaTienda);
    }

    // Manejar game over
    public void gameOver() {
        Render.app.setScreen(new PantallaGameOver());
    }
}
