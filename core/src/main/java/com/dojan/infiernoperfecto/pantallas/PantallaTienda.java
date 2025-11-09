package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Musica;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.items.ItemCura;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.ControladorJuego;
import com.dojan.infiernoperfecto.utiles.Random;
import com.dojan.infiernoperfecto.utiles.Recursos;
import com.dojan.infiernoperfecto.utiles.Render;
import static com.dojan.infiernoperfecto.utiles.Render.renderer;

import io.Entradas;

public class PantallaTienda implements Screen {
    private Imagen fondoTienda;
    private Musica musicaFondo;

    private ItemCura[] items = new ItemCura[2];
    private boolean[] itemsComprados = new boolean[2]; // Track de items comprados

    private Texto infoPersonaje[] = new Texto[2];

    private Texto titulo;
    private Texto infoItem;
    private Texto continuarPartida;

    private float tiempo;
    private int opc = 0;

    Entradas entradas = new Entradas();
    private boolean mouseClick = false;

    @Override
    public void show() {
        fondoTienda = new Imagen(Recursos.FONDOTIENDA);
        if (Render.renderer == null) {
            Render.renderer = new ShapeRenderer();
        }

        Gdx.input.setInputProcessor(entradas);

        // ← RESETEAR items y estado de compra cada vez que se muestra la tienda
        for(int i = 0; i < items.length; i++) {
            items[i] = generarItemRandom();
            itemsComprados[i] = false;  // ← Resetear a false
        }

        // ← Resetear la opción seleccionada
        opc = 0;

        // Info del personaje
        for(int i = 0; i < infoPersonaje.length; i++){
            infoPersonaje[i] = new Texto(Recursos.FUENTEMENU, 50, Color.WHITE, true);

            float anchoTexto = infoPersonaje[i].getAncho();
            int centroX = Config.ANCHO / 3;
            int x = (int)(centroX - anchoTexto);
            int y = (int)(Config.ALTO * 0.8f) - (i * 120);
            infoPersonaje[i].setPosition(x, y);
        }

        // Título
        titulo = new Texto(Recursos.FUENTEMENU, 70, Color.WHITE, true);
        titulo.setTexto("TIENDA");
        float anchoTitulo = titulo.getAncho();
        int centroX = (int) (Config.ANCHO / 1.5);
        int x = (int)(centroX - anchoTitulo / 2);
        titulo.setPosition(x, (int)(Config.ALTO * 0.85f));

        // Continuar partida
        continuarPartida = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, true);
        continuarPartida.setTexto("Continuar partida");
        float anchoContinuar = continuarPartida.getAncho();
        continuarPartida.setPosition((int)(Config.ANCHO / 1.5 - anchoContinuar / 2), (int)(Config.ALTO * 0.15f));

        // Info item
        infoItem = new Texto(Recursos.FUENTEMENU, 50, Color.GOLDENROD, true);

        // Posición items
        int espacioEntreItems = 210;
        centroX = (int) (Config.ANCHO / 1.5);

