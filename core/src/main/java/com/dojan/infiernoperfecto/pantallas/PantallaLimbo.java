package com.dojan.infiernoperfecto.pantallas;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.batalla.Batalla;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.entidades.Enemigo;
import com.dojan.infiernoperfecto.entidades.Jugador;
import com.dojan.infiernoperfecto.entidades.clases.Peleador;
import com.dojan.infiernoperfecto.entidades.enemigos.EnemigoLimbo1;
import com.dojan.infiernoperfecto.entidades.enemigos.EnemigoLimbo2;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.Random;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;

import io.Entradas;


public class PantallaLimbo implements Screen {
    private Imagen fondo;
    private Imagen arena;
    private Imagen danioSpr;
    private boolean mostrarDanio = false;
    private float tiempoDanio = 0f;
    private final float DURACION_DANIO = 1.5f;

    private Texto lugar; // muestra el nivel y el piso
    Entradas entradas = new Entradas();
    private Batalla batalla;
    private float tiempo;
    private int opc=0;

    private boolean esperandoInput = false;
    private EstadoBatalla estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
    private final ArrayList<Enemigo> enemigos = new ArrayList<>();
    private final int CANT_ENEMIGOS_MAX = 3;
    private final int cantEnemigos = (Random.generarEntero(CANT_ENEMIGOS_MAX))+1;
    private final Imagen[] enemigoSpr = new Imagen[CANT_ENEMIGOS_MAX];
    private static final String[] SPRITES_ENEMIGOS = {
        Recursos.ESBIRRO,
        Recursos.MINION
    };
    private int enemigoSeleccionado = 0;



    //private List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
    private Texto[] textoAtaques;
    private int ataqueSeleccionado = 0;

    @Override
    public void show() {
        Config.personajeSeleccionado = new Jugador("personaje1", new Peleador());
        Gdx.input.setInputProcessor(entradas);

        danioSpr = new Imagen(Recursos.EFECTODANIO);



        Render.renderer = new ShapeRenderer();
        fondo = new Imagen(Recursos.FONDOLIMBO);
//        if (cantEnemigos ==1){
//            arena = new Imagen(Recursos.FONDOARENA1);
//        }else if(cantEnemigos == 2){
//            arena = new Imagen(Recursos.FONDOARENA2);
//        }else{
//            arena = new Imagen(Recursos.FONDOARENA3);
//        }
        arena = new Imagen(Recursos.FONDOARENA);

        lugar = new Texto(Recursos.FUENTEMENU,60, Color.BLACK, false);
        lugar.setTexto(Integer.toString(Config.nivel)+" - "+ Integer.toString(Config.piso));
        lugar.setPosition((int)(Config.ANCHO/1.2f),(int)(Config.ALTO/1.1f));

        List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
        textoAtaques = new Texto[ataques.size()];

        for (int i = 0; i < ataques.size(); i++) {
            textoAtaques[i] = new Texto(Recursos.FUENTEMENU, 40, Color.WHITE, false);
            textoAtaques[i].setTexto((i + 1) + " - " + ataques.get(i).getNombre());

            // Zonas 1 y 2 (izquierda), 3 y 4 (centro-izquierda)
            int x = (i < 2) ? 50 : 250;
            int y = (i % 2 == 0) ? 120 : 60;  // primer ataque arriba, segundo abajo

            textoAtaques[i].setPosition(x, y);
        }

        for (int i = 0; i < cantEnemigos; i++) {
            Enemigo enemigo;
            int tipoEnemigo = Random.generarEntero(2);
            if (tipoEnemigo==1) {
                enemigo = new EnemigoLimbo1();
                enemigoSpr[i] = new Imagen(Recursos.ESBIRRO);

            } else{
                enemigo = new EnemigoLimbo2();
                enemigoSpr[i] = new Imagen(Recursos.MINION);
            }
            enemigos.add(enemigo);
        }

        batalla = new Batalla(Config.personajeSeleccionado, enemigos);

    }

