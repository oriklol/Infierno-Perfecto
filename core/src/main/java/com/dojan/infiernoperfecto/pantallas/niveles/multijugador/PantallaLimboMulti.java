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
import com.dojan.infiernoperfecto.red.HiloCliente;
import com.dojan.infiernoperfecto.utiles.*;
import com.dojan.infiernoperfecto.entidades.Jugador;
import com.dojan.infiernoperfecto.entidades.clases.Peleador;
import io.Entradas;

import java.util.ArrayList;
import java.util.List;

/**
 * Pantalla de Batalla Multijugador - Limbo
 * Versión completa basada en PantallaLimbo pero adaptada para multijugador
 * 
 * Diferencias clave vs PantallaLimbo:
 * - NO ejecuta batalla localmente
 * - Lee estado desde HiloCliente (vida, enemigos muertos, logs)
 * - Envía selecciones al servidor
 * - Sincroniza visualmente con mensajes del servidor
 */
public class PantallaLimboMulti implements Screen {
    
    // ============================================================
    // RECURSOS VISUALES (copiados de PantallaLimbo)
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
    private boolean esperandoConfirmacionResultados = false;
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

    // ============================================================
    // ESTADOS DE BATALLA MULTIJUGADOR
    // ============================================================
    // Enum EstadoBatallaMulti movido a archivo externo


    @Override
    public void show() {
        System.out.println("PantallaLimboMulti.show() ejecutado");

        musicaFondo = new Musica(Recursos.MUSICABATALLA);
        ControlAudio.setMusicaActual(musicaFondo);

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
            System.out.println("PantallaLimboMulti: ERROR - No hay cliente conectado");
            estadoActual = EstadoBatallaMulti.ESPERANDO_CONEXION;
        } else {
            System.out.println("PantallaLimboMulti: Cliente conectado, esperando datos de batalla");
            estadoActual = EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA;
        }

        esperandoEsc = false;
        esperandoInput = false;
        tiempo = 0;

        entradas = new Entradas();
        Gdx.input.setInputProcessor(entradas);
        
        // FASE 5: Validar que exista personaje seleccionado (evita crash en update)
        if (Config.personajeSeleccionado == null) {
            System.out.println("PantallaLimboMulti: WARN - PersonajeSeleccionado es NULL. Creando Peleador por defecto.");
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

        fondo = new Imagen(Recursos.FONDOLIMBO);
        arena = new Imagen(Recursos.FONDOARENA);
        danioSpr = new Imagen(Recursos.EFECTODANIO);

        lugar = new Texto(Recursos.FUENTEMENU, 60, Color.BLACK, false);
        lugar.setPosition((int) (Config.ANCHO / 1.2f), (int) (Config.ALTO / 1.1f));
        // MULTIJUGADOR: Mostrar nivel actual (se actualizará dinámicamente)
        lugar.setTexto(Config.nivel + " - " + Config.piso);

        textoEnemigoSeleccionado = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, false);
        vidaEnemigoTexto = new Texto(Recursos.FUENTEMENU, 30, Color.WHITE, false);
        textoPS = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
        textoFe = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
        textoUsos = new Texto(Recursos.FUENTEMENU, 40, Color.RED, false);
        textoCostoFe = new Texto(Recursos.FUENTEMENU, 32, Color.CORAL, false);
        logTexto = new Texto(Recursos.FUENTEMENU, 35, Color.WHITE, false);

