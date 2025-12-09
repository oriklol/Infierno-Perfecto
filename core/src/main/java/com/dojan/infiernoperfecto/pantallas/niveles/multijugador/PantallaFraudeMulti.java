package com.dojan.infiernoperfecto.pantallas.niveles.multijugador;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.dojan.infiernoperfecto.InfiernoPerfecto;
import com.dojan.infiernoperfecto.ataques.Ataque;
import com.dojan.infiernoperfecto.elementos.Imagen;
import com.dojan.infiernoperfecto.elementos.Musica;
import com.dojan.infiernoperfecto.elementos.Texto;
import com.dojan.infiernoperfecto.pantallas.PantallaOpciones;
import com.dojan.infiernoperfecto.pantallas.enciclopedia.PantallaEnciclopedia;
import com.dojan.infiernoperfecto.pantallas.niveles.multijugador.PantallaLimboMulti;
import com.dojan.infiernoperfecto.red.HiloCliente;
import com.dojan.infiernoperfecto.utiles.*;
import com.dojan.infiernoperfecto.entidades.Jugador;
import com.dojan.infiernoperfecto.entidades.clases.Peleador;
import io.Entradas;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de Batalla Multijugador - Fraude (Piso 2)
 */
public class PantallaFraudeMulti implements Screen {

    // ============================================================
    // RECURSOS VISUALES
    // ============================================================
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
    private Texto textoVictoria;

    // UI Info Aliado
    private Texto infoAliadoNombre;
    private Texto infoAliadoVida;
    private Texto infoAliadoFe;

    // Variables de UI reutilizables
    private Texto textoSeleccionarEnemigo;
    private Texto textoEsperandoReusable;
    private Texto textoContinuarReusable;

    // Variables para control de input seguro
    private boolean clickPrevio = false;
    private boolean justClicked = false;
    private boolean disposed = false;

    // ============================================================
    // CONTROL Y ESTADO MULTIJUGADOR
    // ============================================================
    private HiloCliente hiloCliente;
    private int numeroJugador = 0;

    Entradas entradas = new Entradas();
    private float tiempo;
    private int opc = 0;

    private boolean esperandoInput = false;
    private EstadoBatallaMulti estadoActual = EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA;

    // ============================================================
    // DATOS DE ENEMIGOS (desde servidor)
    // ============================================================
    private String[] nombresEnemigos;
    private float[] vidasEnemigos;
    private float[] vidasMaximasEnemigos;
    private boolean[] enemigosMuertos;

    // Recursos gráficos persistentes
    private final ArrayList<Imagen> enemigoSpr = new ArrayList<>();
    private final ArrayList<Texto> textosNombresEnemigos = new ArrayList<>();
    private final ArrayList<Texto> textosVidasEnemigos = new ArrayList<>();

    private int enemigoSeleccionado = 0;
    private boolean inicializado = false;

    // ============================================================
    // ATAQUES DEL JUGADOR
    // ============================================================
    private Texto[] textoAtaques;
    private int ataqueSeleccionado = 0;

    private boolean esperandoEsc = false;
    private String logBatalla = "";


    @Override
    public void show() {
        System.out.println("PantallaFraudeMulti.show() ejecutado");

        musicaFondo = new Musica(Recursos.MUSICABATALLA);
        ControladorAudio.setMusicaActual(musicaFondo);

        if (!inicializado) {
            inicializarRecursos();
            inicializado = true;
        } else {
            if (Render.renderer == null) {
                Render.renderer = new ShapeRenderer();
            }
            Gdx.input.setInputProcessor(entradas);
        }

        // MULTIJUGADOR: Reutilizar HiloCliente existente
        hiloCliente = HiloCliente.getInstanciaActiva();

        if (hiloCliente == null || !hiloCliente.isConectado()) {
            System.out.println("PantallaFraudeMulti: ERROR - No hay cliente conectado");
            estadoActual = EstadoBatallaMulti.ESPERANDO_CONEXION;
        } else {
            System.out.println("PantallaFraudeMulti: Cliente conectado, esperando datos de batalla");
            estadoActual = EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA;
        }

        esperandoEsc = false;
        esperandoInput = false;
        tiempo = 0;

        entradas = new Entradas();
        Gdx.input.setInputProcessor(entradas);

        if (Config.personajeSeleccionado == null) {
            System.out.println("PantallaFraudeMulti: WARN - PersonajeSeleccionado es NULL. Creando Peleador por defecto.");
            String nombreJugador = "Jugador " + (hiloCliente != null ? hiloCliente.getNumeroJugador() : "?");
            Config.personajeSeleccionado = new Jugador(nombreJugador, new Peleador());
        }

        logBatalla = "";
        if (logTexto == null) {
            logTexto = new Texto(Recursos.FUENTEMENU, 35, Color.WHITE, false);
        }
    }

