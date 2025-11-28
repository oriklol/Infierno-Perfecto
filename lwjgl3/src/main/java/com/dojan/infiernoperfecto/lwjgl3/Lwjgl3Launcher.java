package com.dojan.infiernoperfecto.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.dojan.infiernoperfecto.InfiernoPerfecto;
import com.dojan.infiernoperfecto.utiles.Config;

/** Launches the desktop (LWJGL3) application. */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return;
        createApplication();
    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new InfiernoPerfecto(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        configuration.setTitle("InfiernoPerfecto - Cliente");

        configuration.useVsync(true);
        configuration.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate + 1);
        configuration.setResizable(true); // permitir redimensionar ventana

        // iniciar en ventana redimensionable
        configuration.setWindowedMode(Config.ANCHO, Config.ALTO);

        // iniciar en fullscreen
        //configuration.setFullscreenMode(Lwjgl3ApplicationConfiguration.getDisplayMode());

        // ✅ Atajo Alt+Enter para cambiar a fullscreen en tiempo de ejecución
        configuration.setWindowIcon("libgdx128.png", "libgdx64.png", "libgdx32.png", "libgdx16.png");

        return configuration;
    }
}
