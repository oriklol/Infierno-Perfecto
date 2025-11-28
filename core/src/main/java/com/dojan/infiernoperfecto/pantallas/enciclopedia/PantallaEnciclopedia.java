package com.dojan.infiernoperfecto.pantallas.enciclopedia;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dojan.infiernoperfecto.InfiernoPerfecto;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.GestorPantallas;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;
import io.Entradas;

import java.util.Arrays;
import java.util.List;

public class PantallaEnciclopedia implements Screen {

    private Imagen fondo;
    private Texto titulo;
    private Texto textoVolver;
    private Texto[] textosCategorias;
    private Texto[] textosEntradas;
    private Texto textoDetalle;

    private List<CategoriaEnciclopedia> categorias;
    private CategoriaEnciclopedia categoriaActual;
    private List<EntradaEnciclopedia> entradasFiltradas;

    private int categoriaSeleccionada = 0;
    private int entradaSeleccionada = 0;
    private float tiempo = 0;

    private Entradas entradas = new Entradas();
    private boolean mouseClickCategoria = false; // ✅ SEPARAR detección de categorías
    private boolean mouseClickEntrada = false;   // ✅ SEPARAR detección de entradas
    private ShapeRenderer shapeRenderer;

    // Posiciones de layout
    private final int MARGEN = 50;
    private final int ANCHO_LISTA_CATEGORIAS = 200;
    private final int ANCHO_LISTA_ENTRADAS = 250;

    @Override
    public void show() {
        fondo = new Imagen(Recursos.FONDOOPCIONES);

        entradas = new Entradas();
        Gdx.input.setInputProcessor(entradas);

        if (Render.renderer == null) {
            Render.renderer = new ShapeRenderer();
        }
        shapeRenderer = Render.renderer;

        titulo = new Texto(Recursos.FUENTEMENU, 70, Color.GOLDENROD, true);
        titulo.setTexto("ENCICLOPEDIA");
        titulo.setPosition(
            (Config.ANCHO - (int)titulo.getAncho()) / 2,
            Config.ALTO - 20
        );

        textoVolver = new Texto(Recursos.FUENTEMENU, 35, Color.WHITE, true);
        textoVolver.setTexto("Presiona ESC para volver");
        textoVolver.setPosition(
            (Config.ANCHO - (int)textoVolver.getAncho()) / 2,
            50
        );

        categorias = Arrays.asList(CategoriaEnciclopedia.values());
        categoriaSeleccionada = 0;
        categoriaActual = categorias.get(0);

        entradasFiltradas = EntradaEnciclopedia.filtrarPorCategoria(categoriaActual);
        entradaSeleccionada = 0;

        textosCategorias = new Texto[categorias.size()];
        for (int i = 0; i < categorias.size(); i++) {
            textosCategorias[i] = new Texto(Recursos.FUENTEMENU, 30, Color.WHITE, false);
            textosCategorias[i].setTexto(categorias.get(i).getNombre());
            textosCategorias[i].setPosition(
                MARGEN,
                Config.ALTO - 100 - (i * 50)
            );
        }

        textoDetalle = new Texto(Recursos.FUENTEMENU, 25, Color.WHITE, false);

        actualizarListaEntradas();

        tiempo = 0;
    }

    private void actualizarFiltro() {
        categoriaActual = categorias.get(categoriaSeleccionada);
        entradasFiltradas = EntradaEnciclopedia.filtrarPorCategoria(categoriaActual);
        entradaSeleccionada = 0;
        actualizarListaEntradas();
    }

    private void actualizarListaEntradas() {
        if (entradasFiltradas == null || entradasFiltradas.isEmpty()) {
            textosEntradas = new Texto[0];
            return;
        }

        int cantidadVisible = Math.min(10, entradasFiltradas.size());
        textosEntradas = new Texto[cantidadVisible];

        for (int i = 0; i < cantidadVisible; i++) {
            textosEntradas[i] = new Texto(Recursos.FUENTEMENU, 28, Color.WHITE, false);

            EntradaEnciclopedia entrada = entradasFiltradas.get(i);
            String nombre = entrada.getNombre();

            int rareza = entrada.getRareza();
            String estrellas = " ";
            for (int j = 0; j < rareza; j++) estrellas += "★";

            textosEntradas[i].setTexto(nombre + estrellas);
            textosEntradas[i].setPosition(
                MARGEN + ANCHO_LISTA_CATEGORIAS + 30,
                Config.ALTO - 100 - (i * 45)
            );
        }
    }

