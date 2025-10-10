package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;

import io.Entradas;

public class PantallaSiguienteNivel implements Screen {
    private Imagen ganaste;

    private Texto opciones[] = new Texto[2];
    String textos[] = {
        "VENCISTE A TODOS LOS ENEMIGOS!!",
        "Continuando..."
    };
    private int opc = 2;
    private float tiempo;
    private boolean mouseClick = false;

    Entradas entradas = new Entradas();

    @Override
    public void show() {
        ganaste = new Imagen(Recursos.FONDOGANAR);
        Gdx.input.setInputProcessor(entradas);

        for(int i = 0; i < opciones.length; i++){
            opciones[i] = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, true);
            opciones[i].setTexto(textos[i]);
            float anchoTexto = opciones[i].getAncho();
            int centroX = Config.ANCHO / 2;
            int x = (int)(centroX - anchoTexto / 2);
            int y = (int)(Config.ALTO * 0.8f) - (i * 120);
            opciones[i].setPosition(x, y);
        }

    }

    @Override
    public void render(float v) {

        Render.batch.begin();
        ganaste.dibujar();
        for(int i = 0; i<opciones.length;i++){
            opciones[i].dibujar();
        }
        Render.batch.end();

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

        if (opciones != null){
            for(int i=0;i<opciones.length;i++){
                if (opciones[i] != null){
                    try{ opciones[i].dispose(); }catch(Exception e){}
                    opciones[i]=null;
                }
            }
        }
    }
}
