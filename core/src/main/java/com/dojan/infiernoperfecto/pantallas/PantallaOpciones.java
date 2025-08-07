package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.ControlAudio;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;
import io.Entradas;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.dojan.infiernoperfecto.utiles.Render.app;


public class PantallaOpciones implements Screen {
    private Imagen fondo;
    private Texto opciones[] = new Texto[4];
    private Texto titulo;
    //hay que hacer que cambio se alterne entre true y false , u off y on
    public String getTextoOpcion(int opc) {
        switch (opc) {
            case 0: return "Volumen Musica: " + (ControlAudio.volumenMusica * 10) + "%";
            case 1: return "Volumen Efectos: " + (ControlAudio.volumenSFX * 10) + "%";
            case 2: return "Pantalla Completa: " + Gdx.graphics.isFullscreen();
            case 3: return "Volver";
            default: return "";
        }
    }
    GlyphLayout layout;
    private int opc = 1;
    private boolean mouseClick = false;
    private float tiempo;
    private float tiempoCooldown;
    private float cooldown = 0;
    private boolean puedeCambiar = false;


    // FIT VIEWPORT DEBE SER CAMBIADA POR SCREENVIEWPORT, no lo pude resolver en algun momento lo hare
    private FitViewport fitViewport;
    //OrthographicCamera camara;


    Entradas entradas = new Entradas();
    Config config = new Config();

//    private ShapeRenderer shape;
//    private Slider sliderVolumen;

    @Override
    public void show() {
        fitViewport = new FitViewport(800, 600);
        fondo = new Imagen(Recursos.FONDOOPCIONES);
        fondo.setSize(Config.ANCHO, Config.ALTO);

        layout = new GlyphLayout();

        Gdx.input.setInputProcessor(entradas);

//        shape = new ShapeRenderer();
//        sliderVolumen = new Slider(200, 300, 200, 20);

        int avanceY =120;
        int avanceTitulo = 20;
        int avanceX = -150;

        titulo = new Texto(Recursos.FUENTEMENU, 80, Color.WHITE, true);
        titulo.setTexto("OPCIONES");
        titulo.setPosition((int) ((Config.ANCHO - layout.width) / 3.1f), (int) (((Config.ALTO + layout.height) / 1.1f) + avanceTitulo));

        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = new Texto(Recursos.FUENTEMENU, 80, Color.WHITE, true);
            //opciones[i].setTexto(getTextoOpcion(i));
            opciones[i].setPosition((int) ((Config.ANCHO / 2) - (opciones[i].getAncho() / 2)) + avanceX, (int) (((Config.ALTO / 1.4f) + (opciones[0].getAlto() / 2)) - (opciones[i].getAlto() + avanceY) * i));
        }
    }

    @Override
    public void render(float delta) {
        int avanceX = 0;
        fitViewport.apply();
        Render.batch.begin();
        fondo.dibujar();
        titulo.dibujar();
        for (int i = 0; i < opciones.length; i++) {
            opciones[i].setTexto(getTextoOpcion(i));
            int nuevoX = (int) ((Config.ANCHO / 2) - (opciones[i].getAncho() / 2)) + avanceX;
            opciones[i].setPosition(nuevoX, opciones[i].getY());
            opciones[i].dibujar();
        }
        Render.batch.end();

//        shape.begin(ShapeRenderer.ShapeType.Filled);
//        sliderVolumen.dibujar(shape);
//        shape.end();
        tiempo += delta;

        if (entradas.isAbajo()) {
            if (tiempo > 0.15f) {
                tiempo = 0;
                opc++;
                if (opc > 4) {
                    opc = 1;
                }
            }
        }

        if (entradas.isArriba()) {
            if (tiempo > 0.1f) {
                tiempo = 0;
                opc--;
                if (opc < 1) {
                    opc = 4;
                }
            }
        }


        // CUESTION MOUSE
        int cont = 0;
        for (int i = 0; i < opciones.length; i++) {
            if ((entradas.getMouseX() >= opciones[i].getX()) && (entradas.getMouseX() <= (opciones[i].getX() + opciones[i].getAncho()))) {
                if ((((entradas.getMouseY() >= opciones[i].getY() - opciones[i].getAlto()) && (entradas.getMouseY() <= (opciones[i].getY()))))) {
                    opc = i + 1;
                    cont++;
                }

            }
        }
        if (cont > 0) {
            mouseClick = true;
        } else {
            mouseClick = false;
        }

        for (int i = 0; i < opciones.length; i++) {
            if (i == (opc - 1)) {
                opciones[i].setColor(Color.GOLDENROD);
            } else {
                opciones[i].setColor(Color.WHITE);
            }
        }

        tiempoCooldown += delta;

        if (entradas.isEnter() || entradas.isClick()) {
            if (((opc == 1) && entradas.isEnterPresionado())) {
                ControlAudio.cicloVolumenMusica();
            } else if ((opc == 2) && entradas.isEnterPresionado()) {
                ControlAudio.cicloVolumenSFX();
            } else if ((opc == 3) && entradas.isEnterPresionado()) {
                if (tiempoCooldown > 0.4) {
                    if (!Gdx.graphics.isFullscreen()) {
                        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                    } else {
                        Gdx.graphics.setWindowedMode(Config.ANCHO, Config.ALTO);
                    }
                    tiempoCooldown = 0;
                }
            } else if (((opc == 4) && (entradas.isEnter())) || ((opc == 4) && (entradas.isClick()) && (mouseClick))) {
                app.setScreen(new PantallaMenu());
            }

        }
    }



    @Override
    public void resize(int width, int height) {

        fitViewport.update(width, height);

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
