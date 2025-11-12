package com.dojan.infiernoperfecto;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dojan.infiernoperfecto.pantallas.PantallaClases;
import com.dojan.infiernoperfecto.pantallas.PantallaCreditos;
import com.dojan.infiernoperfecto.pantallas.PantallaMenu;
import com.dojan.infiernoperfecto.pantallas.PantallaTienda;
import com.dojan.infiernoperfecto.utiles.Render;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class InfiernoPerfecto extends Game {


    @Override
    public void create() {
        Render.app = this;
        Render.batch = new SpriteBatch();
        this.setScreen(new PantallaMenu()); // PantallaMenu debe ser reemplazado por PantallaCreditos para inprivate Texto lugar; iciar el juego
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
        // dispose global rendering resources
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

        // dispose current screen (if set)
        if (getScreen() != null){
            try{ getScreen().dispose(); }catch(Exception e){ }
        }

        // dispose audio control
        com.dojan.infiernoperfecto.utiles.ControlAudio.dispose();
    }
}