        // Inicializar textos reutilizables UI (OPTIMIZACIÓN DE MEMORIA)
        textoSeleccionarEnemigo = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, false);
        textoSeleccionarEnemigo.setTexto("Selecciona a un enemigo");
        textoSeleccionarEnemigo.setPosition((Config.ANCHO / 2 - ((int) textoSeleccionarEnemigo.getAncho() / 2)), 120);

        textoEsperandoReusable = new Texto(Recursos.FUENTEMENU, 50, Color.YELLOW, false);
        textoEsperandoReusable.setTexto("Esperando al otro jugador...");
        textoEsperandoReusable.setPosition(Config.ANCHO / 2 - (int)(textoEsperandoReusable.getAncho() / 2), 150);

        textoContinuarReusable = new Texto(Recursos.FUENTEMENU, 30, Color.YELLOW, false);
        textoContinuarReusable.setTexto("Click o Enter para continuar");
        textoContinuarReusable.setPosition(50, 100);
    }

    /**
     * MULTIJUGADOR: Parsea datos de batalla del servidor y crea sprites/textos
     * Se ejecuta SOLO una vez al recibir "DATOS_BATALLA"
     */
    private void parsearDatosBatalla(String datos) {
        System.out.println("PantallaLimboMulti: parsearDatosBatalla invocada con: " + datos);
        
        if(hiloCliente != null) hiloCliente.resetBatallaFlags();

        String[] partes = datos.split(",");
        
        try {
            int nivelRecibido = Integer.parseInt(partes[0]);
            if (nivelRecibido < Config.nivel) return;
            Config.nivel = nivelRecibido;
        } catch (NumberFormatException e) {
            System.out.println("Error parseando nivel.");
        }

        if (lugar != null) {
            lugar.setTexto(Config.piso + " - " + Config.nivel);
        }
        
        int numEnemigos = (partes.length - 1) / 2;
        
        // Inicializar arrays de datos puros
        nombresEnemigos = new String[numEnemigos];
        vidasEnemigos = new float[numEnemigos];
        vidasMaximasEnemigos = new float[numEnemigos];
        enemigosMuertos = new boolean[numEnemigos];

        // 1. Limpiar recursos gráficos anteriores
        for (Imagen spr : enemigoSpr) if (spr != null) spr.dispose();
        enemigoSpr.clear();

        // 2. Limpiar textos anteriores
        for (Texto t : textosNombresEnemigos) t.dispose();
        textosNombresEnemigos.clear();
        
        for (Texto t : textosVidasEnemigos) t.dispose();
        textosVidasEnemigos.clear();
        
        // 3. Crear nuevos recursos
        for (int i = 0; i < numEnemigos; i++) {
            int indexDatos = 1 + (i * 2);
            nombresEnemigos[i] = partes[indexDatos];
            vidasEnemigos[i] = Float.parseFloat(partes[indexDatos + 1]);
            vidasMaximasEnemigos[i] = vidasEnemigos[i];
            
            // Sprite
            Imagen nuevoSpr = crearSpriteEnemigo(nombresEnemigos[i]);
            // Posicionar sprite (necesario para posicionar textos después)
            int posX;
            if (numEnemigos == 1) {
                posX = Config.ANCHO / 2 - (int)(nuevoSpr.getAncho() / 2);
            } else {
                posX = (int) ((Config.ANCHO / 3.5f) * i) + 20;
            }
            nuevoSpr.setPosition(posX, Config.ALTO / 2);
            enemigoSpr.add(nuevoSpr);
            
            // Texto Nombre (Creado una sola vez)
            Texto tNombre = new Texto(Recursos.FUENTEMENU, 24, Color.CYAN, false);
            tNombre.setTexto(nombresEnemigos[i]);
            tNombre.setPosition(
                (int) (nuevoSpr.getX() + nuevoSpr.getAncho() / 2 - tNombre.getAncho() / 2),
                (int) (nuevoSpr.getY() + nuevoSpr.getAlto() + 10)
            );
            textosNombresEnemigos.add(tNombre);
            
            // Texto Vida (Creado una sola vez)
            Texto tVida = new Texto(Recursos.FUENTEMENU, 30, Color.WHITE, false);
            tVida.setTexto("HP: " + (int)vidasEnemigos[i]);
            tVida.setPosition(
                (int) (nuevoSpr.getX() + nuevoSpr.getAncho() / 2 - tVida.getAncho() / 2),
                (int) (nuevoSpr.getY() - 20)
            );
            textosVidasEnemigos.add(tVida);
        }
        
        // Crear botones de ataque (Reutilización simple, recreamos si cambian, es poco frecuente)
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
        
        System.out.println("PantallaLimboMulti: Parseados " + numEnemigos + " enemigos y generados recursos gráficos.");
    }

    private Imagen crearSpriteEnemigo(String nombre) {
        String recurso = Recursos.ENEMIGOLIMBO1; // Default
        if (nombre.contains("Mini") || nombre.contains("mini")) {
            recurso = Recursos.ENEMIGOLIMBO1;
        } else if (nombre.contains("Esbirro") || nombre.contains("esbirro")) {
            recurso = Recursos.ENEMIGOLIMBO2;
        } else if (nombre.contains("Sabueso") || nombre.contains("sabueso")) {
            recurso = Recursos.MINIBOSSLIMBO;
        }
        System.out.println("DEBUG SPRITE: Creando sprite para '" + nombre + "' usando recurso: " + recurso);
        return new Imagen(recurso);
    }

    @Override
    public void render(float delta) {
        if (disposed) return;
        ControlAudio.reproducirMusica();

        // Actualizar lógica de input seguro (Rising Edge Detection)
        boolean clickActual = entradas.isClick();
        justClicked = clickActual && !clickPrevio;
        clickPrevio = clickActual;

        // Configurar ShapeRenderer con la cámara del viewport
        Render.renderer.setProjectionMatrix(InfiernoPerfecto.camera.combined);

        // MULTIJUGADOR: Actualizar estado desde servidor
        if (actualizarEstadoDesdeServidor()) {
            return; // Si cambió de pantalla, dejar de renderizar
        }

        // Dibujar fondo
        Render.batch.begin();
        fondo.dibujar();
        arena.dibujar();
        lugar.dibujar();
        Render.batch.end();

        // Dibujar enemigos
        try {
           dibujarEnemigos();
        } catch(Exception e) {
           System.out.println("Error dibujando enemigos: " + e.getMessage());
        }

        // Manejo de teclas especiales
        if (!esperandoEsc && entradas.isEnciclopedia()) {
            GestorPantallas.getInstance().irAPantalla(new PantallaEnciclopedia());
            esperandoEsc = true;
            return; // IMPORTANT: Return to avoid NPE
        }

        if (!esperandoEsc && entradas.isEsc()) {
            GestorPantallas.getInstance().irAPantalla(new PantallaOpciones(true));
            esperandoEsc = true;
            return; // IMPORTANT: Return to avoid NPE
        }

        if (!entradas.isEsc() && !entradas.isEnciclopedia()) {
            esperandoEsc = false;
        }

        tiempo += delta;

        // Máquina de estados
        if (null != estadoActual) switch (estadoActual) {
            case ESPERANDO_DATOS_BATALLA:
                Render.batch.begin();
                Texto esperando = new Texto(Recursos.FUENTEMENU, 60, Color.WHITE, false); // Podria optimizarse
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
                // Mostrar log hasta que el usuario haga click
                Render.batch.begin();

                if (logBatalla != null && !logBatalla.isEmpty()) {
                    // Reusar logTexto si es posible, o crear uno temporal si se necesita configuración diferente
                    // Usaremos logTexto configurado en inicializarRecursos
                    logTexto.setTexto(logBatalla);
                    logTexto.setPosition(50, Config.ALTO / 2); // Centrado vertical aprox
                    logTexto.dibujar();
                }
                
                // Mensaje parpadeante
                if ((int)(tiempo * 2) % 2 == 0) {
                    if (textoContinuarReusable != null) {
                        textoContinuarReusable.dibujar();
                    }
                }

                Render.batch.end();
                
                // Esperar click del usuario (USANDO JUST CLICKED)
                if (entradas.isEnter() || justClicked) { 
                    System.out.println("PantallaLimboMulti: Usuario confirmó log con click seguro.");
                    
                    // Limpiar el log local
                    logBatalla = "";
                    hiloCliente.limpiarLogBatalla();
                    
                    // Notificar al servidor que vimos el log
                    hiloCliente.enviarMensajeAlServidor("CONFIRMAR_LOG");
                    
                    // Esperar respuesta del servidor (TU_TURNO o FIN_BATALLA)
                    estadoActual = EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR;
                    tiempo = 0;
                }
                break;

            case FIN_BATALLA:
                Render.batch.begin();
                Texto textoVictoria = new Texto(Recursos.FUENTEMENU, 80, Color.GOLD, false);
                textoVictoria.setTexto("¡VICTORIA!");
                textoVictoria.setPosition(
                    Config.ANCHO / 2 - (int)(textoVictoria.getAncho() / 2),
                    Config.ALTO / 2 + 50
                );
                textoVictoria.dibujar();
                Render.batch.end();
                
                if (tiempo > 3.0f) {
                    System.out.println("PantallaLimboMulti: FIN_BATALLA tiempo cumplido. Enviando LISTO...");
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

    /**
     * @return true si se cambió de pantalla
     */
    private boolean actualizarEstadoDesdeServidor() {
        if (hiloCliente == null) return false;

        // Obtener número de jugador
        if (numeroJugador == 0 && hiloCliente.getNumeroJugador() > 0) {
            numeroJugador = hiloCliente.getNumeroJugador();
            System.out.println("PantallaLimboMulti: Soy el jugador " + numeroJugador);
        }
        
        // 1. PROCESAR DATOS DE BATALLA NUEVOS
        if (hiloCliente.hasDatosBatallaActualizados() && 
            estadoActual == EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA) {
            
            String datos = hiloCliente.getDatosBatalla();
            if (datos != null && !datos.isEmpty()) {
                System.out.println("PantallaLimboMulti: Parseando datos FRESCOS: " + datos);
                parsearDatosBatalla(datos);
                hiloCliente.consumirDatosBatalla();
                estadoActual = EstadoBatallaMulti.SELECCION_ENEMIGO;
                System.out.println("PantallaLimboMulti: Estado -> SELECCION_ENEMIGO");
            }
            return false;
        }
        
        // 2. MOSTRAR LOG DE BATALLA
        String nuevoLog = hiloCliente.getLogBatalla();
        if (nuevoLog != null && !nuevoLog.isEmpty()) {
            logBatalla = nuevoLog;
            if (estadoActual != EstadoBatallaMulti.RESULTADOS_COMBATE &&
                estadoActual != EstadoBatallaMulti.FIN_BATALLA &&
                estadoActual != EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA) {
                
                System.out.println("PantallaLimboMulti: LOG recibido, mostrando RESULTADOS_COMBATE");
                estadoActual = EstadoBatallaMulti.RESULTADOS_COMBATE;
                tiempo = 0;
                esperandoInput = false;
                return false;
            }
        }
        
        // 3. VERIFICAR FIN DE BATALLA
        if (hiloCliente.isBatallaTerminada() && estadoActual != EstadoBatallaMulti.FIN_BATALLA) {
            System.out.println("PantallaLimboMulti: Batalla terminada, estado -> FIN_BATALLA");
            estadoActual = EstadoBatallaMulti.FIN_BATALLA;
            tiempo = 0;
            return false;
        }
        
        // 4. SINCRONIZAR VIDA DE ENEMIGOS
        if (vidasEnemigos != null && hiloCliente.getNumEnemigos() > 0) {
            float[] vidasActualizadas = hiloCliente.getVidasEnemigos();
            for (int i = 0; i < Math.min(vidasEnemigos.length, vidasActualizadas.length); i++) {
                if (vidasEnemigos[i] != vidasActualizadas[i]) {
                     vidasEnemigos[i] = vidasActualizadas[i];
                     if (i < textosVidasEnemigos.size()) {
                         textosVidasEnemigos.get(i).setTexto("HP: " + (int)vidasEnemigos[i]);
                         // Recentrar - asumiendo posicion de sprite no cambia drásticamente en batalla estática
                         if (i < enemigoSpr.size()) {
                             Imagen spr = enemigoSpr.get(i);
                             Texto tVida = textosVidasEnemigos.get(i);
                             tVida.setPosition(
                                (int) (spr.getX() + spr.getAncho() / 2 - tVida.getAncho() / 2),
                                (int) (spr.getY() - 20)
                            );
                         }
                     }
                }
            }
        }
        
        // 5. SINCRONIZAR VIDA DEL JUGADOR
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
        
        // 6. VERIFICAR SI ES MI TURNO
        if (hiloCliente.isEsMiTurno() && estadoActual == EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR) {
            estadoActual = EstadoBatallaMulti.SELECCION_ENEMIGO;
            System.out.println("PantallaLimboMulti: Es mi turno, estado -> SELECCION_ENEMIGO");
        }
        
        // 8. VERIFICAR TRANSICIÓN A TIENDA
        if (hiloCliente.isIrATienda()) {
            System.out.println("PantallaLimboMulti: Servidor indica IR A TIENDA");
            hiloCliente.setIrATienda(false); // Consumir flag
            GestorPantallas.getInstance().irAPantalla(new PantallaTiendaMulti());
            return true;
        }

        // 7. VERIFICAR SI ESTOY ESPERANDO
        if (hiloCliente.isEsperandoOtroJugador() && 
            estadoActual != EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR &&
            estadoActual != EstadoBatallaMulti.FIN_BATALLA &&
            estadoActual != EstadoBatallaMulti.RESULTADOS_COMBATE &&
            estadoActual != EstadoBatallaMulti.ESPERANDO_DATOS_BATALLA) {
            estadoActual = EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR;
            System.out.println("PantallaLimboMulti: Esperando otro jugador");
        }
        
        return false;
    }

    private void dibujarEnemigos() {
        if (enemigoSpr.isEmpty()) return;
        
        boolean[] enemigosMuertos = hiloCliente != null ? hiloCliente.getEnemigosMuertos() : new boolean[0];
        
        Render.batch.begin();
        for (int i = 0; i < enemigoSpr.size(); i++) {
            // Ocultar enemigos muertos
            if (i < enemigosMuertos.length && enemigosMuertos[i]) {
                continue;
            }
            
            // Dibujar Sprite
            enemigoSpr.get(i).dibujar();

            // Dibujar textos pre-generados de las listas
            if (i < textosVidasEnemigos.size()) {
                textosVidasEnemigos.get(i).dibujar();
            }
            
            if (i < textosNombresEnemigos.size()) {
                textosNombresEnemigos.get(i).dibujar();
            }
        }
        Render.batch.end();

        // Rectángulo de selección
        if (opc >= 0 && opc < enemigoSpr.size()) {
            if (opc >= enemigosMuertos.length || !enemigosMuertos[opc]) { 
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
                    // FASE 5: Enviar selección de enemigo al servidor
                    hiloCliente.enviarMensajeAlServidor("SELECCIONAR_ENEMIGO:" + opc);
                    estadoActual = EstadoBatallaMulti.SELECCION_ATAQUE;
                    tiempo = 0;
                }
            }
        }

        Render.batch.begin();
        if (textoSeleccionarEnemigo != null) {
            textoSeleccionarEnemigo.dibujar(); 
        }
        Render.batch.end();
    }

    private void manejarSeleccionAtaque() {
        if (Config.personajeSeleccionado == null || textoAtaques == null) return;
        
        List<Ataque> ataques = Config.personajeSeleccionado.getClase().getAtaques();
        int feActual = Config.personajeSeleccionado.getFeActual();

        // Mouse hover
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

        // Colorear ataques
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

        // Dibujar ataques
        for (Texto textoAtaque : textoAtaques) {
            Render.batch.begin();
            textoAtaque.dibujar();
            Render.batch.end();
        }

        if (ataqueSeleccionado < 0) ataqueSeleccionado = 0;
        if (ataqueSeleccionado >= textoAtaques.length) ataqueSeleccionado = textoAtaques.length - 1;

        Ataque ataqueSel = ataques.get(ataqueSeleccionado);

        // Dibujar info del jugador
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
                ataqueSeleccionado++;
                if (ataqueSeleccionado >= textoAtaques.length) ataqueSeleccionado = 0;
                tiempo = 0;
            }
            if (entradas.isIzquierda()) {
                ataqueSeleccionado--;
                if (ataqueSeleccionado < 0) ataqueSeleccionado = textoAtaques.length - 1;
                tiempo = 0;
            }

            if (entradas.isEnter() || justClicked) {
                Ataque a = ataques.get(ataqueSeleccionado);
                if (a.getCantUsos() > 0 && a.getCostoFe() <= feActual) {
                    hiloCliente.enviarMensajeAlServidor("SELECCIONAR_ATAQUE:" + ataqueSeleccionado);
                    // FASE 5: Marcar que ya no es mi turno para evitar saltos prematuros a RESULTADOS
                    hiloCliente.setEsMiTurno(false);
                    
                    estadoActual = EstadoBatallaMulti.ESPERANDO_OTRO_JUGADOR;
                    esperandoInput = true;
                }
                tiempo = 0;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        InfiernoPerfecto.viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        if (disposed) return;
        disposed = true;
        System.out.println("PantallaLimboMulti: DISPOSING resources");
        if (musicaFondo != null) musicaFondo.dispose();
        if (fondo != null) fondo.dispose();
        if (arena != null) arena.dispose();
        if (danioSpr != null) danioSpr.dispose();
        
        for (Imagen spr : enemigoSpr) {
            if (spr != null) spr.dispose();
        }
        enemigoSpr.clear();
        
        // Limpiar textos
        for (Texto t : textosNombresEnemigos) if (t!=null) t.dispose();
        for (Texto t : textosVidasEnemigos) if (t!=null) t.dispose();
        
        if (textoAtaques != null) {
            for (Texto t : textoAtaques) if (t!=null) t.dispose();
        }
        
        // Disposear textos estáticos
        if (lugar != null) lugar.dispose();
        if (textoSeleccionarEnemigo != null) textoSeleccionarEnemigo.dispose();
        if (textoEsperandoReusable != null) textoEsperandoReusable.dispose();
        if (textoContinuarReusable != null) textoContinuarReusable.dispose();
    }
}
