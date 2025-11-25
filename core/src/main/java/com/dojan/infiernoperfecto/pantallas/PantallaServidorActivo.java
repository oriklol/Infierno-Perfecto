package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.serverred.HiloServidor;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.Recursos;
import static com.dojan.infiernoperfecto.utiles.Render.app;
import static com.dojan.infiernoperfecto.utiles.Render.batch;

import io.Entradas;

public class PantallaServidorActivo implements Screen {
    private Imagen fondo;
    private Texto titulo;
    private Texto estado;
    private Texto info;
    private Texto instrucciones;
    private FitViewport fitViewport;
    private final Entradas entradas = new Entradas();

    @Override
    public void show() {
        fitViewport = new FitViewport(800, 600);

        fondo = new Imagen(Recursos.FONDOOPCIONES);
        fondo.setSize(Config.ANCHO, Config.ALTO);

        Gdx.input.setInputProcessor(entradas);

        // Título
        titulo = new Texto(Recursos.FUENTEMENU, 80, Color.GOLDENROD, true);
        titulo.setTexto("SERVIDOR ACTIVO");
        titulo.setPosition(
            (int) ((Config.ANCHO / 2) - (titulo.getAncho() / 2)),
            Config.ALTO - 80
        );

        // Estado del servidor
        estado = new Texto(Recursos.FUENTEMENU, 50, Color.GREEN, true);
        estado.setTexto("✓ Servidor escuchando en puerto 6666");
        estado.setPosition(
            (int) ((Config.ANCHO / 2) - (estado.getAncho() / 2)),
            Config.ALTO - 200
        );

        // Información adicional
        info = new Texto(Recursos.FUENTEMENU, 35, Color.WHITE, true);
        info.setTexto("Esperando conexiones de clientes...");
        info.setPosition(
            (int) ((Config.ANCHO / 2) - (info.getAncho() / 2)),
            Config.ALTO - 300
        );

        // Instrucciones
        instrucciones = new Texto(Recursos.FUENTEMENU, 30, Color.LIGHT_GRAY, true);
        instrucciones.setTexto("Presiona ESC para volver al menú");
        instrucciones.setPosition(
            (int) ((Config.ANCHO / 2) - (instrucciones.getAncho() / 2)),
            100
        );
    }

    @Override
    public void render(float delta) {
        fitViewport.apply();

        batch.begin();
        fondo.dibujar();
        titulo.dibujar();
        estado.dibujar();
        info.dibujar();
        instrucciones.dibujar();
        batch.end();

        // Volver al menú con ESC
        if (entradas.isEsc()) {
            // Aquí puedes detener el servidor si lo deseas
            HiloServidor servidor = app.getServidor();
            servidor.detener();
            app.setScreen(new PantallaMenu());
            System.out.println("Servidor detenido por el usuario");
        }
    }

    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        if (fondo != null) {
            try { fondo.dispose(); } catch (Exception e) {}
            fondo = null;
        }

        if (titulo != null) {
            try { titulo.dispose(); } catch (Exception e) {}
            titulo = null;
        }

        if (estado != null) {
            try { estado.dispose(); } catch (Exception e) {}
            estado = null;
        }

        if (info != null) {
            try { info.dispose(); } catch (Exception e) {}
            info = null;
        }

        if (instrucciones != null) {
            try { instrucciones.dispose(); } catch (Exception e) {}
            instrucciones = null;
        }
    }
}
