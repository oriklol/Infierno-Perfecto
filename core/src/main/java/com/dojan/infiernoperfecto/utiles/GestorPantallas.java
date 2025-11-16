package com.dojan.infiernoperfecto.utiles;

import com.badlogic.gdx.Screen;
import java.util.Stack;

public class GestorPantallas {
    private static GestorPantallas instancia;
    private Stack<Screen> historialPantallas;

    private GestorPantallas() {
        historialPantallas = new Stack<>();
    }

    public static GestorPantallas getInstance() {
        if (instancia == null) {
            instancia = new GestorPantallas();
        }
        return instancia;
    }

    /**
     * Cambia a una nueva pantalla y guarda la actual en el historial
     */
    public void irAPantalla(Screen nuevaPantalla) {
        Screen pantallaActual = Render.app.getScreen();

        // Solo guardar si hay una pantalla actual y no es null
        if (pantallaActual != null) {
            historialPantallas.push(pantallaActual);
            // NO hacer hide() ni dispose() porque volveremos a ella
        }

        Render.app.setScreen(nuevaPantalla);
    }

    /**
     * Vuelve a la pantalla anterior
     */
    public void volverAtras() {
        if (!historialPantallas.isEmpty()) {
            Screen pantallaAnterior = historialPantallas.pop();

            // Dispose de la pantalla actual (la enciclopedia)
            Screen pantallaActual = Render.app.getScreen();
            if (pantallaActual != null) {
                pantallaActual.dispose();
            }

            Render.app.setScreen(pantallaAnterior);
        }
    }

    /**
     * Limpia el historial (usar al volver al menú principal)
     */
    public void limpiarHistorial() {
        // Dispose de todas las pantallas guardadas
        while (!historialPantallas.isEmpty()) {
            Screen pantalla = historialPantallas.pop();
            try {
                pantalla.dispose();
            } catch (Exception e) {
                // Ignorar errores de dispose
            }
        }
    }

    /**
     * Verifica si hay pantallas en el historial
     */
    public boolean hayHistorial() {
        return !historialPantallas.isEmpty();
    }

    /**
     * Obtiene el tamaño del historial
     */
    public int tamañoHistorial() {
        return historialPantallas.size();
    }
}