    @Override
    public void render(float delta) {
        Render.limpiarPantalla(0.1f, 0.1f, 0.15f);

        // ✅ Configurar ShapeRenderer con la cámara del viewport
        Render.renderer.setProjectionMatrix(InfiernoPerfecto.camera.combined);

        Render.batch.begin();
        fondo.dibujar();
        titulo.dibujar();
        textoVolver.dibujar();
        Render.batch.end();

        if (textosCategorias != null && textosCategorias.length > 0) {
            for (int i = 0; i < textosCategorias.length; i++) {
                if (i == categoriaSeleccionada) {
                    textosCategorias[i].setColor(categorias.get(i).getColor());
                } else {
                    textosCategorias[i].setColor(Color.GRAY);
                }
            }

            Render.batch.begin();
            for (Texto texto : textosCategorias) {
                texto.dibujar();
            }
            Render.batch.end();

            if (Render.renderer != null) {
                Render.renderer.begin(ShapeRenderer.ShapeType.Line);
                Render.renderer.setColor(Color.GOLDENROD);
                Render.renderer.rect(
                    MARGEN - 5,
                    textosCategorias[categoriaSeleccionada].getY() - 35,
                    ANCHO_LISTA_CATEGORIAS,
                    40
                );
                Render.renderer.end();
            }
        }

        if (textosEntradas != null && textosEntradas.length > 0) {
            for (int i = 0; i < textosEntradas.length; i++) {
                if (i == entradaSeleccionada) {
                    textosEntradas[i].setColor(Color.GOLDENROD);
                } else {
                    textosEntradas[i].setColor(Color.WHITE);
                }
            }

            Render.batch.begin();
            for (Texto texto : textosEntradas) {
                texto.dibujar();
            }
            Render.batch.end();

            if (Render.renderer != null) {
                Render.renderer.begin(ShapeRenderer.ShapeType.Line);
                Render.renderer.setColor(Color.GOLDENROD);
                Render.renderer.rect(
                    MARGEN + ANCHO_LISTA_CATEGORIAS + 25,
                    textosEntradas[entradaSeleccionada].getY() - 32,
                    ANCHO_LISTA_ENTRADAS,
                    38
                );
                Render.renderer.end();
            }

            if (entradasFiltradas != null && !entradasFiltradas.isEmpty()) {
                dibujarPanelDetalle();
            }
        }

        manejarEntrada(delta);
    }

    private void dibujarPanelDetalle() {
        int xPanel = MARGEN + ANCHO_LISTA_CATEGORIAS + ANCHO_LISTA_ENTRADAS + 60;
        int yPanel = Config.ALTO - 95;
        int anchoPanel = Config.ANCHO - xPanel - MARGEN;
        int altoPanel = Config.ALTO - 100;

        if (Render.renderer != null) {
            Render.renderer.begin(ShapeRenderer.ShapeType.Filled);
            Render.renderer.setColor(0, 0, 0, 0.7f);
            Render.renderer.rect(xPanel, yPanel - altoPanel, anchoPanel, altoPanel);
            Render.renderer.end();

            Render.renderer.begin(ShapeRenderer.ShapeType.Line);
            Render.renderer.setColor(Color.GOLDENROD);
            Render.renderer.rect(xPanel, yPanel - altoPanel, anchoPanel, altoPanel);
            Render.renderer.end();
        }

        if (entradaSeleccionada >= 0 && entradaSeleccionada < entradasFiltradas.size()) {
            EntradaEnciclopedia entrada = entradasFiltradas.get(entradaSeleccionada);
            textoDetalle.setTexto(entrada.getTextoCompleto());
            textoDetalle.setPosition(xPanel + 20, yPanel - 00);

            Render.batch.begin();
            textoDetalle.dibujar();
            Render.batch.end();
        }
    }

