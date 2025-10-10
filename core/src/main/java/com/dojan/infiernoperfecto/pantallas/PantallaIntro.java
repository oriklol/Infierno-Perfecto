package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Screen;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.utiles.Recursos;
import static com.dojan.infiernoperfecto.utiles.Render.batch;

public class PantallaIntro implements Screen {
    private Imagen intro;

    @Override
    public void show() {
        intro = new Imagen(Recursos.FONDOINTRO);
    }

    @Override
    public void render(float delta) {
        batch.begin();
            intro.dibujar();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        if (intro != null){
            try{ intro.dispose(); }catch(Exception e){}
            intro = null;
        }
    }
}
