package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Musica;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.utiles.*;
import io.Entradas;

import static com.dojan.infiernoperfecto.utiles.Render.app;

public class PantallaVictoria implements Screen {
    private Musica musicaFondo;
    private Imagen gameOver;

    private Texto opciones[] = new Texto[2];
    String textos[] = {
        "Ganaste!!",
        "Volver al menu"
    };
    private int opc = 2;
    private float tiempo;
    private boolean mouseClick = false;

    Entradas entradas = new Entradas();

    @Override
    public void show() {
        gameOver = new Imagen(Recursos.FONDOGANAR);
        musicaFondo = new Musica(Recursos.MUSICAMUERTE);
        ControlAudio.setMusicaActual(musicaFondo);
        Gdx.input.setInputProcessor(entradas);

        for(int i = 0; i < opciones.length; i++){
            opciones[i] = new Texto(Recursos.FUENTEMENU, 90, Color.WHITE, true);
            opciones[i].setTexto(textos[i]);
            float anchoTexto = opciones[i].getAncho();
            int centroX = Config.ANCHO / 3;
            int x = (int)(centroX - anchoTexto / 2);
            int y = (int)(Config.ALTO * 0.8f) - (i * 120);
            opciones[i].setPosition(x, y);
        }

        GestorPantallas.getInstance().limpiarHistorial();

    }


    @Override
    public void render(float delta) {
        ControlAudio.reproducirMusica();
        Render.batch.begin();
        gameOver.dibujar();
        for(int i = 0; i<opciones.length;i++){
            opciones[i].dibujar();
        }
        Render.batch.end();



        tiempo+= delta;

        if(entradas.isAbajo()){
            if(tiempo>0.15f){
                tiempo=0;
                opc++;
                if(opc>2){
                    opc=2;
                }
            }

        }

        if(entradas.isArriba()){
            if(tiempo>0.1f){
                tiempo=0;
                opc--;
                if(opc<2){
                    opc=2;
                }
            }

        }

        for(int i=0; i<opciones.length;i++){
            if(i==(opc-1)){
                opciones[i].setColor(Color.GOLDENROD);
            }else{
                opciones[i].setColor(Color.WHITE);
            }
        }

        if(entradas.isEnter() || entradas.isClick()) {
            if (((opc == 2) && (entradas.isEnter())) || ((opc == 2) && (entradas.isClick()) && (mouseClick))) {
                ControlAudio.pararMusica();
                app.setScreen(new PantallaMenu());
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
        if (musicaFondo != null){
            try{ musicaFondo.dispose(); }catch(Exception e){}
            musicaFondo = null;
        }

        if (gameOver != null){
            try{ gameOver.dispose(); }catch(Exception e){}
            gameOver = null;
        }

        if (opciones != null){
            for (int i=0;i<opciones.length;i++){
                if (opciones[i] != null){
                    try{ opciones[i].dispose(); }catch(Exception e){}
                    opciones[i]=null;
                }
            }
        }
    }
}

