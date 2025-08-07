package com.dojan.infiernoperfecto;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dojan.infiernoperfecto.pantallas.PantallaCreditos;
import com.dojan.infiernoperfecto.pantallas.PantallaLimbo;
import com.dojan.infiernoperfecto.pantallas.PantallaMenu;
import com.dojan.infiernoperfecto.utiles.Render;
import static com.dojan.infiernoperfecto.utiles.Render.batch;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class InfiernoPerfecto extends Game {


    @Override
    public void create() {
        Render.app = this;
        Render.batch = new SpriteBatch();
        this.setScreen(new PantallaCreditos()); // PantallaMenu debe ser reemplazado por PantallaCreditos para iniciar el juego
    }

    @Override
    public void render() {
        super.render();

//        Gdx.gl.glClearColor(136f / 255f, 0f, 21f / 255f, 1f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    private void update(){

    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
