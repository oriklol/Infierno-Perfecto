package com.dojan.infiernoperfecto.pantallas.niveles;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dojan.infiernoperfecto.InfiernoPerfecto;
import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.batalla.Batalla;
import com.dojan.infiernoperfecto.comandos.ComandoAtacar;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Musica;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.pantallas.enciclopedia.PantallaEnciclopedia;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;
import com.dojan.infiernoperfecto.entidades.enemigos.limbo.EnemigoLimbo1;
import com.dojan.infiernoperfecto.entidades.enemigos.limbo.EnemigoLimbo2;
import com.dojan.infiernoperfecto.entidades.enemigos.limbo.MiniBossLimbo;
import com.dojan.infiernoperfecto.logica.ResultadoCombate;
import com.dojan.infiernoperfecto.pantallas.EstadoBatalla;
import com.dojan.infiernoperfecto.pantallas.PantallaOpciones;
import com.dojan.infiernoperfecto.pantallas.batalla.ControladorBatallaLocal;
import com.dojan.infiernoperfecto.utiles.*;

import io.Entradas;

public class PantallaLimbo implements Screen {
    private Musica musicaFondo;
    private Imagen fondo;
    private Imagen arena;
    private Imagen danioSpr;
    private boolean mostrarDanio = false;
    private float tiempoDanio = 0f;
    private final float DURACION_DANIO = 0.5f;

    private Texto lugar;
    private Texto textoEnemigoSeleccionado;
    private Texto vidaEnemigoTexto;
    private Texto textoPS;
    private Texto textoFe;
    private Texto textoUsos;
    private Texto textoCostoFe;
    private Texto logTexto;

    Entradas entradas = new Entradas();
    private Batalla batalla;
    private float tiempo;
    private int opc = 0;

    private boolean esperandoInput = false;
    private EstadoBatalla estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
    private final ArrayList<Enemigo> enemigos = new ArrayList<>();
    private final int CANT_ENEMIGOS_MAX = 3;
    private final int cantEnemigos = (Random.generarEntero(CANT_ENEMIGOS_MAX)) + 1;
    private final ArrayList<Imagen> enemigoSpr = new ArrayList<>();
    private int enemigoSeleccionado = 0;

    private boolean inicializado = false;

    private Texto[] textoAtaques;
    private int ataqueSeleccionado = 0;

    private ControladorBatallaLocal controladorBatalla;
    private ResultadoCombate ultimoResultado;

    private boolean esperandoEsc = false;

    @Override
    public void show() {
        System.out.println("PantallaLimbo.show() ejecutado");

        musicaFondo = new Musica(Recursos.MUSICABATALLA);
        ControlAudio.setMusicaActual(musicaFondo);

        if (!inicializado) {
            inicializarRecursos();
            inicializado = true;
        } else {
            if (Render.renderer == null) {
                Render.renderer = new ShapeRenderer();
            }
            Gdx.input.setInputProcessor(entradas);
        }

        if (batalla == null && Config.personajeSeleccionado != null) {
            reiniciarNivel(1, 1);
        }

        esperandoEsc = false;
        esperandoInput = false;
        tiempo = 0;

        entradas = new Entradas();
        Gdx.input.setInputProcessor(entradas);
        System.out.println("esperandoEsc reseteado a: " + esperandoEsc);
    }

