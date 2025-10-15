package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.entidades.Jugador;
import com.dojan.infiernoperfecto.entidades.clases.Escudador;
import com.dojan.infiernoperfecto.entidades.clases.Magico;
import com.dojan.infiernoperfecto.entidades.clases.Peleador;
import com.dojan.infiernoperfecto.entidades.clases.Soporte;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;

import io.Entradas;

public class PantallaClases implements Screen {
    private Imagen seleccionFondo;
    private Texto clases[] = new Texto[4];
    String textos[] = {
        "Caballero",
        "Mago",
        "Tanque",
        "Soporte"
    };
    private float tiempo;
    private int opc=1;
    Entradas entradas = new Entradas();
    private ShapeRenderer shapeRenderer;

    private final int MAX_CLASES = 4, MIN_CLASES = 1;
    private boolean mouseClick = false;

    @Override
    public void show() {
        seleccionFondo = new Imagen(Recursos.FONDOSELECCION);
        Gdx.input.setInputProcessor(entradas);
        shapeRenderer = new ShapeRenderer();
        for(int i = 0; i<clases.length;i++){
            clases[i] = new Texto(Recursos.FUENTEMENU,70, Color.WHITE,true);
            clases[i].setTexto(textos[i]);
        }

    }

    @Override
    public void render(float delta) {
        Render.batch.begin();
            seleccionFondo.dibujar();
            for(int i = 0;i<clases.length;i++){
                //float espacioEntreTextos = (Config.ANCHO / (clases.length + 1f));
                float espacioEntreTextos = clases[0].getAncho()+(i*11);
                float centroX = espacioEntreTextos * (i +0.9f);

                float posX = centroX - clases[i].getAncho() / 1.5f;
                float posY = Config.ALTO / 8f;

                //clases[i].setPosition((int)((clases[i].getAncho()/4)+((i)*clases[0].getAncho())),((int) ((Config.ALTO)/8f)));
                clases[i].setPosition((int) posX, (int) posY);
                clases[i].dibujar();
            }
        Render.batch.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // o .Filled si querés un fondo lleno
        shapeRenderer.setColor(Color.GOLDENROD);


        int x = clases[opc-1].getX();
        int y = (int) ((clases[opc-1].getY())/2f);
        int ancho = (int)clases[opc-1].getAncho();
        int alto = (int)clases[opc-1].getAlto();


        int margen =25;

        shapeRenderer.rect(x - margen, y - margen, ancho + margen * 2, (alto + margen) * 7);
        shapeRenderer.end();

        tiempo+= delta;

        if(entradas.isDerecha()){
            if(tiempo>0.15f){
                tiempo=0;
                opc++;
                if(opc>MAX_CLASES){
                    opc=MIN_CLASES;
                }
            }
        }

        if(entradas.isIzquierda()){
            if(tiempo>0.1f){
                tiempo=0;
                opc--;
                if(opc<MIN_CLASES){
                    opc=MAX_CLASES;
                }
            }
        }

        int mouseX = entradas.getMouseX();
        int mouseY = Gdx.graphics.getHeight() - entradas.getMouseY(); // invertir eje Y

        int cont = 0;
        for (int i = 0; i < clases.length; i++) {
            if ((mouseX >= clases[i].getX()) &&
                (mouseX <= clases[i].getX() + clases[i].getAncho())) {

                int yBase = (int) (clases[i].getY() / 2f); // mismo cálculo que el rectángulo
                int altoEquivalente = (int) ((clases[i].getAlto() + 25) * 7);

                if ((mouseY >= yBase) && (mouseY <= yBase + altoEquivalente)) {
                    opc = i + 1;
                    cont++;
                }
            }
        }

        mouseClick = cont > 0;

        if(cont >0){
                mouseClick = true;
            }else{
                mouseClick = false;
            }

        for(int i=0; i<clases.length;i++){
            if(i==(opc-1)){
                clases[i].setColor(Color.GOLDENROD);
            }else{
                clases[i].setColor(Color.WHITE);
            }
        }

        if(entradas.isEnter() || entradas.isClick()) {
            if (((opc == 1) && (entradas.isEnter())) || ((opc == 1) && (entradas.isClick()) && (mouseClick))) {
                Config.personajeSeleccionado = new Jugador("personaje1", new Peleador());
            } else if (((opc == 2) && (entradas.isEnter())) || ((opc == 2) && (entradas.isClick()) && (mouseClick))) {
                Config.personajeSeleccionado = new Jugador("personaje1", new Magico());
            } else if (((opc == 3) && (entradas.isEnter())) || ((opc == 3) && (entradas.isClick()) && (mouseClick))) {
                Config.personajeSeleccionado = new Jugador("personaje1", new Escudador());
            } else if (((opc == 4) && (entradas.isEnter())) || ((opc == 4) && (entradas.isClick()) && (mouseClick))){
                Config.personajeSeleccionado = new Jugador("personaje1", new Soporte());
            }
            Render.app.setScreen(new PantallaMapa());
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
        if (seleccionFondo != null){
            try{ seleccionFondo.dispose(); }catch(Exception e){}
            seleccionFondo = null;
        }

        if (clases != null){
            for (int i=0;i<clases.length;i++){
                if (clases[i] != null){
                    try{ clases[i].dispose(); }catch(Exception e){}
                    clases[i]=null;
                }
            }
        }

        if (shapeRenderer != null){
            try{ shapeRenderer.dispose(); }catch(Exception e){}
            shapeRenderer = null;
        }
    }
}