    private void inicializarRecursos() {
        Gdx.input.setInputProcessor(entradas);
        if (Render.renderer == null) {
            Render.renderer = new ShapeRenderer();
        }

        // --- CAMBIO PRINCIPAL: FONDO DE FRAUDE ---
        fondo = new Imagen(Recursos.FONDOFRAUDE);
        arena = new Imagen(Recursos.FONDOARENA);
        danioSpr = new Imagen(Recursos.EFECTODANIO);

        lugar = new Texto(Recursos.FUENTEMENU, 60, Color.BLACK, false);
        lugar.setPosition((int) (Config.ANCHO / 1.2f), (int) (Config.ALTO / 1.1f));
        lugar.setTexto(Config.nivel + " - " + Config.piso);

        textoEnemigoSeleccionado = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, false);
        vidaEnemigoTexto = new Texto(Recursos.FUENTEMENU, 30, Color.WHITE, false);
        textoPS = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
        textoFe = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
        textoUsos = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
        textoCostoFe = new Texto(Recursos.FUENTEMENU, 32, Color.CORAL, false);
        logTexto = new Texto(Recursos.FUENTEMENU, 35, Color.WHITE, false);

        textoSeleccionarEnemigo = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, false);
        textoSeleccionarEnemigo.setTexto("Selecciona a un enemigo");
        textoSeleccionarEnemigo.setPosition((Config.ANCHO / 2 - ((int) textoSeleccionarEnemigo.getAncho() / 2)), 120);

        textoEsperandoReusable = new Texto(Recursos.FUENTEMENU, 50, Color.YELLOW, false);
        textoEsperandoReusable.setTexto("Esperando al otro jugador...");
        textoEsperandoReusable.setPosition(Config.ANCHO / 2 - (int)(textoEsperandoReusable.getAncho() / 2), 150);

        textoContinuarReusable = new Texto(Recursos.FUENTEMENU, 30, Color.YELLOW, false);
        textoContinuarReusable.setTexto("Click o Enter para continuar");
        textoContinuarReusable.setPosition(50, 100);

        textoVictoria = new Texto(Recursos.FUENTEMENU, 80, Color.GOLD, false);
        textoVictoria.setTexto("¡VICTORIA!");
        textoVictoria.setPosition(
            Config.ANCHO / 2 - (int)(textoVictoria.getAncho() / 2),
            Config.ALTO / 2 + 50
        );

        // Info Aliado
        infoAliadoNombre = new Texto(Recursos.FUENTEMENU, 30, Color.CYAN, false);
        infoAliadoVida = new Texto(Recursos.FUENTEMENU, 24, Color.WHITE, false);
        infoAliadoFe = new Texto(Recursos.FUENTEMENU, 24, Color.YELLOW, false);
    }

    private void parsearDatosBatalla(String datos) {
        System.out.println("PantallaFraudeMulti: parsearDatosBatalla invocada con: " + datos);

        if(hiloCliente != null) hiloCliente.resetBatallaFlags();

        String[] partes = datos.split(",");

        try {
            int nivelRecibido = Integer.parseInt(partes[0]);
            // if (nivelRecibido < Config.nivel) return; // REMOVIDO: Bloqueaba el cambio de piso (nivel 4 -> nivel 1)
            Config.nivel = nivelRecibido;
        } catch (NumberFormatException e) {
            System.out.println("Error parseando nivel.");
        }

        if (lugar != null) {
            lugar.setTexto(Config.piso + " - " + Config.nivel);
        }

        int numEnemigos = (partes.length - 1) / 2;

        nombresEnemigos = new String[numEnemigos];
        vidasEnemigos = new float[numEnemigos];
        vidasMaximasEnemigos = new float[numEnemigos];
        enemigosMuertos = new boolean[numEnemigos];

        for (Imagen spr : enemigoSpr) if (spr != null) spr.dispose();
        enemigoSpr.clear();

        for (Texto t : textosNombresEnemigos) t.dispose();
        textosNombresEnemigos.clear();

        for (Texto t : textosVidasEnemigos) t.dispose();
        textosVidasEnemigos.clear();

        for (int i = 0; i < numEnemigos; i++) {
            int indexDatos = 1 + (i * 2);
            nombresEnemigos[i] = partes[indexDatos];
            vidasEnemigos[i] = Float.parseFloat(partes[indexDatos + 1]);
            vidasMaximasEnemigos[i] = vidasEnemigos[i];

            Imagen nuevoSpr = crearSpriteEnemigo(nombresEnemigos[i]);

            int posX;
            if (numEnemigos == 1) {
                posX = Config.ANCHO / 2 - (int)(nuevoSpr.getAncho() / 2);
            } else {
                posX = (int) ((Config.ANCHO / 3.5f) * i) + 20;
            }
            nuevoSpr.setPosition(posX, Config.ALTO / 2);
            enemigoSpr.add(nuevoSpr);

            Texto tNombre = new Texto(Recursos.FUENTEMENU, 24, Color.CYAN, false);
            tNombre.setTexto(nombresEnemigos[i]);
            tNombre.setPosition(
                (int) (nuevoSpr.getX() + nuevoSpr.getAncho() / 2 - tNombre.getAncho() / 2),
                (int) (nuevoSpr.getY() + nuevoSpr.getAlto() + 10)
            );
            textosNombresEnemigos.add(tNombre);

            Texto tVida = new Texto(Recursos.FUENTEMENU, 30, Color.WHITE, false);
            tVida.setTexto("HP: " + (int)vidasEnemigos[i]);
            tVida.setPosition(
                (int) (nuevoSpr.getX() + nuevoSpr.getAncho() / 2 - tVida.getAncho() / 2),
                (int) (nuevoSpr.getY() - 20)
            );
            textosVidasEnemigos.add(tVida);
        }

        if (Config.personajeSeleccionado != null) {
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
    }

    private Imagen crearSpriteEnemigo(String nombre) {
        String recurso = Recursos.ENEMIGOFRAUDE1; // Default: Bebon

        if (nombre.equalsIgnoreCase("Bebon")) {
            recurso = Recursos.ENEMIGOFRAUDE1;
        } else if (nombre.equalsIgnoreCase("CangreSaura")) {
            recurso = Recursos.ENEMIGOFRAUDE2;
        } else if (nombre.equalsIgnoreCase("SinRostro")) {
            recurso = Recursos.MINIBOSSFRAUDE;
        }

        System.out.println("DEBUG SPRITE FRAUDE: '" + nombre + "' -> " + recurso);
        return new Imagen(recurso);
    }

    @Override
    public void render(float delta) {
        ControladorAudio.reproducirMusica();
        if (disposed) return;

        Render.limpiarPantalla(0, 0, 0);

        boolean clickActual = entradas.isClick();
        justClicked = clickActual && !clickPrevio;
        clickPrevio = clickActual;

        Render.renderer.setProjectionMatrix(InfiernoPerfecto.camera.combined);

        if (actualizarEstadoDesdeServidor()) {
            return;
        }

        Render.batch.begin();
        fondo.dibujar();
        arena.dibujar();
        lugar.dibujar();
        dibujarInfoAliado();
        Render.batch.end();

        try {
           dibujarEnemigos();
        } catch(Exception e) {
           System.out.println("Error dibujando enemigos: " + e.getMessage());
        }

        if (!esperandoEsc && entradas.isEnciclopedia()) {
            GestorPantallas.getInstance().irAPantalla(new PantallaEnciclopedia());
            esperandoEsc = true;
            return;
        }

        if (!esperandoEsc && entradas.isEsc()) {
            GestorPantallas.getInstance().irAPantalla(new PantallaOpciones(true));
            esperandoEsc = true;
            return;
        }

        if (!entradas.isEsc() && !entradas.isEnciclopedia()) {
            esperandoEsc = false;
        }

        tiempo += delta;

        if (null != estadoActual) switch (estadoActual) {
            case ESPERANDO_DATOS_BATALLA:
                Render.batch.begin();
                Texto esperando = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, false);
                esperando.setTexto("Esperando datos de batalla...");
                esperando.setPosition(Config.ANCHO / 2 - (int)(esperando.getAncho() / 2), Config.ALTO / 2);
                esperando.dibujar();
                Render.batch.end();
                break;

            case SELECCION_ENEMIGO:
                manejarSeleccionEnemigo();
                break;

            case SELECCION_ATAQUE:
                manejarSeleccionAtaque();
                break;

            case ESPERANDO_OTRO_JUGADOR:
                Render.batch.begin();
                if (textoEsperandoReusable != null) {
                    textoEsperandoReusable.dibujar();
                }

                if (!logBatalla.isEmpty()) {
                    logTexto.setTexto(logBatalla);
                    logTexto.setPosition(50, 100);
                    logTexto.dibujar();
                }
                Render.batch.end();
                break;

            case RESULTADOS_COMBATE:
                Render.batch.begin();

                if (logBatalla != null && !logBatalla.isEmpty()) {
                    logTexto.setTexto(logBatalla);
                    logTexto.setPosition(50, Config.ALTO / 2); // Log location adjustment if needed
                    logTexto.dibujar();
                }

                if ((int)(tiempo * 2) % 2 == 0) {
                    if (textoContinuarReusable != null) {
                        textoContinuarReusable.dibujar();
                    }
                }

                Render.batch.end();

                if (entradas.isEnter() || justClicked) {
                    logBatalla = "";
                    hiloCliente.limpiarLogBatalla();
                    hiloCliente.enviarMensajeAlServidor("CONFIRMAR_LOG");
                    estadoActual = EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR;
                    tiempo = 0;
                }
                break;

            case FIN_BATALLA:
                Render.batch.begin();
                if (textoVictoria != null) {
                    textoVictoria.dibujar();
                }
                Render.batch.end();

                if (tiempo > 3.0f) {
                    if (hiloCliente.isIrATienda()) {
                        ControladorJuego.getInstance().irATienda();
                    } else {
                        hiloCliente.resetDatosBatalla();
                        hiloCliente.resetBatallaFlags();
                        hiloCliente.enviarMensajeAlServidor("LISTO_SIGUIENTE_NIVEL");
                        estadoActual = EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA;
                    }
                    if (hiloCliente.isBatallaTerminada()) {
                         hiloCliente.setBatallaTerminada(false);
                    }
                    tiempo = 0;
                }
                break;
        }
    }

    private boolean actualizarEstadoDesdeServidor() {
        if (hiloCliente == null) return false;

        if (numeroJugador == 0 && hiloCliente.getNumeroJugador() > 0) {
            numeroJugador = hiloCliente.getNumeroJugador();
        }

        // Detectar cambio de piso
        if (hiloCliente.getPiso() > Config.piso && hiloCliente.getPiso() != 0) {
            System.out.println("PantallaFraudeMulti: Detectado cambio de piso a " + hiloCliente.getPiso());
            Config.piso = hiloCliente.getPiso();
            ControladorJuego.getInstance().cargarNivel(); // Cambiar pantalla
            return true;
        }

        if (hiloCliente.hasDatosBatallaActualizados() &&
            estadoActual == EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA) {

            String datos = hiloCliente.getDatosBatalla();
            if (datos != null && !datos.isEmpty()) {
                parsearDatosBatalla(datos);
                hiloCliente.consumirDatosBatalla();
                estadoActual = EstadoBatallaMulti.SELECCION_ENEMIGO;
            }
            return false;
        }

        String nuevoLog = hiloCliente.getLogBatalla();
        if (nuevoLog != null && !nuevoLog.isEmpty()) {
            logBatalla = nuevoLog;
            if (estadoActual != EstadoBatallaMulti.RESULTADOS_COMBATE &&
                estadoActual != EstadoBatallaMulti.FIN_BATALLA &&
                estadoActual != EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA) {
                estadoActual = EstadoBatallaMulti.RESULTADOS_COMBATE;
                tiempo = 0;
                esperandoInput = false;
                return false;
            }
        }

        if (hiloCliente.isBatallaTerminada() && estadoActual != EstadoBatallaMulti.FIN_BATALLA) {
            estadoActual = EstadoBatallaMulti.FIN_BATALLA;
            tiempo = 0;
            return false;
        }

        if (vidasEnemigos != null && hiloCliente.getNumEnemigos() > 0) {
            float[] vidasActualizadas = hiloCliente.getVidasEnemigos();
            for (int i = 0; i < Math.min(vidasEnemigos.length, vidasActualizadas.length); i++) {
                if (vidasEnemigos[i] != vidasActualizadas[i]) {
                     vidasEnemigos[i] = vidasActualizadas[i];
                     if (i < textosVidasEnemigos.size()) {
                         textosVidasEnemigos.get(i).setTexto("HP: " + (int)vidasEnemigos[i]);
                     }
                }
            }
        }

        if (numeroJugador == 1) {
            if (hiloCliente.getVidaJugador1() != -1) {
                Config.personajeSeleccionado.setVidaActual(hiloCliente.getVidaJugador1());
                Config.personajeSeleccionado.setFeActual(hiloCliente.getFeJugador1());
                if (hiloCliente.getMonedasJugador1() != -1) {
                    Config.personajeSeleccionado.setMonedasActual(hiloCliente.getMonedasJugador1());
                }
            }
        } else if (numeroJugador == 2) {
            if (hiloCliente.getVidaJugador2() != -1) {
                Config.personajeSeleccionado.setVidaActual(hiloCliente.getVidaJugador2());
                Config.personajeSeleccionado.setFeActual(hiloCliente.getFeJugador2());
                if (hiloCliente.getMonedasJugador2() != -1) {
                    Config.personajeSeleccionado.setMonedasActual(hiloCliente.getMonedasJugador2());
                }
            }
        }

        if (hiloCliente.isEsMiTurno() && estadoActual == EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR) {
            estadoActual = EstadoBatallaMulti.SELECCION_ENEMIGO;
        }

        if (hiloCliente.isIrATienda()) {
            hiloCliente.setIrATienda(false);
            GestorPantallas.getInstance().irAPantalla(new PantallaTiendaMulti());
            return true;
        }

        if (hiloCliente.isEsperandoOtroJugador() &&
            estadoActual != EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR &&
            estadoActual != EstadoBatallaMulti.FIN_BATALLA &&
            estadoActual != EstadoBatallaMulti.RESULTADOS_COMBATE &&
            estadoActual != EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA) {
            estadoActual = EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR;
        }

        return false;
    }

    private void dibujarEnemigos() {
        if (enemigoSpr.isEmpty()) return;

        boolean[] enemigosMuertos = hiloCliente != null ? hiloCliente.getEnemigosMuertos() : new boolean[0];

        Render.batch.begin();
        for (int i = 0; i < enemigoSpr.size(); i++) {
            if (i < enemigosMuertos.length && enemigosMuertos[i]) {
                continue;
            }

            enemigoSpr.get(i).dibujar();

            if (i < textosVidasEnemigos.size()) {
                textosVidasEnemigos.get(i).dibujar();
            }

            if (i < textosNombresEnemigos.size()) {
                textosNombresEnemigos.get(i).dibujar();
            }
        }
        Render.batch.end();

        // Rectángulo de selección (Copiado de PantallaLimboMulti)
        if (opc >= 0 && opc < enemigoSpr.size()) {
            if (opc >= enemigosMuertos.length || !enemigosMuertos[opc]) { // Validaci¾n clave
                Render.renderer.begin(ShapeRenderer.ShapeType.Line);
                Render.renderer.setColor(Color.YELLOW);
                Imagen spr = enemigoSpr.get(opc);
                Render.renderer.rect(spr.getX(), spr.getY(), spr.getAncho(), spr.getAlto());
                Render.renderer.end();
            }
        }
    }

    private void manejarSeleccionEnemigo() {
        boolean[] enemigosMuertos = hiloCliente != null ? hiloCliente.getEnemigosMuertos() : new boolean[0];

        // Validar opc inicial si el actual esta muerto
        if (opc < enemigosMuertos.length && enemigosMuertos[opc]) {
             // Buscar siguiente vivo
             for(int i=0; i<enemigoSpr.size(); i++) {
                 if(i < enemigosMuertos.length && !enemigosMuertos[i]) {
                     opc = i;
                     break;
                 }
             }
        }

        if (opc >= enemigoSpr.size()) opc = 0;
        if (opc < 0) opc = enemigoSpr.size() - 1;

        // Mouse hover - Solo si esta vivo
        int mouseX = entradas.getMouseX();
        int mouseY = entradas.getMouseY();

        for (int i = 0; i < enemigoSpr.size(); i++) {
            if (i < enemigosMuertos.length && enemigosMuertos[i]) continue;

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
                if (opc >= enemigoSpr.size()) opc = 0;

                // Saltar muertos
                while (opc < enemigosMuertos.length && enemigosMuertos[opc]) {
                    opc++;
                    if (opc >= enemigoSpr.size()) opc = 0;
                }

                tiempo = 0;
            }
            if (entradas.isIzquierda()) {
                opc--;
                if (opc < 0) opc = enemigoSpr.size() - 1;

                // Saltar muertos
                while (opc >= 0 && opc < enemigosMuertos.length && enemigosMuertos[opc]) {
                    opc--;
                    if (opc < 0) opc = enemigoSpr.size() - 1;
                }

                tiempo = 0;
            }

            if (entradas.isEnter() || justClicked) { // Input SEGURO
                // Doble validacion antes de enviar
                if (opc >= 0 && opc < enemigosMuertos.length && !enemigosMuertos[opc]) {
                    enemigoSeleccionado = opc;
                    hiloCliente.enviarMensajeAlServidor("SELECCIONAR_ENEMIGO:" + opc);
                    estadoActual = EstadoBatallaMulti.SELECCION_ATAQUE;
                    tiempo = 0;
                }
            }

            if (!(entradas.isEnter() || entradas.isClick())) {
                esperandoInput = false;
            }
        }

        Render.batch.begin();
        if (textoSeleccionarEnemigo != null) {
            textoSeleccionarEnemigo.dibujar();
        }
        Render.batch.end();

        // NOTA: El rectángulo se dibuja ahora en dibujarEnemigos() para consistencia visual
    }

    private void dibujarInfoAliado() {
        if (hiloCliente == null) return;
        int soy = numeroJugador;
        int el = (soy == 1) ? 2 : 1;

        String clase = (el == 1) ? hiloCliente.getClaseJugador1() : hiloCliente.getClaseJugador2();
        float vida = (el == 1) ? hiloCliente.getVidaJugador1() : hiloCliente.getVidaJugador2();
        float maxVida = (el == 1) ? hiloCliente.getVidaMaxJugador1() : hiloCliente.getVidaMaxJugador2();
        int fe = (el == 1) ? hiloCliente.getFeJugador1() : hiloCliente.getFeJugador2();
        int maxFe = (el == 1) ? hiloCliente.getFeMaxJugador1() : hiloCliente.getFeMaxJugador2();
        int monedas = (el == 1) ? hiloCliente.getMonedasJugador1() : hiloCliente.getMonedasJugador2();

        // Si no hay datos, mostrar espera
        if (clase.equals("---")) return;

        // MOVIDO A LA DERECHA Y ABAJO
        int xBase = Config.ANCHO - 250;
        int yBase = Config.ALTO - 200;

        infoAliadoNombre.setTexto("Aliado: P" + el + " [" + clase + "]");
        infoAliadoNombre.setPosition(xBase, yBase);
        infoAliadoNombre.dibujar();

        infoAliadoVida.setTexto("HP: " + (int)vida + "-" + (int)maxVida);
        infoAliadoVida.setPosition(xBase, yBase - 30);
        infoAliadoVida.dibujar();

        infoAliadoFe.setTexto("Fe: " + fe + "-" + maxFe + "  $: " + monedas);
        infoAliadoFe.setPosition(xBase, yBase - 60);
        infoAliadoFe.dibujar();
    }

    private void manejarSeleccionAtaque() {
        List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
        int feActual = Config.personajeSeleccionado.getFeActual();

        int mouseX = entradas.getMouseX();
        int mouseY = entradas.getMouseY();
        for (int i = 0; i < textoAtaques.length; i++) {
            if (mouseX >= textoAtaques[i].getX() && mouseX <= textoAtaques[i].getX() + textoAtaques[i].getAncho() &&
                mouseY >= textoAtaques[i].getY() - 40 && mouseY <= textoAtaques[i].getY() + 10) {
                 Ataque a = ataques.get(i);
                 if (a.getCantUsos() > 0 && a.getCostoFe() <= feActual) {
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

        for (Texto t : textoAtaques) {
            Render.batch.begin();
            t.dibujar();
            Render.batch.end();
        }

        Ataque ataqueSel = ataques.get(ataqueSeleccionado);

        Render.batch.begin();
        textoPS.setTexto("P.S. " + (int) Config.personajeSeleccionado.getVidaActual());
        textoPS.setPosition(500, 120);
        textoPS.dibujar();

        textoFe.setTexto("Fe: " + Config.personajeSeleccionado.getFeActual());
        textoFe.setPosition((int)(textoPS.getX() + textoPS.getAncho() + 20), 120);
        textoFe.dibujar();

        textoUsos.setTexto("Usos: " + ataqueSel.getCantUsos() + "   Daño: " + ataqueSel.getDanio());
        textoUsos.setPosition(500, 80);
        textoUsos.dibujar();

        if (ataqueSel.getCostoFe() > 0) {
            textoCostoFe.setTexto("Costo Fe: " + ataqueSel.getCostoFe());
            textoCostoFe.setPosition(500, 40);
            textoCostoFe.dibujar();
        }
        Render.batch.end();

        if (tiempo > 0.15f) {
             if (entradas.isDerecha()) {
                ataqueSeleccionado = (ataqueSeleccionado + 1) % textoAtaques.length;
                tiempo = 0;
            }
            if (entradas.isIzquierda()) {
                ataqueSeleccionado = (ataqueSeleccionado - 1 + textoAtaques.length) % textoAtaques.length;
                tiempo = 0;
            }

            if (!esperandoInput && (entradas.isEnter() || justClicked)) {

                // MULTIJUGADOR: Enviar selecciones por separado (Protocolo v1 soportado)
                hiloCliente.enviarMensajeAlServidor("SELECCIONAR_ENEMIGO:" + enemigoSeleccionado);
                hiloCliente.enviarMensajeAlServidor("SELECCIONAR_ATAQUE:" + ataqueSeleccionado);
                System.out.println("PantallaFraudeMulti: Enviadas selecciones: " + enemigoSeleccionado + ", " + ataqueSeleccionado);

                // CRUCIAL: Marcar que ya no es mi turno para evitar que el update() me regrese a SELECCION_ENEMIGO
                hiloCliente.setEsMiTurno(false);

                estadoActual = EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR;
                tiempo = 0;
                esperandoInput = true;
            }

            if (!(entradas.isEnter() || entradas.isClick())) {
                esperandoInput = false;
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        disposed = true;
        if (fondo != null) try{ fondo.dispose(); }catch(Exception e){}
        if (arena != null) try{ arena.dispose(); }catch(Exception e){}
        if (danioSpr != null) try{ danioSpr.dispose(); }catch(Exception e){}
        if (lugar != null) try{ lugar.dispose(); }catch(Exception e){}

        for (Imagen spr : enemigoSpr) if (spr != null) try{ spr.dispose(); }catch(Exception e){}
        enemigoSpr.clear();

        for (Texto t : textosNombresEnemigos) try{ t.dispose(); }catch(Exception e){}
        for (Texto t : textosVidasEnemigos) try{ t.dispose(); }catch(Exception e){}

        if (textoAtaques != null) for (Texto t : textoAtaques) if(t!=null) try{ t.dispose(); }catch(Exception e){}

        if (textoEnemigoSeleccionado != null) try{ textoEnemigoSeleccionado.dispose(); }catch(Exception e){}
        if (vidaEnemigoTexto != null) try{ vidaEnemigoTexto.dispose(); }catch(Exception e){}
        if (textoPS != null) try{ textoPS.dispose(); }catch(Exception e){}
        if (textoFe != null) try{ textoFe.dispose(); }catch(Exception e){}
        if (textoUsos != null) try{ textoUsos.dispose(); }catch(Exception e){}
        if (textoCostoFe != null) try{ textoCostoFe.dispose(); }catch(Exception e){}
        if (logTexto != null) try{ logTexto.dispose(); }catch(Exception e){}

        if (textoSeleccionarEnemigo != null) try{ textoSeleccionarEnemigo.dispose(); }catch(Exception e){}
        if (textoEsperandoReusable != null) try{ textoEsperandoReusable.dispose(); }catch(Exception e){}
        if (textoContinuarReusable != null) try{ textoContinuarReusable.dispose(); }catch(Exception e){}
        if (textoVictoria != null) try{ textoVictoria.dispose(); }catch(Exception e){}
        if (infoAliadoNombre != null) try{ infoAliadoNombre.dispose(); }catch(Exception e){}
        if (infoAliadoVida != null) try{ infoAliadoVida.dispose(); }catch(Exception e){}
        if (infoAliadoFe != null) try{ infoAliadoFe.dispose(); }catch(Exception e){}
    }
}
