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
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.entidades.enemigos.Enemigo;
import com.dojan.infiernoperfecto.entidades.enemigos.traicion.BossFinal;
import com.dojan.infiernoperfecto.logica.ResultadoCombate;
import com.dojan.infiernoperfecto.pantallas.EstadoBatalla;
import com.dojan.infiernoperfecto.pantallas.PantallaOpciones;
import com.dojan.infiernoperfecto.pantallas.batalla.ControladorBatallaLocal;
import com.dojan.infiernoperfecto.pantallas.enciclopedia.PantallaEnciclopedia;
import com.dojan.infiernoperfecto.utiles.*;

import io.Entradas;


public class PantallaTraicion implements Screen {
    private Imagen fondo;
    private Imagen arena;
    private Imagen danioSpr;
    private boolean mostrarDanio = false;
    private float tiempoDanio = 0f;
    private final float DURACION_DANIO = 0.5f;

    private Texto lugar; // muestra el nivel y el piso
    // Reutilizables para evitar crear fuentes en el loop de render
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


    private ControladorBatallaLocal controladorBatalla;
    private ResultadoCombate ultimoResultado;

    private boolean esperandoEsc = false;

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

        // resetear estados
        esperandoEsc = false;
        esperandoInput = false;
        tiempo = 0;

        entradas = new Entradas(); // ← Esto limpia todos los inputs previos
        Gdx.input.setInputProcessor(entradas);
        System.out.println("esperandoEsc reseteado a: " + esperandoEsc);
    }

    private void inicializarRecursos() {
        Gdx.input.setInputProcessor(entradas);
        if (Render.renderer == null) {
            Render.renderer = new ShapeRenderer();
        }

        // Texturas que se reutilizan
        fondo = new Imagen(Recursos.FONDOSTRAICION[2]);
        arena = new Imagen(Recursos.FONDOARENA);
        danioSpr = new Imagen(Recursos.EFECTODANIO);

        // Texto de ubicación (se actualizará en reiniciarNivel)
        lugar = new Texto(Recursos.FUENTEMENU, 60, Color.BLACK, false);
        lugar.setPosition((int) (Config.ANCHO / 1.2f), (int) (Config.ALTO / 1.1f));

        //textos reutilizables
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

        // Limpiar estado anterior
        enemigos.clear();
        enemigoSpr.clear();

        // Actualizar ubicación
        lugar.setTexto(nivel + " - " + piso);

        // Verificar si es el nivel del miniboss
        Enemigo bossFinal = new BossFinal();  // Tu clase de boss final
        Imagen spriteBoss = new Imagen(Recursos.BOSSFINAL);

        enemigos.add(bossFinal);
        enemigoSpr.add(spriteBoss);

        System.out.println("¡EL BOSS FINAL HA APARECIDO!");

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

        controladorBatalla = new ControladorBatallaLocal(batalla);
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

        Render.renderer.setProjectionMatrix(InfiernoPerfecto.camera.combined);

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

            // Dibujar HP del enemigo (reutilizando Texto)
            Enemigo enemigo = enemigos.get(i);
            vidaEnemigoTexto.setTexto("HP: " + enemigo.getVidaActual() + " - " + enemigo.getVidaBase());
            vidaEnemigoTexto.setPosition(
                (int) (enemigoSpr.get(i).getX() + enemigoSpr.get(i).getAncho() / 2 - vidaEnemigoTexto.getAncho() / 2),
                (int) (enemigoSpr.get(i).getY() - 20)
            );
            vidaEnemigoTexto.dibujar();
        }
        Render.batch.end();

        // otras pantallas

        if (!esperandoEsc && entradas.isEnciclopedia()) {
            GestorPantallas.getInstance().irAPantalla(new PantallaEnciclopedia());
            esperandoEsc = true;
        }

        if (!esperandoEsc && entradas.isEsc()) {
            GestorPantallas.getInstance().irAPantalla(new PantallaOpciones(true)); // ✅ true = en batalla
            System.out.println("entrando opciones desde batalla");
            esperandoEsc = true;
        }

        if (!entradas.isEsc() && !entradas.isEnciclopedia()) {
            if (esperandoEsc) {
                System.out.println("Reseteando esperandoEsc porque tecla soltada"); // ← DEBUG
            }
            esperandoEsc = false;
        }

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
                textoEnemigoSeleccionado.setPosition(
                    (Config.ANCHO / 2 - ((int) textoEnemigoSeleccionado.getAncho() / 2)),
                    120
                );
                textoEnemigoSeleccionado.dibujar();
                Render.batch.end();

                break;
            case SELECCION_ATAQUE:
                if (batalla.getTurno() == 0) {
                    List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
                    int feActual = Config.personajeSeleccionado.getFeActual();

                    // Detección de mouse sobre los ataques
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

                    // Colorear según disponibilidad
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

                    // Validaciones de índices
                    if (ataqueSeleccionado < 0) ataqueSeleccionado = 0;
                    if (textoAtaques.length == 0) {
                        estadoActual = EstadoBatalla.SELECCION_ENEMIGO;
                        break;
                    }
                    if (ataqueSeleccionado >= textoAtaques.length) ataqueSeleccionado = textoAtaques.length - 1;

                    Ataque ataqueSel = Config.personajeSeleccionado.getClase().getAtaques().get(ataqueSeleccionado);

                    // Mostrar info del jugador
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

                    // Navegación con teclado
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

                        // ✅ AQUÍ ESTÁ LA CORRECCIÓN
                        if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                            // 1. Crear comando
                            ComandoAtacar comando = new ComandoAtacar(
                                enemigoSeleccionado,
                                ataqueSeleccionado
                            );

                            // 2. Procesar comando
                            ultimoResultado = controladorBatalla.procesarComando(comando);

                            // 3. Verificar si fue válido
                            if (ultimoResultado.isValido()) {
                                estadoActual = EstadoBatalla.EJECUTAR_BATALLA;
                                tiempoDanio = 0; // ← RESETEAR el contador de tiempo de animación
                                esperandoInput = true;
                            } else {
                                System.out.println("Error: " + ultimoResultado.getMensajeError());
                            }
                        }

                        // ✅ ESTO ES LO QUE FALTABA
                        if (!(entradas.isEnter() || entradas.isClick())) {
                            esperandoInput = false;
                        }
                    }
                }
                break;

            case EJECUTAR_BATALLA:
                // ✅ ELIMINAR esta línea:
                // batalla.avanzarTurno(enemigoSeleccionado, ataqueSeleccionado); // ❌ BORRAR

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
                /* antes de red
                Texto logTexto = new Texto(Recursos.FUENTEMENU, 35, Color.WHITE, false);
                logTexto.setTexto(batalla.getLogCombate());
                logTexto.setPosition(50, 150);
                Render.batch.begin();
                logTexto.dibujar();
                Render.batch.end();
                */
                // after red
                mostrarResultado(ultimoResultado);

                if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
                    // Eliminar enemigos muertos
                    if (ultimoResultado.isObjetivoMurio()) {
                        eliminarEnemigo(enemigoSeleccionado);
                    }

                    // Verificar fin de batalla
                    if (controladorBatalla.estaTerminada()) {
                        estadoActual = EstadoBatalla.FIN_BATALLA;
                    } else if (batalla.getTurno() != 0) {
                        // Turno enemigo
                        // AQUÍ TENDRÍAS QUE ADAPTAR LA LÓGICA DEL ENEMIGO TAMBIÉN
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

        // dispose textos reutilizables
        if (textoEnemigoSeleccionado != null){ try{ textoEnemigoSeleccionado.dispose(); }catch(Exception e){} textoEnemigoSeleccionado = null; }
        if (vidaEnemigoTexto != null){ try{ vidaEnemigoTexto.dispose(); }catch(Exception e){} vidaEnemigoTexto = null; }
        if (textoPS != null){ try{ textoPS.dispose(); }catch(Exception e){} textoPS = null; }
        if (textoFe != null){ try{ textoFe.dispose(); }catch(Exception e){} textoFe = null; }
        if (textoUsos != null){ try{ textoUsos.dispose(); }catch(Exception e){} textoUsos = null; }
        if (textoCostoFe != null){ try{ textoCostoFe.dispose(); }catch(Exception e){} textoCostoFe = null; }
        if (logTexto != null){ try{ logTexto.dispose(); }catch(Exception e){} logTexto = null; }

        // dispose renderer if this screen created it
        // No se debe disponer del renderer global aquí (lo gestiona la aplicación central).
    }
}
