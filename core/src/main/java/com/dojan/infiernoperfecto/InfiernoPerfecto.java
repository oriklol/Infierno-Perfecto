package com.dojan.infiernoperfecto;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dojan.infiernoperfecto.pantallas.PantallaCreditos;
import com.dojan.infiernoperfecto.pantallas.PantallaMenu;
import com.dojan.infiernoperfecto.pantallas.PantallaVictoria;
import com.dojan.infiernoperfecto.pantallas.enciclopedia.PantallaEnciclopedia;
import com.dojan.infiernoperfecto.red.HiloCliente;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.Render;

/** {@link ApplicationListener} implementation shared by all platforms. */
public class InfiernoPerfecto extends Game {
    private HiloCliente cliente = null;

    // relacion de aspecto
    public static Viewport viewport;
    public static OrthographicCamera camera;

    @Override
    public void create() {
        // crear camara ortográfica
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Config.ANCHO, Config.ALTO);

        // crear viewport con la camara que mantiene la relacion de aspecto
        viewport = new FitViewport(Config.ANCHO, Config.ALTO, camera);

        Render.app = this;
        Render.batch = new SpriteBatch();
        this.setScreen(new PantallaMenu());
    }

    @Override
    public void render() {
        if (cliente != null && cliente.isServidorCaido()) {
            System.out.println("Servidor caído detectado globalmente");
            cliente.desconectar();
            System.out.println("Cerrando Socket Cliente");
            cliente = null;
            Config.empiezaPartida = false;
            setScreen(new PantallaMenu());
        }

        if (cliente != null && cliente.isClienteExternoDesconectado()) {
            System.out.println("Cliente externo desconectado detectado globalmente");
            cliente.desconectar();
            System.out.println("Cerrando Socket Cliente");
            cliente = null;
            Config.empiezaPartida = false;
            setScreen(new PantallaMenu());
        }

        // aplicacion de viewport y actualizacion de camara
        viewport.apply();
        camera.update();
        Render.batch.setProjectionMatrix(camera.combined);

        super.render();
    }

    @Override
    public void resize(int width, int height) {
        // actualizar el viewport al nuevo tamaño de la ventana
        viewport.update(width, height, true);
        super.resize(width, height);
    }

    @Override
    public void dispose() {
        if (cliente != null) {
            try {
                cliente.desconectar();
                System.out.println("Juego: HiloCliente desconectado");
            } catch (Exception e) {
                System.err.println("Juego: Error al desconectar HiloCliente - " + e.getMessage());
            }
            cliente = null;
        }

        try{
            if (Render.batch != null) {
                Render.batch.dispose();
                Render.batch = null;
            }
            if (Render.renderer != null) {
                Render.renderer.dispose();
                Render.renderer = null;
            }
        }catch(Exception e){
            // ignore
        }

        if (getScreen() != null){
            try{ getScreen().dispose(); }catch(Exception e){ }
        }

        com.dojan.infiernoperfecto.utiles.ControlAudio.dispose();
    }

    public HiloCliente getCliente() {
        return cliente;
    }

    public void setCliente(HiloCliente hiloCliente) {
        this.cliente = hiloCliente;
    }
}