    private void inicializarRecursos() {
        Gdx.input.setInputProcessor(entradas);
        if (Render.renderer == null) {
            Render.renderer = new ShapeRenderer();
        }

        fondo = new Imagen(Recursos.FONDOLIMBO);
        arena = new Imagen(Recursos.FONDOARENA);
        danioSpr = new Imagen(Recursos.EFECTODANIO);

        lugar = new Texto(Recursos.FUENTEMENU, 60, Color.BLACK, false);
        lugar.setPosition((int) (Config.ANCHO / 1.2f), (int) (Config.ALTO / 1.1f));

        textoEnemigoSeleccionado = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, false);
        vidaEnemigoTexto = new Texto(Recursos.FUENTEMENU, 30, Color.WHITE, false);
        textoPS = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
        textoFe = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
        textoUsos = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
        textoCostoFe = new Texto(Recursos.FUENTEMENU, 32, Color.CORAL, false);
        logTexto = new Texto(Recursos.FUENTEMENU, 35, Color.WHITE, false);
    }

    public void reiniciarNivel(int piso, int nivel) {
        if (!inicializado) {
            inicializarRecursos();
            inicializado = true;
        }

        enemigos.clear();
        enemigoSpr.clear();

        lugar.setTexto(nivel + " - " + piso);

        if (nivel == 4) {
            Enemigo miniboss = new MiniBossLimbo();
            Imagen spriteMiniboss = new Imagen(Recursos.MINIBOSSLIMBO);

            enemigos.add(miniboss);
            enemigoSpr.add(spriteMiniboss);

            System.out.println("¡MINIBOSS DEL PISO " + piso + " HA APARECIDO!");
        } else {
            for (int i = 0; i < cantEnemigos; i++) {
                Enemigo enemigo;
                int tipoEnemigo = Random.generarEntero(2);

                Imagen sprite;
                if (tipoEnemigo == 1) {
                    enemigo = new EnemigoLimbo1();
                    sprite = new Imagen(Recursos.ENEMIGOLIMBO2);
                } else {
                    enemigo = new EnemigoLimbo2();
                    sprite = new Imagen(Recursos.ENEMIGOLIMBO1);
                }
                enemigos.add(enemigo);
                enemigoSpr.add(sprite);
            }
        }

        batalla = new Batalla(Config.personajeSeleccionado, enemigos);

        estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
        opc = 0;
        enemigoSeleccionado = 0;
        ataqueSeleccionado = 0;
        esperandoInput = false;
        mostrarDanio = false;
        tiempoDanio = 0f;
        tiempo = 0f;

        List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
        textoAtaques = new Texto[ataques.size()];

        for (int i = 0; i < ataques.size(); i++) {
            textoAtaques[i] = new Texto(Recursos.FUENTEMENU, 40, Color.WHITE, false);
            textoAtaques[i].setTexto((i + 1) + " - " + ataques.get(i).getNombre());

            int x = (i < 2) ? 50 : 250;
            int y = (i % 2 == 0) ? 120 : 60;
            textoAtaques[i].setPosition(x, y);
        }

        controladorBatalla = new ControladorBatallaLocal(batalla);
    }

    public void eliminarEnemigo(int indice) {
        if (indice >= 0 && indice < enemigos.size()) {
            enemigos.remove(indice);
            if (indice < enemigoSpr.size()) {
                Imagen sprite = enemigoSpr.remove(indice);
                sprite.dispose();
            }
            if (enemigoSeleccionado >= enemigos.size() && enemigos.size() > 0) {
                enemigoSeleccionado = enemigos.size() - 1;
            }
            if (opc >= enemigos.size() && enemigos.size() > 0) {
                opc = enemigos.size() - 1;
            }
        }
    }

    @Override
    public void render(float delta) {
        ControlAudio.reproducirMusica();

        // ✅ CRÍTICO: Configurar ShapeRenderer con la cámara del viewport
        Render.renderer.setProjectionMatrix(InfiernoPerfecto.camera.combined);

        Render.batch.begin();
        fondo.dibujar();
        arena.dibujar();
        lugar.dibujar();
        Render.batch.end();

        Render.batch.begin();
        for (int i = 0; i < enemigos.size(); i++) {
            int posX;

            if (enemigos.size() == 1) {
                posX = Config.ANCHO / 2 - (int)(enemigoSpr.get(i).getAncho() / 2);
            } else {
                posX = (int) ((Config.ANCHO / 3.5f) * i) + 20;
            }

            enemigoSpr.get(i).setPosition(posX, Config.ALTO / 2);
            enemigoSpr.get(i).dibujar();

            Enemigo enemigo = enemigos.get(i);
            Texto vidaEnemigo = new Texto(Recursos.FUENTEMENU, 30, Color.WHITE, false);
            vidaEnemigo.setTexto("HP: " + enemigo.getVidaActual() + " - " + enemigo.getVidaBase());
            vidaEnemigo.setPosition(
                (int) (enemigoSpr.get(i).getX() + enemigoSpr.get(i).getAncho() / 2 - vidaEnemigo.getAncho() / 2),
                (int) (enemigoSpr.get(i).getY() - 20)
            );
            vidaEnemigo.dibujar();
        }
        Render.batch.end();

        if (!esperandoEsc && entradas.isEnciclopedia()) {
            GestorPantallas.getInstance().irAPantalla(new PantallaEnciclopedia());
            esperandoEsc = true;
        }

        if (!esperandoEsc && entradas.isEsc()) {
            GestorPantallas.getInstance().irAPantalla(new PantallaOpciones(true));
            System.out.println("entrando opciones desde batalla");
            esperandoEsc = true;
        }

        if (!entradas.isEsc() && !entradas.isEnciclopedia()) {
            if (esperandoEsc) {
                System.out.println("Reseteando esperandoEsc porque tecla soltada");
            }
            esperandoEsc = false;
        }

        tiempo += delta;

        if (null != estadoActual) switch (estadoActual) {
            case SELECCION_ENEMIGO:
                if (batalla.getTurno() == 0) {
                    if (opc >= enemigos.size()) opc = 0;
                    if (opc < 0) opc = enemigos.size() - 1;
                    if (enemigoSeleccionado >= enemigos.size()) enemigoSeleccionado = 0;
                    if (enemigoSeleccionado < 0) enemigoSeleccionado = 0;

                    // ✅ CORRECCIÓN: Usar coordenadas transformadas del viewport
                    int mouseX = entradas.getMouseX();
                    int mouseY = entradas.getMouseY();

                    for (int i = 0; i < enemigos.size(); i++) {
                        Imagen spr = enemigoSpr.get(i);
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

                        if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                            enemigoSeleccionado = opc;
                            estadoActual = EstadoBatalla.SELECCION_ATAQUE;
                            tiempo = 0;
                            esperandoInput = true;
                            System.out.println("enemigo seleccionado: " + enemigoSeleccionado);
                        }

                        if (!(entradas.isEnter() || entradas.isClick())) {
                            esperandoInput = false;
                        }
                    }
                }

                Render.batch.begin();
                Texto textoEnemigoSeleccionado = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, false);
                textoEnemigoSeleccionado.setTexto("Selecciona a un enemigo");
                textoEnemigoSeleccionado.setPosition((Config.ANCHO / 2 - ((int) textoEnemigoSeleccionado.getAncho() / 2)), 120);
                textoEnemigoSeleccionado.dibujar();
                Render.batch.end();

                break;

            case SELECCION_ATAQUE:
                if (batalla.getTurno() == 0) {
                    List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
                    int feActual = Config.personajeSeleccionado.getFeActual();

                    // ✅ CORRECCIÓN: Usar coordenadas transformadas del viewport
                    int mouseX = entradas.getMouseX();
                    int mouseY = entradas.getMouseY();

                    for (int i = 0; i < textoAtaques.length; i++) {
                        Texto textoAtaque = textoAtaques[i];
                        int x = textoAtaque.getX();
                        int y = textoAtaque.getY() - 50;
                        int ancho = (int) textoAtaque.getAncho();
                        int alto = (int) textoAtaque.getAlto() + 50;
                        if (mouseX >= x && mouseX <= x + ancho && mouseY >= y && mouseY <= y + alto) {
                            Ataque a = ataques.get(i);
                            boolean usable = a.getCantUsos() > 0 && a.getCostoFe() <= feActual;
                            if (usable) {
                                ataqueSeleccionado = i;
                            }
                        }
                    }

                    for (int i = 0; i < textoAtaques.length; i++) {
                        Ataque a = ataques.get(i);
                        boolean usable = a.getCantUsos() > 0 && a.getCostoFe() <= feActual;
                        if (!usable) {
                            textoAtaques[i].setColor(Color.RED);
                        } else if (i == ataqueSeleccionado) {
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

                    if (ataqueSeleccionado < 0) ataqueSeleccionado = 0;
                    if (textoAtaques.length == 0) {
                        estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
                        break;
                    }
                    if (ataqueSeleccionado >= textoAtaques.length) ataqueSeleccionado = textoAtaques.length - 1;

                    Ataque ataqueSel = Config.personajeSeleccionado.getClase().getAtaques().get(ataqueSeleccionado);

                    Render.batch.begin();
                    Texto textoPS = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
                    textoPS.setTexto("P.S. " + (int) Config.personajeSeleccionado.getVidaActual());
                    textoPS.setPosition(500, 120);
                    textoPS.dibujar();

                    Texto textoFe = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
                    textoFe.setTexto("Fe: " + Config.personajeSeleccionado.getFeActual());
                    textoFe.setPosition((int)(textoPS.getX() + textoPS.getAncho() + 20), 120);
                    textoFe.dibujar();

                    Texto textoUsos = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
                    textoUsos.setTexto("Usos: " + ataqueSel.getCantUsos() + "   Daño: " + ataqueSel.getDanio());
                    textoUsos.setPosition(500, 80);
                    textoUsos.dibujar();

                    if (ataqueSel.getCostoFe() > 0) {
                        Texto textoCostoFe = new Texto(Recursos.FUENTEMENU, 32, Color.CORAL, false);
                        textoCostoFe.setTexto("Costo Fe: " + ataqueSel.getCostoFe());
                        textoCostoFe.setPosition(500, 40);
                        textoCostoFe.dibujar();
                    }
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

                        if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                            ComandoAtacar comando = new ComandoAtacar(
                                enemigoSeleccionado,
                                ataqueSeleccionado
                            );

                            ultimoResultado = controladorBatalla.procesarComando(comando);

                            if (ultimoResultado.isValido()) {
                                batalla.saltarTurnoJugador();
                                estadoActual = EstadoBatalla.EJECUTAR_BATALLA;
                                tiempoDanio = 0;
                                esperandoInput = true;
                            } else {
                                System.out.println("Error: " + ultimoResultado.getMensajeError());
                            }
                        }

                        if (!(entradas.isEnter() || entradas.isClick())) {
                            esperandoInput = false;
                        }
                    }
                }
                break;

            case EJECUTAR_BATALLA:
                mostrarDanio = true;
                tiempoDanio += delta;

                Imagen sprDanio = enemigoSpr.get(enemigoSeleccionado);
                danioSpr.setPosition(
                    (int) (sprDanio.getX() + sprDanio.getAncho() / 2 - danioSpr.getAncho() / 2),
                    (int) (sprDanio.getY() + sprDanio.getAlto() / 2 - danioSpr.getAlto() / 2)
                );

                Render.batch.begin();
                danioSpr.dibujar();
                Render.batch.end();

                if (tiempoDanio >= DURACION_DANIO) {
                    mostrarDanio = false;
                    estadoActual = EstadoBatalla.RESULTADOS_COMBATE;
                    tiempo = 0;
                }
                break;

            case RESULTADOS_COMBATE:
                if (batalla.getTurno() == 1) {
                    mostrarResultado(ultimoResultado);
                } else {
                    logTexto.setTexto(batalla.getLogCombate());
                    logTexto.setPosition(50, 150);

                    Render.batch.begin();
                    logTexto.dibujar();
                    Render.batch.end();
                }

                if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                    if (ultimoResultado.isObjetivoMurio()) {
                        eliminarEnemigo(enemigoSeleccionado);
                    }
                    
                    for (Integer index : batalla.getEnemigosMuertosEsteTurno()) {
                        eliminarEnemigo(index);
                    }

                    if (controladorBatalla.estaTerminada()) {
                        estadoActual = EstadoBatalla.FIN_BATALLA;
                    } else if (batalla.getTurno() != 0) {
                        batalla.avanzarTurno(0, 0);
                    } else {
                        estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
                    }

                    esperandoInput = true;
                }

                if (!(entradas.isEnter() || entradas.isClick())) {
                    esperandoInput = false;
                }
                break;

            case FIN_BATALLA:
                if (!Config.personajeSeleccionado.sigueVivo()) {
                    ControladorJuego.getInstance().gameOver();
                } else {
                    estadoActual = null;
                    ControladorJuego.getInstance().avanzarNivel();
                }
                break;
        }

        Render.renderer.begin(ShapeRenderer.ShapeType.Line);
        Render.renderer.setColor(Color.BLACK);

        if (enemigoSpr != null && !enemigos.isEmpty() && opc >= 0 && opc < enemigoSpr.size() && enemigoSpr.get(opc) != null) {
            Imagen spr = enemigoSpr.get(opc);
            Render.renderer.rect(spr.getX(), spr.getY(), spr.getAncho(), spr.getAlto());
        }

        Render.renderer.end();
    }

    private void mostrarResultado(ResultadoCombate resultado) {
        String log = "Hiciste " + resultado.getDanio() + " de daño";
        if (resultado.getEfectoMensaje() != null) {
            log += "\n" + resultado.getEfectoMensaje();
        }

        logTexto.setTexto(log);
        logTexto.setPosition(50, 150);

        Render.batch.begin();
        logTexto.dibujar();
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
        if (fondo != null){
            try{ fondo.dispose(); }catch(Exception e){}
            fondo = null;
        }

        if (arena != null){
            try{ arena.dispose(); }catch(Exception e){}
            arena = null;
        }

        if (danioSpr != null){
            try{ danioSpr.dispose(); }catch(Exception e){}
            danioSpr = null;
        }

        if (lugar != null){
            try{ lugar.dispose(); }catch(Exception e){}
            lugar = null;
        }

        if (enemigoSpr != null) {
            for (Imagen sprite : enemigoSpr) {
                if (sprite != null){
                    try{ sprite.dispose(); }catch(Exception e){}
                }
            }
            enemigoSpr.clear();
        }

        if (textoAtaques != null){
            for (int i=0;i<textoAtaques.length;i++){
                if (textoAtaques[i] != null){
                    try{ textoAtaques[i].dispose(); }catch(Exception e){}
                    textoAtaques[i]=null;
                }
            }
            textoAtaques = null;
        }
    }
}