    @Override
    public void render(float delta) {
        Render.batch.begin();
            fondo.dibujar();
            arena.dibujar();
            for (int i = 0; i<enemigos.size(); i++){
                enemigoSpr[i].setPosition((int) ((Config.ANCHO/3.5f)*i)+20,Config.ALTO/2);
                enemigoSpr[i].dibujar();
            }
            lugar.dibujar();
        Render.batch.end();

        Render.batch.begin();
        for (int i = 0; i < enemigos.size(); i++) {
            enemigoSpr[i].setPosition((int) ((Config.ANCHO / 3.5f) * i) + 20, Config.ALTO / 2);
            enemigoSpr[i].dibujar();

            Enemigo enemigo = enemigos.get(i);
            Texto vidaEnemigo = new Texto(Recursos.FUENTEMENU, 30, Color.WHITE, false);
            vidaEnemigo.setTexto("HP: " + enemigo.getVidaActual() + " - " + enemigo.getVidaBase());
            vidaEnemigo.setPosition((int) (enemigoSpr[i].getX() + enemigoSpr[i].getAncho() / 2 - vidaEnemigo.getAncho() / 2),
                (int) (enemigoSpr[i].getY() - 20));
            vidaEnemigo.dibujar();
        }
        Render.batch.end();

        tiempo += delta;

        if (null != estadoActual) switch (estadoActual) {
            case SELECCION_ENEMIGO:
                if (batalla.getTurno() == 0) {
                    // Ajustar índices si hay menos enemigos
                    if (opc >= enemigos.size()) opc = 0;
                    if (opc < 0) opc = enemigos.size() - 1;
                    if (enemigoSeleccionado >= enemigos.size()) enemigoSeleccionado = 0;
                    if (enemigoSeleccionado < 0) enemigoSeleccionado = 0;

                    // Detección de mouse sobre los enemigos
                    int mouseX = Gdx.input.getX();
                    int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // invertir Y
                    for (int i = 0; i < enemigos.size(); i++) {
                        Imagen spr = enemigoSpr[i];
                        int x = (int) spr.getX();
                        int y = (int) spr.getY();
                        int ancho = (int) spr.getAncho();
                        int alto = (int) spr.getAlto();
                        if (mouseX >= x && mouseX <= x + ancho && mouseY >= y && mouseY <= y + alto) {
                            opc = i;
                        }
                    }

                    if (tiempo > 0.15f) {
                        if (entradas.isDerecha()) {
                            opc++;
                            if (opc >= enemigos.size()) {
                                opc = 0;
                            }
                            tiempo = 0;
                        }
                        if (entradas.isIzquierda()) {
                            opc--;
                            if (opc < 0) {
                                opc = enemigos.size() - 1;
                            }
                            tiempo = 0;
                        }
                        // Solo avanzar si NO estamos esperando input y el usuario acaba de presionar
                        if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                            enemigoSeleccionado = opc;
                            estadoActual = EstadoBatalla.SELECCION_ATAQUE;
                            tiempo = 0;
                            esperandoInput = true;
                            System.out.println("enemigo seleccionado: " + enemigoSeleccionado);
                            System.out.println("opc: " + opc);
                        }
                        // Resetear esperandoInput cuando el usuario suelta el input
                        if (!(entradas.isEnter() || entradas.isClick())) {
                            esperandoInput = false;
                        }
                    }
                }

                Render.batch.begin();
                Texto textoEnemigoSeleccionado = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, false);
                textoEnemigoSeleccionado.setTexto("Selecciona a un enemigo");
                textoEnemigoSeleccionado.setPosition((Gdx.graphics.getWidth() - (Gdx.graphics.getWidth() / 2) - ((int) textoEnemigoSeleccionado.getAncho() / 2)), 120);
                textoEnemigoSeleccionado.dibujar();
                Render.batch.end();

