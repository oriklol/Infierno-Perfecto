package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dojan.infiernoperfecto.InfiernoPerfecto;
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

    private boolean esperandoInputEsc = false;
    private boolean esperandoInputVolver = false;
    private boolean saliendo = false;

    Entradas entradas = new Entradas();
    Config config = new Config();

    @Override
    public void show() {
        System.out.println("============================================");
        System.out.println("PantallaOpciones.show() EJECUTÁNDOSE");
        System.out.println("enBatalla: " + enBatalla);
        System.out.println("saliendo ANTES de resetear: " + saliendo);
        System.out.println("============================================");


        if (Render.renderer == null) {
            Render.renderer = new ShapeRenderer();
        }
        if (fondo != null) {
            try { fondo.dispose(); } catch(Exception e) {}
        }
        fondo = new Imagen(Recursos.FONDOOPCIONES);
        fondo.setSize(Config.ANCHO, Config.ALTO);

        layout = new GlyphLayout();

        entradas = new Entradas();
        Gdx.input.setInputProcessor(entradas);

        int avanceY = 40;
        int avanceTitulo = 20;
        int avanceX = -150;

        if (titulo != null) {
            try { titulo.dispose(); } catch(Exception e) {}
        }
        titulo = new Texto(Recursos.FUENTEMENU, 80, Color.WHITE, true);
        titulo.setTexto("OPCIONES");
        titulo.setPosition((int) ((Config.ANCHO - layout.width) / 3.1f),
            (int) (((Config.ALTO + layout.height) / 1.1f) + avanceTitulo));

        if (opciones != null) {
            for (int i = 0; i < opciones.length; i++) {
                if (opciones[i] != null) {
                    try { opciones[i].dispose(); } catch(Exception e) {}
                }
            }
        }

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
        System.out.println("saliendo DESPUÉS de resetear: " + saliendo);
        System.out.println("fondo != null: " + (fondo != null));
        System.out.println("titulo != null: " + (titulo != null));
        System.out.println("============================================");
    }

    @Override
    public void render(float delta) {
        System.out.println("PantallaOpciones.render() - saliendo: " + saliendo); // ← Agregar esto

        if (saliendo) {
            System.out.println("RENDER BLOQUEADO porque saliendo=true");
            return;
        }

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        int avanceX = 0;

        System.out.println("Dibujando fondo...");
        //usar camara desde infiernoperfecto
        Render.renderer.setProjectionMatrix(InfiernoPerfecto.camera.combined);
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
        System.out.println("Fondo dibujado OK"); // ← Agregar esto

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

        // mouse ahora se maneja desde entradas y es responsive al viewport
        int mouseX = entradas.getMouseX();
        int mouseY = entradas.getMouseY();

        int cont = 0;
        for (int i = 0; i < opciones.length; i++) {
            // ✅ Ahora mouseX y mouseY ya están en coordenadas 800x600
            if ((mouseX >= opciones[i].getX()) &&
                (mouseX <= (opciones[i].getX() + opciones[i].getAncho()))) {
                if ((mouseY >= opciones[i].getY() - opciones[i].getAlto()) &&
                    (mouseY <= opciones[i].getY())) {
                    opc = i +1 ;
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
                System.out.println("Salir al Menu - Mostrando confirmación");
                saliendo = true;
                esperandoInputVolver = true;

                // ✅ Crear pantalla de confirmación con la acción
                PantallaConfirmacion confirmacion = new PantallaConfirmacion(
                    "¿SALIR AL MENU?",
                    "Perderas el progreso de la batalla",
                    () -> {
                        // Esta acción se ejecuta si presiona "SÍ"
                        GestorPantallas.getInstance().limpiarHistorial();
                        app.setScreen(new PantallaMenu());
                    }
                );

                GestorPantallas.getInstance().irAPantalla(confirmacion);
                return;
            }
        }

        if (!entradas.isEnter() && !entradas.isClick()) {
            esperandoInputVolver = false;
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
