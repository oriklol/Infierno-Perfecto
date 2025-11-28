package com.dojan.infiernoperfecto.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Musica;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.red.HiloCliente;
import com.dojan.infiernoperfecto.utiles.Config;
import com.dojan.infiernoperfecto.utiles.ControlAudio;
import com.dojan.infiernoperfecto.utiles.GestorPantallas;
import com.dojan.infiernoperfecto.utiles.Recursos;
import static com.dojan.infiernoperfecto.utiles.Render.app;
import static com.dojan.infiernoperfecto.utiles.Render.batch;

import io.Entradas;

public class PantallaMenu implements Screen {
    private Musica musicaFondo;
    private Imagen menu;
    private final Imagen[] pantallasEspera = new Imagen[4];
    final private Texto opciones[] = new Texto[5];
    String textos[] = {
        "Nueva Partida",
        "Multijugador",
        "Opciones",
        "Tutorial",
        "Salir"
    };
    private int opc = 1;
    private boolean mouseClick = false;
    private float tiempo;
    private FitViewport fitViewport;

    // Variables para manejar las pantallas de espera
    private float tiempoEspera = 0f;
    private boolean mostrandoEspera = false;
    private int indiceEspera = 0;
    private float duracionPorPantalla = 1.5f;
    private float tiempoMaximoEspera = 30f;
    private Texto textoEspera;
    private Texto textoCancelar;

    HiloCliente cliente;

    Entradas entradas = new Entradas();

    public PantallaMenu(){
    }

    @Override
    public void show() {
        // DECLARACION DE RECURSOS
        // resetear el cliente al mostrar el menú
        limpiarCliente();

        // viewport para mantener la relacion de aspecto
        fitViewport = new FitViewport(800,600);

        // musica
        musicaFondo = new Musica(Recursos.MUSICAMENU);
        ControlAudio.setMusicaActual(musicaFondo);


        // menu
        menu = new Imagen(Recursos.FONDOMENU);
        menu.setSize(Config.ANCHO, Config.ALTO);

        // pantallas lobby
        pantallasEspera[0] = new Imagen(Recursos.FONDOESPERA1);
        pantallasEspera[1] = new Imagen(Recursos.FONDOESPERA2);
        pantallasEspera[2] = new Imagen(Recursos.FONDOESPERA3);
        pantallasEspera[3] = new Imagen(Recursos.FONDOESPERA4);
        for (int i = 0; i < pantallasEspera.length; i++){
            pantallasEspera[i].setSize(Config.ANCHO, Config.ALTO);
        }

        // entradas
        Gdx.input.setInputProcessor(entradas);

        int avanceY = 30;
        int avanceX = 200;

        for(int i = 0; i<opciones.length;i++){
            opciones[i] = new Texto(Recursos.FUENTEMENU,90,Color.WHITE,true);
            opciones[i].setTexto(textos[i]);
            opciones[i].setPosition((int) ((Config.ANCHO/2)-(opciones[i].getAncho()/2))+avanceX, (int) (((Config.ALTO/1.4f)+(opciones[0].getAlto()/1.2))-(opciones[i].getAlto()+avanceY)*i));
        }

        // Textos para la pantalla de espera
        textoEspera = new Texto(Recursos.FUENTEMENU, 40, Color.WHITE, true);
        textoEspera.setTexto("Esperando jugadores...");
        textoEspera.setPosition((int)(Config.ANCHO/2 - textoEspera.getAncho()/2), 100);

        textoCancelar = new Texto(Recursos.FUENTEMENU, 30, Color.LIGHT_GRAY, true);
        textoCancelar.setTexto("Presiona ESC para cancelar");
        textoCancelar.setPosition((int)(Config.ANCHO/2 - textoCancelar.getAncho()/2), 50);

        GestorPantallas.getInstance().limpiarHistorial();
    }

    @Override
    public void render(float delta) {

        ControlAudio.reproducirMusica();

        // Si estamos mostrando pantallas de espera
        if (mostrandoEspera) {
            renderPantallasEspera(delta);
            return; // No renderizar el menú
        }

        // Renderizado normal del menú
        batch.begin();
        menu.dibujar();
        for(int i = 0; i<opciones.length;i++){
            opciones[i].dibujar();
        }
        batch.end();

        tiempo+= delta;

        // Navegación con teclado
        if(entradas.isAbajo()){
            if(tiempo>0.15f){
                tiempo=0;
                opc++;
                if(opc>4){
                    opc=1;
                }
            }
        }

        if(entradas.isArriba()){
            if(tiempo>0.1f){
                tiempo=0;
                opc--;
                if(opc<1){
                    opc=4;
                }
            }
        }

        // Detección de mouse
        int cont = 0;
        for(int i = 0; i<opciones.length;i++){
            if ((entradas.getMouseX()>=opciones[i].getX())&&(entradas.getMouseX()<=(opciones[i].getX()+opciones[i].getAncho()))){
                if((((entradas.getMouseY()>=opciones[i].getY()-opciones[i].getAlto())&&(entradas.getMouseY()<=(opciones[i].getY()))))){
                    opc=i+1;
                    cont++;
                }
            }
        }
        mouseClick = cont > 0;

        // Colorear opción seleccionada
        for(int i=0; i<opciones.length;i++){
            if(i==(opc-1)){
                opciones[i].setColor(Color.GOLDENROD);
            }else{
                opciones[i].setColor(Color.WHITE);
            }
        }

        // Manejo de selección
        if(entradas.isEnter() || entradas.isClick()){
            if(((opc==1)&&(entradas.isEnter())) || ((opc==1)&&(entradas.isClick())&&(mouseClick))){
                // iniciar juego un jugador
                app.setScreen(new PantallaHistoria());
                ControlAudio.pararMusica();
            }else if(((opc==2)&&(entradas.isEnter())) || ((opc==2)&&(entradas.isClick())&&(mouseClick))){
                // Iniciar pantallas de espera para juego multijugador
                mostrandoEspera = true;
                tiempoEspera = 0f;
                indiceEspera = 0;

                // Limpiar cliente anterior si existe
                limpiarCliente();

                // Crear cliente NUEVO
                cliente = new HiloCliente();
                cliente.start();
                app.setCliente(cliente);
                System.out.println("PantallaMenu: Nuevo cliente creado y conectando...");
            }else if(((opc==3)&&(entradas.isEnter())) || ((opc==3)&&(entradas.isClick())&&(mouseClick))){
                // entra en opciones
                app.setScreen(new PantallaOpciones());
            }else if(((opc==4)&&(entradas.isEnter())) || ((opc==4)&&(entradas.isClick())&&(mouseClick))){
                // entra en tutorial
                app.setScreen(new PantallaTutorial());
                ControlAudio.pararMusica();
            }
            else if(((opc==5)&&(entradas.isEnter())) || ((opc==5)&&(entradas.isClick())&&(mouseClick))){
                // salir del juego
                Gdx.app.exit();
            }
        }
    }

