package com.dojan.infiernoperfecto.red;

import com.dojan.infiernoperfecto.utiles.Config;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class HiloCliente extends Thread {

    private DatagramSocket socket;
    private InetAddress ipBroadcast;
    private InetAddress ipServidor; // ✅ Guardar IP real del servidor una vez conectado
    private static final int PUERTO_SERVIDOR = 6666;
    private boolean fin = false;
    private boolean conectado = false;
    private int jugadoresConectados = 0;
    private long tiempoUltimoMensaje = 0;
    private static final long TIMEOUT_REENVIO = 2000;

    public HiloCliente() {
        this.setDaemon(true);
        try {
            ipBroadcast = InetAddress.getByName("255.255.255.255");
            socket = new DatagramSocket();
            socket.setSoTimeout(1000); // Timeout de 1 segundo
            System.out.println("Cliente: Socket creado en puerto local " + socket.getLocalPort());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // ✅ Enviar el primer mensaje DENTRO del run, cuando ya estamos escuchando
        enviarMensaje("Conexion");
        tiempoUltimoMensaje = System.currentTimeMillis();

        do {
            // ✅ Reenviar si no está conectado y pasó el timeout
            if (!conectado && (System.currentTimeMillis() - tiempoUltimoMensaje) > TIMEOUT_REENVIO) {
                System.out.println("Cliente: Reenviando mensaje de conexión...");
                enviarMensaje("Conexion");
                tiempoUltimoMensaje = System.currentTimeMillis();
            }

            // ✅ Escuchar respuestas del servidor
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

    private void enviarMensaje(String msg) {
        try {
            byte[] mensaje = msg.getBytes();
            // Si ya conocemos la IP del servidor, enviar directo. Si no, broadcast
            InetAddress destino = (ipServidor != null) ? ipServidor : ipBroadcast;
            DatagramPacket dp = new DatagramPacket(mensaje, mensaje.length, destino, PUERTO_SERVIDOR);
            socket.send(dp);
            System.out.println("Cliente: Enviado '" + msg + "' a " + destino.getHostAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void procesarMensaje(DatagramPacket dp) {
        String msg = new String(dp.getData(), 0, dp.getLength()).trim();
        InetAddress servidorIP = dp.getAddress();
        int servidorPuerto = dp.getPort();

        System.out.println("Cliente: Recibido '" + msg + "' de " + servidorIP.getHostAddress() + ":" + servidorPuerto);

        if (msg.equals("OK")) {
            conectado = true;
            // ✅ Guardar la IP real del servidor
            this.ipServidor = servidorIP;
            jugadoresConectados = 1;
            System.out.println("Cliente: ✅ CONECTADO al servidor en " + servidorIP.getHostAddress());
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
            fin = true;
        }
    }

    public boolean isConectado() {
        return conectado;
    }

    public int getJugadoresConectados() {
        return jugadoresConectados;
    }

    public void desconectar() {
        if (conectado) {
            enviarMensaje("Desconexion");
        }
        fin = true;
    }

    private void cerrarConexion() {
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
        System.out.println("Cliente: Socket cerrado");
    }
}