    private void manejarEntrada(float delta) {
        tiempo += delta;

        // ✅ Coordenadas del mouse ya transformadas por el viewport
        int mouseX = entradas.getMouseX();
        int mouseY = entradas.getMouseY();

        // ✅ DETECCIÓN DE CATEGORÍAS CON EL MOUSE
        int contCategorias = 0;
        for (int i = 0; i < textosCategorias.length; i++) {
            if ((mouseX >= textosCategorias[i].getX()) &&
                (mouseX <= (textosCategorias[i].getX() + textosCategorias[i].getAncho()))) {
                if ((mouseY >= textosCategorias[i].getY() - textosCategorias[i].getAlto()) &&
                    (mouseY <= textosCategorias[i].getY())) {
                    // ✅ Cambiar la categoría seleccionada cuando el mouse pasa por encima
                    if (categoriaSeleccionada != i) {
                        categoriaSeleccionada = i;
                        actualizarFiltro();
                    }
                    contCategorias++;
                }
            }
        }
        mouseClickCategoria = (contCategorias > 0);

        // ✅ DETECCIÓN DE ENTRADAS CON EL MOUSE
        if (textosEntradas != null && textosEntradas.length > 0) {
            int contEntradas = 0;
            for (int i = 0; i < textosEntradas.length; i++) {
                if ((mouseX >= textosEntradas[i].getX()) &&
                    (mouseX <= (textosEntradas[i].getX() + textosEntradas[i].getAncho()))) {
                    if ((mouseY >= textosEntradas[i].getY() - textosEntradas[i].getAlto()) &&
                        (mouseY <= textosEntradas[i].getY())) {
                        // ✅ Cambiar la entrada seleccionada cuando el mouse pasa por encima
                        entradaSeleccionada = i;
                        contEntradas++;
                    }
                }
            }
            mouseClickEntrada = (contEntradas > 0);
        }

        // ✅ CONTROLES DE TECLADO (con cooldown)
        if (tiempo > 0.15f) {
            if (entradas.isArriba()) {
                categoriaSeleccionada--;
                if (categoriaSeleccionada < 0) categoriaSeleccionada = categorias.size() - 1;
                actualizarFiltro();
                tiempo = 0;
            }

            if (entradas.isAbajo()) {
                categoriaSeleccionada++;
                if (categoriaSeleccionada >= categorias.size()) categoriaSeleccionada = 0;
                actualizarFiltro();
                tiempo = 0;
            }

            if (entradas.isDerecha()) {
                entradaSeleccionada++;
                if (entradaSeleccionada >= entradasFiltradas.size()) {
                    entradaSeleccionada = 0;
                }
                tiempo = 0;
            }

            if (entradas.isIzquierda()) {
                entradaSeleccionada--;
                if (entradaSeleccionada < 0) {
                    entradaSeleccionada = entradasFiltradas.size() - 1;
                }
                tiempo = 0;
            }

            if (entradas.isEsc()) {
                GestorPantallas.getInstance().volverAtras();
                tiempo = 0;
            }
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
            try { fondo.dispose(); } catch (Exception e) {}
            fondo = null;
        }

        if (titulo != null) {
            try { titulo.dispose(); } catch (Exception e) {}
            titulo = null;
        }

        if (textoVolver != null) {
            try { textoVolver.dispose(); } catch (Exception e) {}
            textoVolver = null;
        }

        if (textosCategorias != null) {
            for (Texto texto : textosCategorias) {
                if (texto != null) {
                    try { texto.dispose(); } catch (Exception e) {}
                }
            }
            textosCategorias = null;
        }

        if (textosEntradas != null) {
            for (Texto texto : textosEntradas) {
                if (texto != null) {
                    try { texto.dispose(); } catch (Exception e) {}
                }
            }
            textosEntradas = null;
        }

        if (textoDetalle != null) {
            try { textoDetalle.dispose(); } catch (Exception e) {}
            textoDetalle = null;
        }

        shapeRenderer = null;
    }
}
