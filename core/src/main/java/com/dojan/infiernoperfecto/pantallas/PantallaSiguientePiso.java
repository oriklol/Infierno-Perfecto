package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.ControladorJuego;  // ← AGREGAR
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;

import io.Entradas;

public class PantallaSiguientePiso implements Screen {
    private Imagen ganaste;


    private float tiempo = 0f;  // ← Inicializar en 0
    private final float DURACION_PANTALLA = 2f;  // ← 2 segundos
    private boolean mouseClick = false;

    Entradas entradas = new Entradas();

    @Override
    public void show() {
        ganaste = new Imagen(Recursos.SIGUIENTEPISO);
        Gdx.input.setInputProcessor(entradas);
        tiempo = 0f;  // ← Resetear el tiempo al mostrar
    }

    @Override
    public void render(float delta) {
        // ← Acumular tiempo
        tiempo += delta;

        Render.batch.begin();
        ganaste.dibujar();

        Render.batch.end();

        // ← Después de 2 segundos, continuar automáticamente
        if (tiempo >= DURACION_PANTALLA) {
            ControladorJuego.getInstance().continuarDespuesDePiso();
        }
    }

    @Override
    public void resize(int i, int i1) {

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
        if (ganaste != null){
            try{ ganaste.dispose(); }catch(Exception e){}
            ganaste = null;
        }

    }
}
