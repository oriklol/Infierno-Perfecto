package com.dojan.infiernoperfecto.red;

import com.dojan.infiernoperfecto.utiles.Config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class HiloCliente extends Thread {

    private DatagramSocket socket;
    private InetAddress ipServidor; // IP real del servidor
    private static final int PUERTO_SERVIDOR = 6666; // Puerto donde escucha el servidor
    private static final int PUERTO_CLIENTE = 6667;  // ✅ Puerto donde ESTE cliente escucha broadcasts
    private boolean fin = false;
    private boolean conectado = false;
    private boolean servidorCaido = false; // ✅ Nueva bandera
    private int jugadoresConectados = 0;
    private long tiempoUltimoMensaje = 0;
    private long tiempoUltimoHeartbeat = 0; // ✅ Última vez que recibimos heartbeat
    private static final long TIMEOUT_REENVIO = 2000;
    private static final long TIMEOUT_SERVIDOR = 8000; // ✅ Si no recibimos nada en 8 segundos, servidor caído

    public HiloCliente() {
        this.setDaemon(true);
        try {
            // ✅ Intentar puerto fijo primero, si falla usar puerto aleatorio
            try {
                socket = new DatagramSocket(PUERTO_CLIENTE);
                System.out.println("Cliente: Socket creado escuchando en puerto " + PUERTO_CLIENTE);
            } catch (Exception e) {
                // Puerto ocupado, usar puerto aleatorio (para testing en misma PC)
                socket = new DatagramSocket(0); // 0 = puerto aleatorio
                System.out.println("Cliente: Puerto 6667 ocupado, usando puerto aleatorio " + socket.getLocalPort());
            }
            socket.setBroadcast(true);
            socket.setSoTimeout(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Enviar el primer mensaje de conexión
        enviarMensajeAlServidor("Conexion");
        tiempoUltimoMensaje = System.currentTimeMillis();

        do {
            // Reenviar si no está conectado y pasó el timeout
            if (!conectado && (System.currentTimeMillis() - tiempoUltimoMensaje) > TIMEOUT_REENVIO) {
                System.out.println("Cliente: Reenviando mensaje de conexión...");
                enviarMensajeAlServidor("Conexion");
                tiempoUltimoMensaje = System.currentTimeMillis();
            }

            // Escuchar respuestas (unicast del servidor Y broadcasts)
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

    // ✅ Envía mensajes UNICAST al servidor (puerto 6666)
    private void enviarMensajeAlServidor(String msg) {
        try {
            byte[] mensaje = msg.getBytes();
            InetAddress destino;

            // Si conocemos la IP del servidor, enviar directo. Si no, broadcast
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

        System.out.println("Cliente: Recibido '" + msg + "' de " + origenIP.getHostAddress() + ":" + origenPuerto);

        if (msg.equals("OK")) {
            conectado = true;
            // Guardar la IP real del servidor para futuros mensajes
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
        }
    }

    public boolean isConectado() {
        return conectado;
    }

    public int getJugadoresConectados() {
        return jugadoresConectados;
    }

    // ✅ Método para desconectar cuando el jugador sale del lobby
    public void desconectar() {
        if (conectado) {
            System.out.println("Cliente: Enviando mensaje de desconexión...");
            enviarMensajeAlServidor("Desconexion");
            conectado = false;
        }
        fin = true;
    }

    private void cerrarConexion() {
        if (socket != null && !socket.isClosed()) {
            if (conectado) {
                enviarMensajeAlServidor("Desconexion");
            }
            socket.close();
        }
        System.out.println("Cliente: Socket cerrado");
    }

    public boolean isServidorCaido() {
        return servidorCaido;
    }
}
