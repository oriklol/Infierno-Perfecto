package com.dojan.infiernoperfecto.pantallas.niveles;

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
import com.dojan.infiernoperfecto.entidades.enemigos.EnemigoLimbo1;
import com.dojan.infiernoperfecto.entidades.enemigos.EnemigoLimbo2;
import com.dojan.infiernoperfecto.entidades.enemigos.MiniBossLimbo;
import com.dojan.infiernoperfecto.pantallas.EstadoBatalla;
import com.dojan.infiernoperfecto.pantallas.PantallaOpciones;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.ControladorJuego;
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
    private int opc = 0;

    private boolean esperandoInput = false;
    private EstadoBatalla estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
    private final ArrayList<Enemigo> enemigos = new ArrayList<>();
    private final int CANT_ENEMIGOS_MAX = 3;
    private final int cantEnemigos = (Random.generarEntero(CANT_ENEMIGOS_MAX)) + 1;
    private final ArrayList<Imagen> enemigoSpr = new ArrayList<>();
    private static final String[] SPRITES_ENEMIGOS = {
        Recursos.ENEMIGOLIMBO2,
        Recursos.ENEMIGOLIMBO1
    };
    private int enemigoSeleccionado = 0;

    // private boolean jugadorMurio = false;
    private boolean inicializado = false;


    //private List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
    private Texto[] textoAtaques;
    private int ataqueSeleccionado = 0;

    @Override
    public void show() {

        if (!inicializado) {
            inicializarRecursos();
            inicializado = true;
        } else {
            // Si el renderer global fue liberado por otra pantalla, recrearlo aquí.
            if (Render.renderer == null) {
                Render.renderer = new ShapeRenderer();
            }
            // Asegurar que este screen reciba entradas
            Gdx.input.setInputProcessor(entradas);
        }
        // Si no hay batalla inicializada (ej. al entrar desde el menú), crear una de muestra
        if (batalla == null && Config.personajeSeleccionado != null) {
            reiniciarNivel(1, 1);
        }
        // Crear personaje de prueba
        // Config.personajeSeleccionado = new Jugador("personaje1", new Peleador());

        /* Cambia el fondo de la arena dependiendo la cantidad de enemigos
        if (cantEnemigos ==1){
            arena = new Imagen(Recursos.FONDOARENA1);
        }else if(cantEnemigos == 2){
            arena = new Imagen(Recursos.FONDOARENA2);
        }else{
            arena = new Imagen(Recursos.FONDOARENA3);
        }
        */
    }

    private void inicializarRecursos() {
        Gdx.input.setInputProcessor(entradas);
        Render.renderer = new ShapeRenderer();

        // Texturas que se reutilizan
        fondo = new Imagen(Recursos.FONDOLIMBO);
        arena = new Imagen(Recursos.FONDOARENA);
        danioSpr = new Imagen(Recursos.EFECTODANIO);

        // Texto de ubicación (se actualizará en reiniciarNivel)
        lugar = new Texto(Recursos.FUENTEMENU, 60, Color.BLACK, false);
        lugar.setPosition((int) (Config.ANCHO / 1.2f), (int) (Config.ALTO / 1.1f));
    }

    public void reiniciarNivel(int piso, int nivel) {

        if (!inicializado) {
            inicializarRecursos();
            inicializado = true;
        }

        // Limpiar estado anterior
        enemigos.clear();
        enemigoSpr.clear();

        // Actualizar ubicación
        lugar.setTexto(nivel + " - " + piso);

        // Verificar si es el nivel del miniboss
        if (nivel == 4) {
            // ========== NIVEL 4 = MINIBOSS ==========
            Enemigo miniboss = new MiniBossLimbo();
            Imagen spriteMiniboss = new Imagen(Recursos.MINIBOSSLIMBO);


            enemigos.add(miniboss);
            enemigoSpr.add(spriteMiniboss);

            System.out.println("¡MINIBOSS DEL PISO " + piso + " HA APARECIDO!");

        } else {
            // ========== NIVELES NORMALES (1, 2) ==========
            // int cantEnemigos = (Random.generarEntero(CANT_ENEMIGOS_MAX)) + 1;
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

        // Crear nueva batalla
        batalla = new Batalla(Config.personajeSeleccionado, enemigos);

        // Resetear estados
        estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
        opc = 0;
        enemigoSeleccionado = 0;
        ataqueSeleccionado = 0;
        esperandoInput = false;
        mostrarDanio = false;
        tiempoDanio = 0f;
        tiempo = 0f;

        // Actualizar textos de ataques
        List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
        textoAtaques = new Texto[ataques.size()];

        for (int i = 0; i < ataques.size(); i++) {
            textoAtaques[i] = new Texto(Recursos.FUENTEMENU, 40, Color.WHITE, false);
            textoAtaques[i].setTexto((i + 1) + " - " + ataques.get(i).getNombre());

            int x = (i < 2) ? 50 : 250;
            int y = (i % 2 == 0) ? 120 : 60;
            textoAtaques[i].setPosition(x, y);
        }
    }

    public void eliminarEnemigo(int indice) {
        if (indice >= 0 && indice < enemigos.size()) {
            enemigos.remove(indice);
            if (indice < enemigoSpr.size()) {
                Imagen sprite = enemigoSpr.remove(indice);
                sprite.dispose(); // liberar recursos
            }
            // Ajustar selección si es necesario
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
        // Dibujar fondo y arena
        Render.batch.begin();
        fondo.dibujar();
        arena.dibujar();
        lugar.dibujar();
        Render.batch.end();

        // Dibujar enemigos con HP
        Render.batch.begin();
        for (int i = 0; i < enemigos.size(); i++) {
            int posX;

            // Si hay solo 1 enemigo (miniboss), centrarlo
            if (enemigos.size() == 1) {
                posX = Config.ANCHO / 2 - (int)(enemigoSpr.get(i).getAncho() / 2);
            } else {
                // Enemigos normales en fila
                posX = (int) ((Config.ANCHO / 3.5f) * i) + 20;
            }

            enemigoSpr.get(i).setPosition(posX, Config.ALTO / 2);
            enemigoSpr.get(i).dibujar();

            // Dibujar HP del enemigo
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
                        // Solo avanzar si NO estamos esperando input y el usuario acaba de presionar
                        if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                            enemigoSeleccionado = opc;
                            estadoActual = EstadoBatalla.SELECCION_ATAQUE;
                            tiempo = 0;
                            esperandoInput = true;
                            System.out.println("enemigo seleccionado: " + enemigoSeleccionado);
                            // System.out.println("opc: " + opc);
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
                    // Obtener lista de ataques y Fe actual
                    List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
                    int feActual = Config.personajeSeleccionado.getFeActual();

                                // Detección de mouse sobre los ataques: solo cambia la selección si el ataque está disponible
                    int mouseX = Gdx.input.getX();
                    int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY(); // invertir Y
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

                    // Colorear según disponibilidad y selección: los no usables en rojo
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

                    // Dibujar textos de ataques
                    for (Texto textoAtaque : textoAtaques) {
                        Render.batch.begin();
                        textoAtaque.dibujar();
                        Render.batch.end();
                    }

                    // Asegurar índices válidos antes de usar ataqueSeleccionado
                    if (ataqueSeleccionado < 0) ataqueSeleccionado = 0;
                    if (textoAtaques.length == 0) {
                        // No hay ataques definidos, volver a estado de selección enemigo
                        estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
                        break;
                    }
                    if (ataqueSeleccionado >= textoAtaques.length) ataqueSeleccionado = textoAtaques.length - 1;
                    Ataque ataqueSel = Config.personajeSeleccionado.getClase().getAtaques().get(ataqueSeleccionado);
                    Render.batch.begin();
                    // Mostrar vida y Fe del jugador uno al lado del otro
                    Texto textoPS = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
                    textoPS.setTexto("P.S. " + (int) Config.personajeSeleccionado.getVidaActual());
                    textoPS.setPosition(500, 120);
                    textoPS.dibujar();
                    // Mostrar Fe a la derecha de la vida
                    Texto textoFe = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
                    textoFe.setTexto("Fe: " + Config.personajeSeleccionado.getFeActual());
                    textoFe.setPosition((int)(textoPS.getX() + textoPS.getAncho() + 20), 120);
                    textoFe.dibujar();

                    Texto textoUsos = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
                    textoUsos.setTexto("Usos: " + ataqueSel.getCantUsos() + "   Daño: " + ataqueSel.getDanio());
                    textoUsos.setPosition(500, 80);
                    textoUsos.dibujar();
                    // Mostrar costo de Fe del ataque solo si tiene costo
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
                        // Solo avanzar si NO estamos esperando input y el usuario acaba de presionar
                        if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                                // Solo ejecutar si el ataque seleccionado es usable
                                if (ataqueSeleccionado < 0 || ataqueSeleccionado >= ataques.size()) {
                                    System.out.println("Seleccion invalida de ataque, ignorando");
                                } else {
                                    Ataque aSel = ataques.get(ataqueSeleccionado);
                                    boolean usable = aSel.getCantUsos() > 0 && aSel.getCostoFe() <= feActual;
                                    if (usable) {
                                        System.out.println("ataque seleccionado: " + ataqueSeleccionado);
                                        estadoActual = EstadoBatalla.EJECUTAR_BATALLA;
                                        tiempo = 0;
                                        esperandoInput = true;
                                    } else {
                                        // No hacer nada si no es usable
                                        System.out.println("Ataque no usable, ignorando entrada");
                                    }
                                }
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

                Imagen sprDanio = enemigoSpr.get(enemigoSeleccionado);
                danioSpr.setPosition(
                    (int) (sprDanio.getX() + sprDanio.getAncho() / 2 - danioSpr.getAncho() / 2),
                    (int) (sprDanio.getY() + sprDanio.getAlto() / 2 - danioSpr.getAlto() / 2)
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
                logTexto.setPosition(50, 150);
                Render.batch.begin();
                logTexto.dibujar();
                Render.batch.end();

                if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                    // Obtener lista de enemigos muertos este turno
                    List<Integer> enemigosMuertos = batalla.getEnemigosMuertosEsteTurno();

                    // Eliminar enemigos muertos (de atrás hacia adelante para evitar problemas de índices)
                    for (int i = enemigosMuertos.size() - 1; i >= 0; i--) {
                        int indice = enemigosMuertos.get(i);
                        System.out.println("Eliminando enemigo en índice: " + indice);
                        eliminarEnemigo(indice);
                    }

                    // Verificar si la batalla terminó
                    if (batalla.batallaTerminada()) {
                        estadoActual = EstadoBatalla.FIN_BATALLA;
                    } else if (batalla.getTurno() != 0) {
                        // Turno enemigo
                        batalla.avanzarTurno(0, 0);
                    } else {
                        // Volver a selección de enemigo
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
                    ControladorJuego.getInstance().gameOver();  // ← Usa el controlador
                } else {
                    estadoActual = null; // evitar repetir
                    ControladorJuego.getInstance().avanzarNivel();  // ← Usa el controlador
                }
                break;
        }

        Render.renderer.begin(ShapeRenderer.ShapeType.Line);
        Render.renderer.setColor(Color.BLACK);

        // Dibujar rect alrededor del enemigo seleccionado si existe
        if (enemigoSpr != null && !enemigos.isEmpty() && opc >= 0 && opc < enemigoSpr.size() && enemigoSpr.get(opc) != null) {
            Imagen spr = enemigoSpr.get(opc);
            Render.renderer.rect(spr.getX(), spr.getY(), spr.getAncho(), spr.getAlto());
        }

        Render.renderer.end();


        //MENU OPCIONES despues ver que onda
        /*
        Deberia ser una clase "PantallaOpcionesInGame" y que el volver te deje al punto de la partida donde estabas
         */
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
        // dispose textures and fonts created by this screen
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

        if (enemigoSpr != null){
            for (Imagen sprite : enemigoSpr) { // ← cambiar el for
                if (sprite != null){
                    try{ sprite.dispose(); }catch(Exception e){}
                }
            }
            enemigoSpr.clear(); // ← agregar clear
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

        // dispose renderer if this screen created it
        // No se debe disponer del renderer global aquí (lo gestiona la aplicación central).
    }
}
