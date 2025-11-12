package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;

public class PantallaTutorial implements Screen {
    private final Imagen[] imagenesTutorial = new Imagen[3];
    final private Texto TEXTOS_TUTORIAL[] = new Texto[3];
    String textos[] = {
        "ELIGE EL ENEMIGO QUE DESEES ATACAR.",
        "ELIGE EL ATAQUE QUE DESEES USAR.",
        "COMPRA OBJETOS PARA CURAR TU VIDA."
    };

    private float tiempo = 0f;
    private int indice = 0;


    @Override
    public void show() {
        imagenesTutorial[0] = new Imagen(Recursos.IMAGENTUTORIAL1);
        imagenesTutorial[1] = new Imagen(Recursos.IMAGENTUTORIAL2);
        imagenesTutorial[2] = new Imagen(Recursos.IMAGENTUTORIAL3);

        for (Imagen imagenesTutorial : imagenesTutorial) {
            if (imagenesTutorial != null) imagenesTutorial.setSize(Config.ANCHO, Config.ALTO);
        }

        for (int i = 0; i < TEXTOS_TUTORIAL.length; i++) {
            TEXTOS_TUTORIAL[i] = new Texto(Recursos.FUENTEMENU, 53  , Color.BLACK, false);
            TEXTOS_TUTORIAL[i].setTexto(textos[i]);
        }


    }

    @Override
    public void render(float delta) {
        tiempo += delta;

        if (tiempo >= 2f && indice < imagenesTutorial.length) {
            indice++;
            tiempo = 0;
        } else if (indice > imagenesTutorial.length - 1) {
            Render.app.setScreen(new PantallaMenu());
            return;
        }

        Render.batch.begin();
        if (indice < imagenesTutorial.length && imagenesTutorial[indice] != null) {
            imagenesTutorial[indice].dibujar();
            TEXTOS_TUTORIAL[indice].setPosition((int) ((Config.ANCHO - TEXTOS_TUTORIAL[indice].getAncho()) / 2), Config.ALTO/3);
            TEXTOS_TUTORIAL[indice].dibujar();
        }
        Render.batch.end();
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

    }
}
