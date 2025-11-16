package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.utiles.*;

import static com.dojan.infiernoperfecto.utiles.Render.app;

import io.Entradas;


public class PantallaOpciones implements Screen {
    private Imagen fondo;
    private Texto opciones[];
    private Texto titulo;

    // ✅ NUEVO: Variable para saber si estamos en batalla
    private boolean enBatalla;
    private int cantidadOpciones;

    // ✅ NUEVO: Constructor que recibe el contexto
    public PantallaOpciones(boolean enBatalla) {
        this.enBatalla = enBatalla;
        this.cantidadOpciones = enBatalla ? 5 : 4; // 5 opciones si está en batalla, 4 si no
    }

    // Constructor por defecto (para compatibilidad)
    public PantallaOpciones() {
        this(false); // Por defecto, NO está en batalla
    }

    public String getTextoOpcion(int opc) {
        switch (opc) {
            case 0: return "Volumen Musica: " + (ControlAudio.volumenMusica * 10) + "%";
            case 1: return "Volumen Efectos: " + (ControlAudio.volumenSFX * 10) + "%";
            case 2: return "Pantalla Completa: " + Gdx.graphics.isFullscreen();
            case 3: return "Volver";
            case 4: return "Salir al Menu"; // ✅ Nueva opción
            default: return "";
        }
    }

    GlyphLayout layout;
    private int opc = 1;
    private boolean mouseClick = false;
    private float tiempo;
    private float tiempoCooldown;
    private FitViewport fitViewport;

    private boolean esperandoInputEsc = false;
    private boolean esperandoInputVolver = false;
    private boolean saliendo = false;

    Entradas entradas = new Entradas();
    Config config = new Config();

    @Override
    public void show() {
        System.out.println("PantallaOpciones.show() - enBatalla: " + enBatalla);

        fitViewport = new FitViewport(800, 600);
        fondo = new Imagen(Recursos.FONDOOPCIONES);
        fondo.setSize(Config.ANCHO, Config.ALTO);

        layout = new GlyphLayout();

        entradas = new Entradas();
        Gdx.input.setInputProcessor(entradas);

        int avanceY = 40;
        int avanceTitulo = 20;
        int avanceX = -150;

        titulo = new Texto(Recursos.FUENTEMENU, 80, Color.WHITE, true);
        titulo.setTexto("OPCIONES");
        titulo.setPosition((int) ((Config.ANCHO - layout.width) / 3.1f),
            (int) (((Config.ALTO + layout.height) / 1.1f) + avanceTitulo));

        // ✅ Crear array del tamaño correcto
        opciones = new Texto[cantidadOpciones];

        for (int i = 0; i < opciones.length; i++) {
            opciones[i] = new Texto(Recursos.FUENTEMENU, 80, Color.WHITE, true);
            opciones[i].setTexto(getTextoOpcion(i));
        }

        for (int i = 0; i < opciones.length; i++) {
            int posY = (int) (((Config.ALTO / 1.4f) + (opciones[0].getAlto() / 2)) -
                (opciones[i].getAlto() + avanceY) * i);
            opciones[i].setPosition((int) ((Config.ANCHO / 2) - (opciones[i].getAncho() / 2)) + avanceX,
                posY);
        }

        tiempo = 0;
        tiempoCooldown = 0;
        esperandoInputEsc = false;
        esperandoInputVolver = false;
        saliendo = false;

        System.out.println("Opciones inicializadas: " + opciones.length);
    }

    @Override
    public void render(float delta) {
        if (saliendo) {
            return;
        }

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

        tiempo += delta;

        if (entradas.isAbajo()) {
            if (tiempo > 0.15f) {
                tiempo = 0;
                opc++;
                if (opc > cantidadOpciones) { // ✅ Usar cantidadOpciones
                    opc = 1;
                }
            }
        }

        if (entradas.isArriba()) {
            if (tiempo > 0.1f) {
                tiempo = 0;
                opc--;
                if (opc < 1) {
                    opc = cantidadOpciones; // ✅ Usar cantidadOpciones
                }
            }
        }

        if (!esperandoInputEsc && entradas.isEsc()) {
            saliendo = true;
            GestorPantallas.getInstance().volverAtras();
            return;
        }

        if (!entradas.isEsc()) {
            esperandoInputEsc = false;
        }

        // CUESTION MOUSE
        int cont = 0;
        for (int i = 0; i < opciones.length; i++) {
            if ((entradas.getMouseX() >= opciones[i].getX()) &&
                (entradas.getMouseX() <= (opciones[i].getX() + opciones[i].getAncho()))) {
                if ((entradas.getMouseY() >= opciones[i].getY() - opciones[i].getAlto()) &&
                    (entradas.getMouseY() <= opciones[i].getY())) {
                    opc = i + 1;
                    cont++;
                }
            }
        }

        mouseClick = (cont > 0);

        for (int i = 0; i < opciones.length; i++) {
            if (i == (opc - 1)) {
                opciones[i].setColor(Color.GOLDENROD);
            } else {
                opciones[i].setColor(Color.WHITE);
            }
        }

        tiempoCooldown += delta;

        if (entradas.isEnter() || entradas.isClick()) {
            if ((opc == 1) && entradas.isEnterPresionado()) {
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
            } else if (!esperandoInputVolver &&
                ((opc == 4 && entradas.isEnter()) ||
                    (opc == 4 && entradas.isClick() && mouseClick))) {
                // ✅ VOLVER
                System.out.println("Volver seleccionado");
                saliendo = true;
                esperandoInputVolver = true;

                if (GestorPantallas.getInstance().hayHistorial()) {
                    GestorPantallas.getInstance().volverAtras();
                } else {
                    app.setScreen(new PantallaMenu());
                }
                return;
            }
            // ✅ NUEVA OPCIÓN: Salir al menú (solo si está en batalla)
            else if (enBatalla && !esperandoInputVolver &&
                ((opc == 5 && entradas.isEnter()) ||
                    (opc == 5 && entradas.isClick() && mouseClick))) {
                System.out.println("Salir al Menu seleccionado");
                ControlAudio.pararMusica();
                saliendo = true;
                esperandoInputVolver = true;

                // Limpiar historial y volver al menú
                GestorPantallas.getInstance().limpiarHistorial();
                app.setScreen(new PantallaMenu());
                return;
            }
        }

        if (!entradas.isEnter() && !entradas.isClick()) {
            esperandoInputVolver = false;
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
        if (fondo != null) {
            try { fondo.dispose(); } catch(Exception e) {}
            fondo = null;
        }

        if (titulo != null) {
            try { titulo.dispose(); } catch(Exception e) {}
            titulo = null;
        }

        if (opciones != null) {
            for (int i = 0; i < opciones.length; i++) {
                if (opciones[i] != null) {
                    try { opciones[i].dispose(); } catch(Exception e) {}
                    opciones[i] = null;
                }
            }
        }
    }
}
