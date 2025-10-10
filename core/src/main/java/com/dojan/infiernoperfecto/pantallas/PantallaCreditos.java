package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Screen;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;
import static com.dojan.infiernoperfecto.utiles.Render.batch;


public class PantallaCreditos implements Screen {
    private Imagen creditos;
    private boolean fadeInTerminado = false,termina = false;
    private float contTiempo = 0, tiempoEspera = 5;
    private float contTiempoTermina = 0, tiempoTermina = 5;
    private float alpha = 0;

    @Override
    public void show() {
        creditos = new Imagen(Recursos.FONDOCREDITOS);
        creditos.setTransparencia(alpha);
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(0,0,0);
        batch.begin();
            creditos.dibujar();
        batch.end();

        procesarFade();
    }

    private void procesarFade() {

        if (!fadeInTerminado) {
            alpha += 0.01f;
            if (alpha > 1) {
                alpha = 1;
                fadeInTerminado = true;
            }
        } else {
            contTiempo += 0.05f;
            if (contTiempo > tiempoEspera) {
                alpha -= 0.01f;
                if (alpha < 0) {
                    alpha = 0;
                    termina=true;
                }
            }
        }
        creditos.setTransparencia(alpha);

        if (termina) {
            contTiempoTermina += 0.1f;
            if (contTiempoTermina > tiempoTermina) {
                Render.app.setScreen(new PantallaMenu());
            }
        }
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
        if (creditos != null){
            try{ creditos.dispose(); }catch(Exception e){}
            creditos = null;
        }
    }
}
