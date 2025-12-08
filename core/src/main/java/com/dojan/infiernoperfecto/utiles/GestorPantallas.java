package com.dojan.infiernoperfecto.utiles;

import com.badlogic.gdx.Screen;
import java.util.Stack;

public class GestorPantallas {
    // Singleton para gestionar las pantallas y su historial
    private static GestorPantallas instancia;
    // Pila para almacenar el historial de pantallas
    private Stack<Screen> historialPantallas;

    // Constructor privado para el singleton
    private GestorPantallas() {
        historialPantallas = new Stack<>();
    }

    // Funcion para obtener la instancia del singleton
    public static GestorPantallas getInstance() {
        if (instancia == null) {
            instancia = new GestorPantallas();
        }
        return instancia;
    }

    // Cambia a una nueva pantalla y guarda la actual en el historial
    public void irAPantalla(Screen nuevaPantalla) {
        Screen pantallaActual = Render.app.getScreen();

        // Solo guardar si hay una pantalla actual y no es null
        if (pantallaActual != null) {
            historialPantallas.push(pantallaActual);
            pantallaActual.hide(); // Pausar pero NO eliminar
        }

        Render.app.setScreen(nuevaPantalla);
    }


    // Vuelve a la pantalla anterior
    public void volverAtras() {
        if (!historialPantallas.isEmpty()) {
            // Dispose de la pantalla actual (opciones/enciclopedia)
            Screen pantallaActual = Render.app.getScreen();
            if (pantallaActual != null) {
                pantallaActual.dispose();
            }

            // Recuperar la pantalla anterior
            Screen pantallaAnterior = historialPantallas.pop();

            // Volver a activarla
            Render.app.setScreen(pantallaAnterior);
            //pantallaAnterior.show(); // Reactivar
        }
    }

    // Limpia el historial de pantallas
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

    // Verifica si hay historial disponible
    public boolean hayHistorial() {
        return !historialPantallas.isEmpty();
    }

    // Obtiene el tamaño del historial
    public int tamañoHistorial() {
        return historialPantallas.size();
    }
}
