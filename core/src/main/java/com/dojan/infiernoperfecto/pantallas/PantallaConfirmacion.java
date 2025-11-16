package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.GestorPantallas;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;
import io.Entradas;

import static com.dojan.infiernoperfecto.utiles.Render.app;

public class PantallaConfirmacion implements Screen {
    private Texto titulo;
    private Texto mensaje;
    private Texto[] opciones = new Texto[2];

    private int opc = 0;
    private float tiempo = 0;
    private boolean mouseClick = false;
    private boolean esperandoInput = false;
    private boolean saliendo = false;
    private Entradas entradas;

    // ✅ Acción a ejecutar si confirma
    private Runnable accionConfirmar;

    /**
     * @param textoTitulo Título de la confirmación
     * @param textoMensaje Mensaje a mostrar
     * @param accionConfirmar Acción a ejecutar si presiona "Sí"
     */
    public PantallaConfirmacion(String textoTitulo, String textoMensaje, Runnable accionConfirmar) {
        this.accionConfirmar = accionConfirmar;

        // Inicializar textos
        titulo = new Texto(Recursos.FUENTEMENU, 70, Color.RED, true);
        titulo.setTexto(textoTitulo);

        mensaje = new Texto(Recursos.FUENTEMENU, 50, Color.WHITE, true);
        mensaje.setTexto(textoMensaje);

        opciones[0] = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, true);
        opciones[0].setTexto("NO");

        opciones[1] = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, true);
        opciones[1].setTexto("SI");
    }

    @Override
    public void show() {
        System.out.println("PantallaConfirmacion.show() ejecutado");

        entradas = new Entradas();
        Gdx.input.setInputProcessor(entradas);

        if (Render.renderer == null) {
            Render.renderer = new ShapeRenderer();
        }

        // Posicionar textos
        titulo.setPosition((int)(Config.ANCHO / 2 - titulo.getAncho() / 2),
            (int)(Config.ALTO / 1.5f));

        mensaje.setPosition((int)(Config.ANCHO / 2 - mensaje.getAncho() / 2),
            (int)(Config.ALTO / 2));

        // Opciones lado a lado
        int espacioEntreOpciones = 200;
        int centroX = Config.ANCHO / 2;

        opciones[0].setPosition(centroX - espacioEntreOpciones - (int)(opciones[0].getAncho() / 2),
            (int)(Config.ALTO / 3));

        opciones[1].setPosition(centroX + espacioEntreOpciones - (int)(opciones[1].getAncho() / 2),
            (int)(Config.ALTO / 3));

        opc = 0; // Iniciar en "NO" por seguridad
        tiempo = 0;
        esperandoInput = false;
        saliendo = false;
    }

    @Override
    public void render(float delta) {
        if (saliendo) {
            return;
        }

        // Limpiar pantalla
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Fondo semi-transparente oscuro
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

        Render.renderer.begin(ShapeRenderer.ShapeType.Filled);
        Render.renderer.setColor(0, 0, 0, 0.8f);
        Render.renderer.rect(0, 0, Config.ANCHO, Config.ALTO);
        Render.renderer.end();

        // Cuadro de diálogo
        Render.renderer.begin(ShapeRenderer.ShapeType.Filled);
        Render.renderer.setColor(0.2f, 0.2f, 0.2f, 1f);
        Render.renderer.rect(Config.ANCHO / 4, Config.ALTO / 4,
            Config.ANCHO / 2, Config.ALTO / 2);
        Render.renderer.end();

        // Borde del cuadro
        Render.renderer.begin(ShapeRenderer.ShapeType.Line);
        Render.renderer.setColor(Color.GOLDENROD);
        Gdx.gl.glLineWidth(3);
        Render.renderer.rect(Config.ANCHO / 4, Config.ALTO / 4,
            Config.ANCHO / 2, Config.ALTO / 2);
        Render.renderer.end();

        Gdx.gl.glDisable(GL20.GL_BLEND);

        // Dibujar textos
        Render.batch.begin();
        titulo.dibujar();
        mensaje.dibujar();
        opciones[0].dibujar();
        opciones[1].dibujar();
        Render.batch.end();

        tiempo += delta;

        // Navegación con teclado
        if (entradas.isIzquierda() || entradas.isDerecha()) {
            if (tiempo > 0.15f) {
                opc = (opc == 0) ? 1 : 0;
                tiempo = 0;
            }
        }

        // Detección de mouse
        int cont = 0;
        for (int i = 0; i < opciones.length; i++) {
            if ((entradas.getMouseX() >= opciones[i].getX()) &&
                (entradas.getMouseX() <= (opciones[i].getX() + opciones[i].getAncho()))) {
                if ((entradas.getMouseY() >= opciones[i].getY() - opciones[i].getAlto()) &&
                    (entradas.getMouseY() <= opciones[i].getY())) {
                    opc = i;
                    cont++;
                }
            }
        }

        mouseClick = (cont > 0);

        // Colorear opción seleccionada
        for (int i = 0; i < opciones.length; i++) {
            if (i == opc) {
                opciones[i].setColor(Color.GOLDENROD);
            } else {
                opciones[i].setColor(Color.WHITE);
            }
        }

        // Procesar selección
        if (!esperandoInput && (entradas.isEnter() || entradas.isClick())) {
            if ((entradas.isEnter()) || (entradas.isClick() && mouseClick)) {
                saliendo = true;
                esperandoInput = true;

                if (opc == 1) { // SÍ
                    System.out.println("Confirmado: SÍ - Ejecutando acción");
                    // Ejecutar la acción y salir
                    if (accionConfirmar != null) {
                        accionConfirmar.run();
                    }
                } else { // NO
                    System.out.println("Confirmado: NO - Volviendo atrás");
                    // ✅ SIMPLEMENTE VOLVER ATRÁS
                    GestorPantallas.getInstance().volverAtras();
                }
                return;
            }
        }

        if (!entradas.isEnter() && !entradas.isClick()) {
            esperandoInput = false;
        }

        // ESC = cancelar (igual que presionar NO)
        if (!esperandoInput && entradas.isEsc()) {
            System.out.println("Cancelado con ESC - Volviendo atrás");
            saliendo = true;
            // ✅ SIMPLEMENTE VOLVER ATRÁS
            GestorPantallas.getInstance().volverAtras();
            return;
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
        if (titulo != null) {
            try { titulo.dispose(); } catch(Exception e) {}
            titulo = null;
        }

        if (mensaje != null) {
            try { mensaje.dispose(); } catch(Exception e) {}
            mensaje = null;
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
