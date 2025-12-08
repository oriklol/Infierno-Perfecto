package com.dojan.infiernoperfecto.pantallas.niveles.multijugador;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dojan.infiernoperfecto.InfiernoPerfecto;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Musica;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.items.ItemCura;
import com.dojan.infiernoperfecto.red.HiloCliente;
import com.dojan.infiernoperfecto.utiles.*;
import io.Entradas;

public class PantallaTiendaMulti implements Screen {
    private Imagen fondoTienda;
    private Musica musicaFondo;

    private ItemCura[] items = new ItemCura[2];
    private boolean[] itemsComprados = new boolean[2];

    private Texto infoPersonaje[] = new Texto[2];

    private Texto titulo;
    private Texto infoItem;
    private Texto continuarPartida;
    private Texto textoEsperando;

    private float tiempo;
    private int opc = 0;

    Entradas entradas = new Entradas();
    private boolean mouseClick = false;
    private boolean mouseClickContinuar = false;
    
    // MULTIJUGADOR
    private HiloCliente hiloCliente;
    private boolean esperandoOtroJugador = false;
    private boolean inicializado = false;

    @Override
    public void show() {
        if (!inicializado) {
            inicializarRecursos();
            inicializado = true;
        }

        hiloCliente = HiloCliente.getInstanciaActiva();
        if (hiloCliente == null) {
            System.out.println("ERROR: No hay cliente multijugador activo");
        }
        
        // Reset flags
        esperandoOtroJugador = false;
        mouseClick = false;
        mouseClickContinuar = false;
        
        opc = 0;
        tiempo = 0;
        
        Gdx.input.setInputProcessor(entradas);
    }
    
    private void inicializarRecursos() {
        fondoTienda = new Imagen(Recursos.FONDOTIENDA);
        if (Render.renderer == null) {
            Render.renderer = new ShapeRenderer();
        }

        // Generar items (cada cliente ve los suyos, aceptable por ahora)
        for(int i = 0; i < items.length; i++) {
            items[i] = generarItemRandom();
            itemsComprados[i] = false;
        }

        for(int i = 0; i < infoPersonaje.length; i++){
            infoPersonaje[i] = new Texto(Recursos.FUENTEMENU, 50, Color.WHITE, true);

            float anchoTexto = infoPersonaje[i].getAncho();
            int centroX = Config.ANCHO / 3;
            int x = (int)((centroX/2) - anchoTexto);
            int y = (int)(Config.ALTO * 0.8f) - (i * 120);
            infoPersonaje[i].setPosition(x, y);
        }

        titulo = new Texto(Recursos.FUENTEMENU, 70, Color.WHITE, true);
        titulo.setTexto("TIENDA");
        float anchoTitulo = titulo.getAncho();
        int centroX = (int) (Config.ANCHO / 1.5);
        int x = (int)(centroX - anchoTitulo / 2);
        titulo.setPosition(x, (int)(Config.ALTO * 0.85f));

        continuarPartida = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, true);
        continuarPartida.setTexto("Continuar partida");
        float anchoContinuar = continuarPartida.getAncho();
        continuarPartida.setPosition((int)(Config.ANCHO / 1.5 - anchoContinuar / 2), (int)(Config.ALTO * 0.15f));

        infoItem = new Texto(Recursos.FUENTEMENU, 50, Color.GOLDENROD, true);

        int espacioEntreItems = 210;
        centroX = (int) (Config.ANCHO / 1.5);

        items[0].setPosition(centroX - espacioEntreItems, (int) (Config.ALTO/2.5f));
        items[1].setPosition(centroX + espacioEntreItems - items[1].getAncho(), (int) (Config.ALTO/2.5f));
        