    private void renderPantallasEspera(float delta) {
        tiempoEspera += delta;

        // Cambiar de pantalla cada duracionPorPantalla segundos
        indiceEspera = (int)(tiempoEspera / duracionPorPantalla) % pantallasEspera.length;

        batch.begin();
        pantallasEspera[indiceEspera].dibujar();

        // Mostrar texto de espera
        textoEspera.dibujar();
        textoCancelar.dibujar();

        // Mostrar tiempo restante
        int tiempoRestante = (int)(tiempoMaximoEspera - tiempoEspera);
        if (tiempoRestante > 0) {
            textoEspera.setTexto("Esperando jugadores... (" + tiempoRestante + "s)");
            textoEspera.setPosition((int)(Config.ANCHO/2 - textoEspera.getAncho()/2), 100);
        }
        batch.end();

        // ✅ Permitir cancelar con ESC (CORREGIDO)
        if (entradas.isEsc()) {
            cancelarEspera(); // Ya maneja la desconexión internamente
            return;
        }

        // Verificar si hay dos jugadores conectados
        if (Config.empiezaPartida || verificarDosJugadoresConectados()) {
            ControlAudio.pararMusica();
            app.setScreen(new PantallaHistoria());
            return;
        }

        // Timeout: volver al menú si se acaba el tiempo
        if (tiempoEspera >= tiempoMaximoEspera) {
            cancelarEspera();
            System.out.println("Tiempo de espera agotado. No se encontraron jugadores.");
        }
    }

    private void cancelarEspera() {
        mostrandoEspera = false;
        tiempoEspera = 0f;
        indiceEspera = 0;

        // ✅ Desconectar y limpiar el cliente
        limpiarCliente();

        Config.empiezaPartida = false;
    }

    // ✅ MÉTODO CENTRALIZADO para limpiar el cliente
    private void limpiarCliente() {
        // Limpiar cliente local
        if (cliente != null) {
            try {
                cliente.desconectar();
                System.out.println("Cliente local desconectado y limpiado");
            } catch (Exception e) {
                System.out.println("Error al desconectar cliente local: " + e.getMessage());
            }
            cliente = null;
        }

        // Limpiar cliente de la aplicación global
        HiloCliente clienteGlobal = app.getCliente();
        if (clienteGlobal != null) {
            try {
                clienteGlobal.desconectar();
                System.out.println("Cliente global desconectado y limpiado");
            } catch (Exception e) {
                System.out.println("Error al desconectar cliente global: " + e.getMessage());
            }
            app.setCliente(null);
        }

        // Resetear configuración
        Config.empiezaPartida = false;

        System.out.println("✅ Información del cliente completamente reseteada");
    }

    private boolean verificarDosJugadoresConectados() {
        return false; // Por ahora retorna false
    }

    @Override
    public void resize(int width, int height) {
        //se maneja en infiernoperfecto
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
        // ✅ Limpiar cliente al destruir la pantalla
        limpiarCliente();

        if (musicaFondo != null) {
            try{ musicaFondo.dispose(); }catch(Exception e){}
            musicaFondo = null;
        }

        if (menu != null) {
            try{ menu.dispose(); }catch(Exception e){}
            menu = null;
        }

        if (pantallasEspera != null) {
            for(int i=0; i<pantallasEspera.length; i++){
                if (pantallasEspera[i] != null){
                    try{ pantallasEspera[i].dispose(); }catch(Exception e){}
                }
            }
        }

        if (opciones != null) {
            for(int i=0;i<opciones.length;i++){
                if (opciones[i] != null){
                    try{ opciones[i].dispose(); }catch(Exception e){}
                    opciones[i] = null;
                }
            }
        }

        if (textoEspera != null) {
            try{ textoEspera.dispose(); }catch(Exception e){}
            textoEspera = null;
        }

        if (textoCancelar != null) {
            try{ textoCancelar.dispose(); }catch(Exception e){}
            textoCancelar = null;
        }
    }

    public void setTiempo(float tiempo) {
        this.tiempo = tiempo;
    }
}