        items[0].setPosition(centroX - espacioEntreItems, (int) (Config.ALTO/2.5f));
        items[1].setPosition(centroX + espacioEntreItems - items[1].getAncho(), (int) (Config.ALTO/2.5f));
    }

    private ItemCura generarItemRandom() {
        int itemRandom = Random.generarEntero(1, 3);
        if(itemRandom==1){
            return new ItemCura(20,30, "Vino medio vacio",Recursos.ITEMCURA1);
        } else if (itemRandom==2) {
            return new ItemCura(30,40, "Pequeño angel bostezando",Recursos.ITEMCURA2);
        } else if (itemRandom==3) {
            return new ItemCura(50,70, "Zanahoria interdimensional",Recursos.ITEMCURA3);
        }
        return null;
    }

    @Override
    public void render(float delta) {
        Render.batch.begin();
        fondoTienda.dibujar();

        // Actualizar y dibujar info del personaje
        for (int i = 0; i < infoPersonaje.length; i++){
            if(i == 0){
                infoPersonaje[i].setTexto("Vida: " + Config.personajeSeleccionado.getVidaActual());
            } else if (i == 1) {
                infoPersonaje[i].setTexto("Monedas: " + Config.personajeSeleccionado.getMonedasActual());
            }
            infoPersonaje[i].dibujar();
        }

        // Dibujar solo items NO comprados
        for(int i = 0; i < items.length; i++) {
            if(!itemsComprados[i]) {
                items[i].dibujar();
            }
        }

        titulo.dibujar();

        // Dibujar info del item seleccionado (solo si no está comprado)
        if(opc >= 0 && opc < items.length && !itemsComprados[opc]) {
            ItemCura itemSeleccionado = items[opc];
            String info = itemSeleccionado.getNombre() + " - $" + itemSeleccionado.getPrecio();
            infoItem.setTexto(info);

            float anchoInfo = infoItem.getAncho();
            infoItem.setPosition((int)(Config.ANCHO / 1.5 - anchoInfo / 2), (int)(Config.ALTO / 3.5));

            infoItem.dibujar();
        }

        continuarPartida.dibujar();

        Render.batch.end();

        // Dibujar cuadrado de selección (solo si el item no está comprado)
        if(opc >= 0 && opc < items.length && !itemsComprados[opc]) {
            ItemCura itemSeleccionado = items[opc];

            renderer.begin(ShapeRenderer.ShapeType.Line);
            renderer.setColor(Color.GOLDENROD);

            int margen = 0;
            renderer.rect(
                itemSeleccionado.getX() - margen,
                itemSeleccionado.getY() - margen,
                itemSeleccionado.getAncho() + (int)(margen * 1.2),
                itemSeleccionado.getAlto() + (int)(margen * 1.2)
            );

            renderer.end();
        }

        // Navegación
        tiempo += delta;

        if(entradas.isDerecha()){
            if(tiempo > 0.2f){
                tiempo = 0;
                opc = siguienteOpcionDisponible(opc, true);
            }
        }

        if(entradas.isIzquierda()){
            if(tiempo > 0.2f){
                tiempo = 0;
                opc = siguienteOpcionDisponible(opc, false);
            }
        }

        // Cambiar color según selección
        if(opc == 2){
            continuarPartida.setColor(Color.GOLDENROD);
        } else {
            continuarPartida.setColor(Color.WHITE);
        }


        if((entradas.isEnter() || entradas.isClick()) && tiempo > 0.3f) {
            tiempo = 0; // Resetear timer

            if(opc >= 0 && opc < items.length && !itemsComprados[opc]) {
                // Comprar item
                boolean compraExitosa = Config.personajeSeleccionado.comprar(items[opc]);

                if(compraExitosa) {
                    itemsComprados[opc] = true; // Marcar como comprado

                    // Mover a la siguiente opción disponible
                    opc = siguienteOpcionDisponible(opc, true);
                }
            } else if(opc == 2) {
                ControladorJuego.getInstance().avanzarNivel();
            }
        }
    }

    /**
     * Encuentra la siguiente opción disponible (que no esté comprada)
     * @param opcActual opción actual
     * @param adelante true para avanzar, false para retroceder
     * @return índice de la siguiente opción disponible
     */
    private int siguienteOpcionDisponible(int opcActual, boolean adelante) {
        int nuevaOpc = opcActual;
        int intentos = 0;
        int maxOpciones = items.length + 1; // items + continuar

        do {
            if(adelante) {
                nuevaOpc++;
                if(nuevaOpc > 2) nuevaOpc = 0;
            } else {
                nuevaOpc--;
                if(nuevaOpc < 0) nuevaOpc = 2;
            }

            intentos++;

            // Evitar loop infinito si todas las opciones están compradas
            if(intentos > maxOpciones) {
                return 2; // Ir a "Continuar partida"
            }

        } while(nuevaOpc < items.length && itemsComprados[nuevaOpc]);

        return nuevaOpc;
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
        if (musicaFondo != null){
            try{ musicaFondo.dispose(); }catch(Exception e){}
            musicaFondo = null;
        }

        if (fondoTienda != null){
            try{ fondoTienda.dispose(); }catch(Exception e){}
            fondoTienda = null;
        }

        if (titulo != null){
            try{ titulo.dispose(); }catch(Exception e){}
            titulo = null;
        }

        if (infoItem != null){
            try{ infoItem.dispose(); }catch(Exception e){}
            infoItem = null;
        }

        if (continuarPartida != null){
            try{ continuarPartida.dispose(); }catch(Exception e){}
            continuarPartida = null;
        }

        if (infoPersonaje != null){
            for (int i = 0; i < infoPersonaje.length; i++){
                if (infoPersonaje[i] != null){
                    try{ infoPersonaje[i].dispose(); }catch(Exception e){}
                    infoPersonaje[i] = null;
                }
            }
        }

        if (items != null){
            for (int i = 0; i < items.length; i++){
                if (items[i] != null){
                    try{ items[i].dispose(); }catch(Exception e){}
                    items[i] = null;
                }
            }
        }

        // No se debe disponer del renderer global aquí (lo gestiona la aplicación central).
    }
}