                break;
            case SELECCION_ATAQUE:
                if (batalla.getTurno() == 0) {
                    // Detección de mouse sobre los ataques
                    int mouseX = Gdx.input.getX();
                    int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // invertir Y
                    for (int i = 0; i < textoAtaques.length; i++) {
                        Texto textoAtaque = textoAtaques[i];
                        int x = textoAtaque.getX();
                        int y = textoAtaque.getY() - 50;
                        int ancho = (int) textoAtaque.getAncho();
                        int alto = (int) textoAtaque.getAlto() + 50;
                        // Si el mouse está sobre el área del texto, selecciona ese ataque
                        if (mouseX >= x && mouseX <= x + ancho && mouseY >= y && mouseY <= y + alto) {
                            ataqueSeleccionado = i;
                        }
                    }

                    // Colorear el ataque seleccionado
                    for (int i = 0; i < textoAtaques.length; i++) {
                        if (i == (ataqueSeleccionado)) {
                            textoAtaques[i].setColor(Color.GOLDENROD);
                        } else {
                            textoAtaques[i].setColor(Color.WHITE);
                        }
                    }

                    for (Texto textoAtaque : textoAtaques) {
                        Render.batch.begin();
                        textoAtaque.dibujar();
                        Render.batch.end();
                    }

                    Ataque ataqueSel = Config.personajeSeleccionado.getClase().getAtaques().get(ataqueSeleccionado);
                    Render.batch.begin();
                    Texto textoPS = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
                    textoPS.setTexto("P.S. " + (int) Config.personajeSeleccionado.getVidaActual());
                    textoPS.setPosition(500, 120);
                    textoPS.dibujar();
                    Texto textoUsos = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
                    textoUsos.setTexto("Usos: " + ataqueSel.getCantUsos() + "   Daño: " + ataqueSel.getDanio());
                    textoUsos.setPosition(500, 60);
                    textoUsos.dibujar();
                    Render.batch.end();

                    if (tiempo > 0.15f) {
                        if (entradas.isDerecha()) {
                            ataqueSeleccionado++;
                            if (ataqueSeleccionado >= textoAtaques.length) {
                                ataqueSeleccionado = 0;
                            }
                            tiempo = 0;
                        }
                        if (entradas.isIzquierda()) {
                            ataqueSeleccionado--;
                            if (ataqueSeleccionado < 0) {
                                ataqueSeleccionado = textoAtaques.length - 1;
                            }
                            tiempo = 0;
                        }
                        // Solo avanzar si NO estamos esperando input y el usuario acaba de presionar
                        if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                            // Selección redundante, pero mantenida por compatibilidad
                            System.out.println("ataque seleccionado: " + ataqueSeleccionado);
                            estadoActual = EstadoBatalla.EJECUTAR_BATALLA;
                            tiempo = 0;
                            esperandoInput = true;
                        }
                        if (!(entradas.isEnter() || entradas.isClick())) {
                            esperandoInput = false;
                        }
                    }
                }
                break;
            case EJECUTAR_BATALLA:
                // Ejecutar el ataque automáticamente al entrar en este estado
                System.out.println("Ejecutar batalla");
                batalla.avanzarTurno(enemigoSeleccionado, ataqueSeleccionado);


                mostrarDanio = true;
                tiempoDanio = 0;

                tiempoDanio += delta;

                Imagen sprDanio = enemigoSpr[enemigoSeleccionado];
                danioSpr.setPosition(
                    (int)(sprDanio.getX() + sprDanio.getAncho()/2 - danioSpr.getAncho()/2),
                    (int)(sprDanio.getY() + sprDanio.getAlto()/2 - danioSpr.getAlto()/2)
                );

                Render.batch.begin();
                danioSpr.dibujar();
                danioSpr.dibujar();

                if (tiempoDanio >= DURACION_DANIO) {
                    mostrarDanio = false;
                    estadoActual = EstadoBatalla.RESULTADOS_COMBATE;
                }


                Render.batch.end();
                estadoActual = EstadoBatalla.RESULTADOS_COMBATE;
                tiempo = 0;
                break;
            case RESULTADOS_COMBATE:
                Texto logTexto = new Texto(Recursos.FUENTEMENU, 35, Color.WHITE, false);
                logTexto.setTexto(batalla.getLogCombate());
                logTexto.setPosition(50, 150);  // Donde quieras
                Render.batch.begin();
                logTexto.dibujar();
                Render.batch.end();
                // Si es turno de enemigo, cada input avanza un turno enemigo y muestra el log
                if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                    if (batalla.getTurno() != 0) {
                        batalla.avanzarTurno(0, 0); // Avanza un turno enemigo
                    } else {
                        if (opc >= enemigos.size()) opc = 0;
                        if (enemigoSeleccionado >= enemigos.size()) enemigoSeleccionado = 0;
                        estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
                    }
                    esperandoInput = true;
                }
                if (!(entradas.isEnter() || entradas.isClick())) {
                    esperandoInput = false;
                }
                break;
            default:
                break;
        }

        Render.renderer.begin(ShapeRenderer.ShapeType.Line);
        Render.renderer.setColor(Color.BLACK);

        Imagen spr = enemigoSpr[opc];
        Render.renderer.rect(spr.getX(), spr.getY(), spr.getAncho(), spr.getAlto());

        Render.renderer.end();







        //MENU OPCIONES despues ver que onda
        if (entradas.isEsc()){
            Render.app.setScreen(new PantallaOpciones());
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

    }
}
