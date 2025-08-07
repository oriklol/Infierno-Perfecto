package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Screen;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;

public class PantallaMapa implements Screen {
    private Imagen mapa;
    private float tiempo;

    @Override
    public void show() {
        mapa = new Imagen(Recursos.MAPA);

    }

    @Override
    public void render(float delta) {
        tiempo+=delta;
        Render.batch.begin();
            mapa.dibujar();

        Render.batch.end();
        if (tiempo>5){
            Render.app.setScreen(new PantallaLimbo());
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

    }
}
