package com.dojan.infiernoperfecto.red;

import com.dojan.infiernoperfecto.utiles.Config;
import com.badlogic.gdx.Gdx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class HiloCliente extends Thread {

    private DatagramSocket socket;
    private InetAddress ipServidor;
    private static final int PUERTO_SERVIDOR = 6666;
    private static final int PUERTO_CLIENTE = 6667;
    private boolean fin = false;
    private boolean conectado = false;
    private boolean servidorCaido = false;
    private boolean clienteExternoDesconectado = false;
    private int jugadoresConectados = 0;
    private long tiempoUltimoMensaje = 0;
    private long tiempoUltimoHeartbeat = 0;
    private static final long TIMEOUT_REENVIO = 2000;
    private static final long TIMEOUT_SERVIDOR = 8000;
    // ============================================================
    // ATRIBUTOS PARA MODO MULTIJUGADOR - FASE 2.2
    // ============================================================
    private int numeroJugador = 0;          // Número asignado por el servidor (1 o 2)
    private String datosBatalla = "";
    private boolean datosBatallaActualizados = false;       // Datos de enemigos
    private boolean esMiTurno = false;      // true cuando es el turno del jugador
    private boolean esperandoOtroJugador = false;  // true cuando ya seleccionó y espera al otro
    private String logBatalla = "";         // Log de combate
    private String resultadoBatalla = "";   // Resultado final (VICTORIA/DERROTA)
    
    // FASE 1 COMPLETAR PANTALLA: Estado de enemigos
    private float[] vidasEnemigos = new float[0];  // Vidas actuales de enemigos
    private boolean[] enemigosMuertos = new boolean[0];  // Estado de muerte de enemigos
    private int numEnemigos = 0;  // Cantidad de enemigos en batalla
    
    // FASE 5.2: Instancia estática para auto-detección
    private static HiloCliente instanciaActiva = null;

    // FASE 5.3: Variables de estado de batalla faltantes
    private boolean batallaTerminada = false;
    private boolean victoria = false;
    private boolean irATienda = false;
    private boolean todosListosResultados = false;
    
    private float vidaJugador1 = -1;
    private int feJugador1 = -1;
    private float vidaJugador2 = -1;
    private int feJugador2 = -1;
    private int monedasJugador1 = -1;
    private int monedasJugador2 = -1;

    public HiloCliente() {
        this.setDaemon(true);
        try {
            try {
                socket = new DatagramSocket(PUERTO_CLIENTE);
                System.out.println("Cliente: Socket creado escuchando en puerto " + PUERTO_CLIENTE);
            } catch (Exception e) {
                socket = new DatagramSocket(0);
                System.out.println("Cliente: Puerto 6667 ocupado, usando puerto aleatorio " + socket.getLocalPort());
            }
            socket.setBroadcast(true);
            socket.setSoTimeout(1000);
            
            // Registrar esta instancia como activa
            instanciaActiva = this;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        enviarMensajeAlServidor("Conexion");
        tiempoUltimoMensaje = System.currentTimeMillis();
        tiempoUltimoHeartbeat = System.currentTimeMillis();

        do {
            if (!conectado && (System.currentTimeMillis() - tiempoUltimoMensaje) > TIMEOUT_REENVIO) {
                System.out.println("Cliente: Reenviando mensaje de conexión...");
                enviarMensajeAlServidor("Conexion");
                tiempoUltimoMensaje = System.currentTimeMillis();
            }

            if (conectado && (System.currentTimeMillis() - tiempoUltimoHeartbeat) > TIMEOUT_SERVIDOR) {
                System.out.println("Cliente: ⚠️ TIMEOUT - Servidor no responde");
                servidorCaido = true;
                conectado = false;
            }

            byte[] buffer = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            try {
                socket.receive(dp);
                procesarMensaje(dp);
            } catch (SocketTimeoutException e) {
                // Timeout normal, continuar
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!fin);

        cerrarConexion();
    }

    public void enviarMensajeAlServidor(String msg) {
        try {
            byte[] mensaje = msg.getBytes();
            InetAddress destino;

            if (ipServidor != null) {
                destino = ipServidor;
            } else {
                destino = InetAddress.getByName("255.255.255.255");
            }

            DatagramPacket dp = new DatagramPacket(mensaje, mensaje.length, destino, PUERTO_SERVIDOR);
            socket.send(dp);
            System.out.println("Cliente: Enviado '" + msg + "' a " + destino.getHostAddress() + ":" + PUERTO_SERVIDOR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarMensaje(DatagramPacket dp) {
        String msg = new String(dp.getData(), 0, dp.getLength()).trim();
        InetAddress origenIP = dp.getAddress();
        int origenPuerto = dp.getPort();

        // FILTRO: Ignorar mensajes de IPs desconocidas (prevenir basura)
        if (ipServidor != null && !origenIP.equals(ipServidor)) {
            // Silenciosamente ignorar (no llenar logs con basura)
            return;
        }

        System.out.println("Cliente: Recibido '" + msg + "' de " + origenIP.getHostAddress() + ":" + origenPuerto);

        tiempoUltimoHeartbeat = System.currentTimeMillis();

        if (msg.equals("OK")) {
            conectado = true;
            this.ipServidor = origenIP;
            jugadoresConectados = 1;
            System.out.println("Cliente: ✅ CONECTADO al servidor en " + origenIP.getHostAddress());

        } else if (msg.startsWith("ESPERANDO:")) {
            try {
                jugadoresConectados = Integer.parseInt(msg.split(":")[1]);
                System.out.println("Cliente: Jugadores conectados -> " + jugadoresConectados + "/2");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (msg.equals("INICIAR")) {
            System.out.println("Cliente: ========================================");
            System.out.println("Cliente: ¡¡¡MENSAJE INICIAR RECIBIDO!!!");
            System.out.println("Cliente: ========================================");
            Config.empiezaPartida = true;
            jugadoresConectados = 2;

        } else if (msg.equals("SERVIDOR_LLENO")) {
            System.out.println("Cliente: ⚠️ El servidor está lleno");
            conectado = false;
            fin = true;

        } else if (msg.equals("HEARTBEAT")) {
            // Solo actualizar el tiempo (ya lo hicimos arriba)

        } else if (msg.equals("SERVIDOR_CERRANDO")) {
            System.out.println("Cliente: 🚨 SERVIDOR SE ESTÁ CERRANDO");
            servidorCaido = true;
            conectado = false;

        } else if (msg.contains("COMPANIERO_DESCONECTADO")) {
            System.out.println("Cliente: 🚨 EL OTRO CLIENTE SE DESCONECTÓ");
            clienteExternoDesconectado = true;
            conectado = false;
            fin = true; // detiene el hilo
        
        // ============================================================
        // MENSAJES DE BATALLA MULTIJUGADOR - FASE 2.2
        // ============================================================
        } else if (msg.startsWith("ASIGNAR_JUGADOR:")) {
            try {
                numeroJugador = Integer.parseInt(msg.split(":")[1]);
                System.out.println("Cliente: Asignado como Jugador " + numeroJugador);
            } catch (Exception e) {
                e.printStackTrace();
            }
        
        } else if (msg.startsWith("DATOS_BATALLA:")) {
            this.datosBatalla = msg.substring("DATOS_BATALLA:".length());
            this.datosBatallaActualizados = true;
            System.out.println("Cliente: Datos de batalla recibidos (FRESCOS): " + datosBatalla);
            
            // FASE 1: Inicializar arrays de enemigos
            // Formato: nivel,nombre1,hp1,nombre2,hp2...
            String[] partes = datosBatalla.split(",");
            
            // Validar formato (offset por nivel)
            numEnemigos = (partes.length - 1) / 2;
            vidasEnemigos = new float[numEnemigos];
            enemigosMuertos = new boolean[numEnemigos];
            
            // Parsear vidas iniciales (empezando desde índice 1 del array)
            for (int i = 0; i < numEnemigos; i++) {
                // El HP está en: 1 (nivel) + i*2 (iteración) + 1 (nombre) = index + 1
                // indices:
                // partes[0] = nivel
                // partes[1] = nombre 0
                // partes[2] = hp 0
                // partes[3] = nombre 1
                // partes[4] = hp 1
                try {
                    vidasEnemigos[i] = Float.parseFloat(partes[2 + (i * 2)]);
                    enemigosMuertos[i] = false;
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                   System.out.println("HiloCliente: Error parseando HP enemigo " + i);
                }
            }
            System.out.println("Cliente: Inicializados " + numEnemigos + " enemigos (Protocolo v2)");
        
        } else if (msg.equals("TU_TURNO")) {
            esMiTurno = true;
            esperandoOtroJugador = false;
            System.out.println("Cliente: ¡Es tu turno!");
        }// AGREGAR DESPUÉS DE PROCESAR "TU_TURNO"
        else if (msg.equals("FIN_BATALLA:VICTORIA")) {
            batallaTerminada = true;
            victoria = true;
            System.out.println("Cliente: ¡VICTORIA! Todos los enemigos derrotados");
        }
        else if (msg.equals("IR_A_TIENDA")) {
            irATienda = true;
            System.out.println("Cliente: Ir a tienda después de este nivel");
        }else if (msg.equals("VICTORIA_FINAL")) {
            System.out.println("Cliente: ¡VICTORIA FINAL DEL JUEGO!");
        } else if (msg.equals("ESPERANDO_OTRO_JUGADOR")) {
            esperandoOtroJugador = true;
            esMiTurno = false;
            System.out.println("Cliente: Esperando a que el otro jugador elija...");
        
        } else if (msg.startsWith("LOG_BATALLA:")) {
            // Acumular mensajes de log de batalla
            String mensajeLog = msg.substring("LOG_BATALLA:".length());
            if (logBatalla == null || logBatalla.isEmpty()) {
                logBatalla = mensajeLog;
            } else {
                logBatalla += "\n" + mensajeLog;
            }
            System.out.println("Cliente: [BATALLA] " + mensajeLog);
        
        } else if (msg.startsWith("ACTUALIZAR_ENEMIGO:")) {
            try {
                // Formato: ACTUALIZAR_ENEMIGO:indice:vida,maxVida
                String[] partes = msg.split(":");
                int indice = Integer.parseInt(partes[1]);
                String[] datosVida = partes[2].split(",");
                float vida = Float.parseFloat(datosVida[0]);
                
                if (indice >= 0 && indice < vidasEnemigos.length) {
                    vidasEnemigos[indice] = vida;
                }
                System.out.println("Cliente: Actualización de enemigo: " + msg);
            } catch (Exception e) {
                System.out.println("Cliente: Error parsing ACTUALIZAR_ENEMIGO: " + e.getMessage());
            }

        } else if (msg.equals("TODOS_LISTOS_RESULTADOS")) {
            todosListosResultados = true;
            System.out.println("Cliente: Todos listos para salir de resultados");
        
        } else if (msg.startsWith("ACTUALIZAR_JUGADOR:")) {
            try {
                String[] partes = msg.split(":");
                int numJug = Integer.parseInt(partes[1]);
                float vida = Float.parseFloat(partes[2]);
                int fe = Integer.parseInt(partes[3]);
                
                int monedas = -1;
                if (partes.length > 4) {
                    try {
                        monedas = Integer.parseInt(partes[4]);
                    } catch (NumberFormatException e) {
                        // ignorar si no es numero
                    }
                }
                
                if (numJug == 1) {
                    vidaJugador1 = vida;
                    feJugador1 = fe;
                    if (monedas != -1) monedasJugador1 = monedas;
                } else if (numJug == 2) {
                    vidaJugador2 = vida;
                    feJugador2 = fe;
                    if (monedas != -1) monedasJugador2 = monedas;
                }
                
                // Actualizar DIRECTAMENTE la configuración global si soy ese jugador
                if (numJug == numeroJugador && Config.personajeSeleccionado != null) {
                    final int m = monedas;
                    final float v = vida;
                    final int f = fe;
                    
                    if (Gdx.app != null) {
                        Gdx.app.postRunnable(new Runnable() {
                            @Override
                            public void run() {
                                if (m != -1) Config.personajeSeleccionado.setMonedasActual(m);
                                Config.personajeSeleccionado.setVidaActual(v);
                                Config.personajeSeleccionado.setFeActual(f);
                            }
                        });
                    }
                }

                String monedasStr = (monedas != -1) ? String.valueOf(monedas) : "N/A";
                System.out.println("Cliente: Actualización de jugador " + numJug + ": Vida=" + vida + ", Fe=" + fe + ", Monedas=" + monedasStr);
            } catch (Exception e) {
                System.out.println("Cliente: Error parsing ACTUALIZAR_JUGADOR: " + e.getMessage());
            }
        
        } else if (msg.startsWith("ENEMIGO_MUERTO:")) {
            // FASE 1: Marcar enemigo como muerto
            // Formato: ENEMIGO_MUERTO:indice
            try {
                int indice = Integer.parseInt(msg.substring("ENEMIGO_MUERTO:".length()));
                if (indice >= 0 && indice < enemigosMuertos.length) {
                    enemigosMuertos[indice] = true;
                    vidasEnemigos[indice] = 0;
                    System.out.println("Cliente: Enemigo " + indice + " marcado como MUERTO");
                }
            } catch (Exception e) {
                System.out.println("Cliente: Error procesando ENEMIGO_MUERTO: " + e.getMessage());
            }
            System.out.println("Cliente: Enemigo eliminado: " + msg);

        // AGREGAR DESPUÉS DE PROCESAR "SELECCIONAR_ATAQUE"

        }else if (msg.startsWith("LOG_BATALLA:")) {
            String logMensaje = msg.substring("LOG_BATALLA:".length());
            System.out.println("Cliente: [BATALLA] " + logMensaje);

        } else if (msg.startsWith("FIN_BATALLA:")) {
            String resultado = msg.substring("FIN_BATALLA:".length());
            System.out.println("Cliente: ========================================");
            System.out.println("Cliente: FIN DE BATALLA - " + resultado);
            System.out.println("Cliente: ========================================");
        }
    }

    public boolean isConectado() {
        return conectado;
    }

    public int getJugadoresConectados() {
        return jugadoresConectados;
    }

    public boolean isServidorCaido() {
        return servidorCaido;
    }


    public boolean isClienteExternoDesconectado() {
        return clienteExternoDesconectado;
    }
    
    // ============================================================
    // GETTERS PARA MODO MULTIJUGADOR - FASE 2.2
    // ============================================================
    
    public int getNumeroJugador() {
        return numeroJugador;
    }
    
    public String getDatosBatalla() {
        return datosBatalla;
    }
    
    public synchronized boolean hasDatosBatallaActualizados() {
        return datosBatallaActualizados;
    }
    
    public synchronized void consumirDatosBatalla() {
        datosBatallaActualizados = false;
        // NO limpiar datosBatalla aquí, se limpia en resetDatosBatalla()
    }
    
    /**
     * CRÍTICO: Resetear COMPLETAMENTE los datos de batalla
     * Llamar ANTES de esperar nuevos datos para evitar parsear datos viejos
     */
    public synchronized void resetDatosBatalla() {
        datosBatalla = "";
        datosBatallaActualizados = false;
        logBatalla = "";
        System.out.println("Cliente: Buffer de datos de batalla LIMPIADO");
    }
    
    public boolean isEsMiTurno() {
        return esMiTurno;
    }
    
    public boolean isEsperandoOtroJugador() {
        return esperandoOtroJugador;
    }
    
    public void setEsMiTurno(boolean esMiTurno) {
        this.esMiTurno = esMiTurno;
    }

    public void desconectar() {
        if (conectado) {
            System.out.println("Cliente: Enviando mensaje de desconexión...");
            enviarMensajeAlServidor("Desconexion");
            conectado = false;
        }
        fin = true;
    }

    public void cerrarConexion() {
        if (socket != null && !socket.isClosed()) {
            if (conectado) {
                enviarMensajeAlServidor("Desconexion");
            }
            socket.close();
        }
        System.out.println("Cliente: Socket cerrado");
    }
    
    // FASE 5: Métodos adicionales para PantallaLimboMulti
    public String getLogBatalla() {
        return logBatalla;
    }
    
    public String getResultadoBatalla() {
        return resultadoBatalla;
    }
    
    /**
     * Verifica si hay un cliente activo y conectado (modo multijugador)
     */
    public static boolean hayClienteActivo() {
        return instanciaActiva != null && instanciaActiva.isConectado();
    }
    
    /**
     * Obtiene la instancia activa del cliente (para reutilizar en pantallas)
     */
    public static HiloCliente getInstanciaActiva() {
        return instanciaActiva;
    }
    
    // ============================================================
    // GETTERS PARA ESTADO DE ENEMIGOS - FASE 1
    // ============================================================
    
    /**
     * Obtiene el array de vidas de enemigos
     */
    public float[] getVidasEnemigos() {
        return vidasEnemigos;
    }
    
    /**
     * Obtiene el array de estados de muerte de enemigos
     */
    public boolean[] getEnemigosMuertos() {
        return enemigosMuertos;
    }
    
    /**
     * Obtiene la cantidad de enemigos en batalla
     */
    public int getNumEnemigos() {
        return numEnemigos;
    }

    public boolean isBatallaTerminada() { return batallaTerminada; }
    public void setBatallaTerminada(boolean v) { this.batallaTerminada = v; }
    public boolean isVictoria() { return victoria; }
    public boolean isIrATienda() { return irATienda; }
    public void setIrATienda(boolean irATienda) { this.irATienda = irATienda; }
    
    public boolean isTodosListosResultados() { return todosListosResultados; }
    public void setTodosListosResultados(boolean v) { this.todosListosResultados = v; }
    
    public float getVidaJugador1() { return vidaJugador1; }
    public float getVidaJugador2() { return vidaJugador2; }
    public int getFeJugador1() { return feJugador1; }
    public int getFeJugador2() { return feJugador2; }
    public int getMonedasJugador1() { return monedasJugador1; }
    public int getMonedasJugador2() { return monedasJugador2; }


    public void resetBatallaFlags() {
        this.batallaTerminada = false;
        this.victoria = false;
        this.irATienda = false;
        this.todosListosResultados = false;
    }
    
    public void limpiarLogBatalla() {
        this.logBatalla = "";
    }
}
