package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;


public class PantallaHistoria implements Screen {
    private Imagen[] vinetas = new Imagen[3];
    private Texto parrafos[] = new Texto[3];
    private String textos[] = {
        "El les ordeno bajar hacia el abismo nunca antes visitado",
        "Siguieron sus ordenes y el abismo visitaron",
        "Nadie sabe que se encontraran... ni dios mismo"
    };

    private float tiempo = 0;
    private int indice = 0;




    @Override
    public void show() {
        vinetas[0] = new Imagen(Recursos.FONDOHISTORIA1);
        vinetas[1] = new Imagen(Recursos.FONDOHISTORIA2);
        vinetas[2] = new Imagen(Recursos.FONDOHISTORIA3);

        for (int i = 0; i < vinetas.length; i++) {
            vinetas[i].setSize(Config.ANCHO, Config.ALTO);
        }

        for(int i= 0;i<parrafos.length;i++){
            parrafos[i] = new Texto(Recursos.FUENTEMENU,65, Color.BLUE,false);
            parrafos[i].setTexto(textos[i]);
        }

    }


    @Override
    public void render(float delta) {
        tiempo += delta;

        if(tiempo>=1f && indice<vinetas.length){
            indice++;
            tiempo=0;
        }else if(indice>vinetas.length-1){
            Render.app.setScreen(new PantallaClases());
        }

        Render.batch.begin();
        if(indice <vinetas.length){
            vinetas[indice].dibujar();
            parrafos[indice].setPosition((int)((Config.ANCHO - parrafos[indice].getAncho())/2),100);
            parrafos[indice].dibujar();
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

    }
}