        textoEsperando = new Texto(Recursos.FUENTEMENU, 40, Color.YELLOW, true);
        textoEsperando.setTexto("Esperando al otro jugador...");
        textoEsperando.setPosition(
            Config.ANCHO / 2 - (int)(textoEsperando.getAncho() / 2),
            Config.ALTO / 2
        );
    }

    private ItemCura generarItemRandom() {
        int itemRandom = Random.generarEntero(1, 3);
        if(itemRandom==1){
            return new ItemCura(20,40, "Vino medio vacio",Recursos.ITEMCURA1);
        } else if (itemRandom==2) {
            return new ItemCura(35,70, "Pequeño angel bostezando",Recursos.ITEMCURA2);
        } else if (itemRandom==3) {
            return new ItemCura(60,120, "Zanahoria interdimensional",Recursos.ITEMCURA3);
        }
        return null; // Default null safe handle
    }

    @Override
    public void render(float delta) {
        // 0. Sincronización Server -> Cliente
        if (hiloCliente != null && hiloCliente.isConectado()) {
            // Verificar si comenzó el MiniBoss (server manda DATOS_BATALLA)
            if (hiloCliente.hasDatosBatallaActualizados()) {
                System.out.println("PantallaTiendaMulti: Datos de batalla recibidos. Iniciando MiniBoss.");
                // NO consumir datos aquí, los consume PantallaLimboMulti
                GestorPantallas.getInstance().irAPantalla(new PantallaLimboMulti());
                return;
            }
            
            // Actualizar stats desde servidor (por si hubo compra y server validó)
            int numJug = hiloCliente.getNumeroJugador();
             // Las compras locales ya actualizan stats locales, pero el server manda confirmación
             // Podríamos sobrescribir si queremos autoridad total del servidor
        }

        // 1. Renderizado
        Render.renderer.setProjectionMatrix(InfiernoPerfecto.camera.combined);

        Render.batch.begin();
        fondoTienda.dibujar();

        if (waitingState()) {
             textoEsperando.dibujar();
        } else {
            // UI Normal de Tienda
            dibujarInterfaz();
        }

        Render.batch.end();

        // 2. Lógica (Solo si no estamos esperando)
        if (!waitingState()) {
            dibujarSeleccion();
            manejarInput(delta);
        }
    }
    
    private boolean waitingState() {
        return esperandoOtroJugador;
    }
    
    private void dibujarInterfaz() {
        // Info Personaje
        infoPersonaje[0].setTexto("Vida: " + Config.personajeSeleccionado.getVidaActual());
        infoPersonaje[1].setTexto("Monedas: " + Config.personajeSeleccionado.getMonedasActual());
        infoPersonaje[0].dibujar();
        infoPersonaje[1].dibujar();

        // Items
        for(int i = 0; i < items.length; i++) {
            if(!itemsComprados[i]) {
                items[i].dibujar();
            }
        }

        titulo.dibujar();

        // Info Item Seleccionado
        if(opc >= 0 && opc < items.length && !itemsComprados[opc]) {
            ItemCura itemSeleccionado = items[opc];
            String info = itemSeleccionado.getNombre() + " - $" + itemSeleccionado.getPrecio();
            infoItem.setTexto(info);

            float anchoInfo = infoItem.getAncho();
            infoItem.setPosition((int)(Config.ANCHO / 1.5 - anchoInfo / 2), (int)(Config.ALTO / 3.5));

            infoItem.dibujar();
        }

        continuarPartida.dibujar();
    }
    
    private void dibujarSeleccion() {
        if(opc >= 0 && opc < items.length && !itemsComprados[opc]) {
            ItemCura itemSeleccionado = items[opc];

            Render.renderer.begin(ShapeRenderer.ShapeType.Line);
            Render.renderer.setColor(Color.GOLDENROD);

            int margen = 0;
            Render.renderer.rect(
                itemSeleccionado.getX() - margen,
                itemSeleccionado.getY() - margen,
                itemSeleccionado.getAncho() + (int)(margen * 1.2),
                itemSeleccionado.getAlto() + (int)(margen * 1.2)
            );

            Render.renderer.end();
        }
    }

    private void manejarInput(float delta) {
        int mouseX = entradas.getMouseX();
        int mouseY = entradas.getMouseY();

        // Detección Items
        int contItems = 0;
        for (int i = 0; i < items.length; i++) {
            if (!itemsComprados[i]) {
                if ((mouseX >= items[i].getX()) &&
                    (mouseX <= (items[i].getX() + items[i].getAncho())) &&
                    (mouseY >= items[i].getY()) &&
                    (mouseY <= items[i].getY() + items[i].getAlto())) {
                    opc = i;
                    contItems++;
                }
            }
        }
        mouseClick = (contItems > 0);

        // Detección Continuar
        int contContinuar = 0;
        if ((mouseX >= continuarPartida.getX()) &&
            (mouseX <= (continuarPartida.getX() + continuarPartida.getAncho())) &&
            (mouseY >= continuarPartida.getY() - continuarPartida.getAlto()) &&
            (mouseY <= continuarPartida.getY())) {
            opc = 2;
            contContinuar++;
        }
        mouseClickContinuar = (contContinuar > 0);

        // Teclado
        tiempo += delta;
        if(entradas.isDerecha() && tiempo > 0.2f){
            tiempo = 0;
            opc = siguienteOpcionDisponible(opc, true);
        }
        if(entradas.isIzquierda() && tiempo > 0.2f){
            tiempo = 0;
            opc = siguienteOpcionDisponible(opc, false);
        }

        // Color Continuar
        if(opc == 2) continuarPartida.setColor(Color.GOLDENROD);
        else continuarPartida.setColor(Color.WHITE);

        // Acción
        if((entradas.isEnter() || entradas.isClick()) && tiempo > 0.3f) {
            tiempo = 0;

            // Compra Item
            if(opc >= 0 && opc < items.length && !itemsComprados[opc]) {
                if (entradas.isEnter() || (entradas.isClick() && mouseClick)) {
                    // Validar compra localmente primero
                    if (Config.personajeSeleccionado.getMonedasActual() >= items[opc].getPrecio()) {
                        boolean compraExitosa = Config.personajeSeleccionado.comprar(items[opc]);
                        if(compraExitosa) {
                            itemsComprados[opc] = true;
                            // Enviar al servidor: COMPRAR_ITEM:COSTO:VIDA_EXTRA:FE_EXTRA
                            // El item cura en constructor: ItemCura(fe, vida, nombre, ruta)
                            // items[opc].getCura() es vida, items[opc].getFe() es fe. (Hay que ver la clase ItemCura pero asumimos por ahora)
                            // En ItemCura: super(precio...) y atributos.
                            // Asumamos que comprar() ya actualizó localmente.
                            // Ahora notificamos al servidor para que actualice su estado autoritativo y replique.
                            // PERO ItemCura no tiene getters de curación públicos fáciles a veces.
                            // Mejor enviar solo que se compró un item y el nuevo estado del jugador.
                            // O enviar "COMPRAR_ITEM:PRECIO".
                            
                            // Simplificación robusta: Enviar el nuevo estado del jugador al servidor
                            // "ACTUALIZAR_MI_ESTADO:VIDA:FE:MONEDAS" -> Servidor valida y broadcastea.
                            // Pero el protocolo pidió "COMPRAR_ITEM".
                            // Enviemos: COMPRAR_ITEM:PRECIO:VIDA_GANADA:FE_GANADA
                            hiloCliente.enviarMensajeAlServidor("COMPRAR_ITEM:" + items[opc].getPrecio() + ":" + items[opc].getCantidadCura() + ":0");
                            
                            opc = siguienteOpcionDisponible(opc, true);
                        }
                    } else {
                        // Feedback visual de "No alcanza" (opcional)
                        System.out.println("No tienes suficientes monedas");
                    }
                }
            }
            // Continuar
            else if(opc == 2) {
                if (entradas.isEnter() || (entradas.isClick() && mouseClickContinuar)) {
                    esperandoOtroJugador = true;
                    if (hiloCliente != null) {
                        hiloCliente.enviarMensajeAlServidor("SALIR_TIENDA");
                    }
                }
            }
        }
    }

    private int siguienteOpcionDisponible(int opcActual, boolean adelante) {
        int nuevaOpc = opcActual;
        int intentos = 0;
        int maxOpciones = items.length + 1;

        do {
            if(adelante) {
                nuevaOpc++;
                if(nuevaOpc > 2) nuevaOpc = 0;
            } else {
                nuevaOpc--;
                if(nuevaOpc < 0) nuevaOpc = 2;
            }
            intentos++;
            if(intentos > maxOpciones) return 2;
        } while(nuevaOpc < items.length && itemsComprados[nuevaOpc]);

        return nuevaOpc;
    }

    @Override
    public void resize(int width, int height) {}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() { dispose(); }

    @Override
    public void dispose() {
        if (musicaFondo != null) musicaFondo.dispose();
        if (fondoTienda != null) fondoTienda.dispose();
        if (titulo != null) titulo.dispose();
        if (infoItem != null) infoItem.dispose();
        if (continuarPartida != null) continuarPartida.dispose();
        if (textoEsperando != null) textoEsperando.dispose();
        
        for (Texto t : infoPersonaje) if (t != null) t.dispose();
        for (ItemCura i : items) if (i != null) i.dispose();
    }
}
