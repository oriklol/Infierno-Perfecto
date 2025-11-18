package com.dojan.infiernoperfecto.red;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import com.dojan.infiernoperfecto.utiles.Config;

public class HiloCliente extends Thread {

    private DatagramSocket conexion;
    private InetAddress ipServer;
    private int puerto = 6666;
    private boolean fin = false;
    private boolean conectado = false;
    private int jugadoresConectados = 0;
    private long tiempoUltimoMensaje = 0;
    private static final long TIMEOUT_REENVIO = 2000; // 2 segundos

    public HiloCliente() {
        this.setDaemon(true);  // ✅ DAEMON THREAD: Se termina automáticamente al cerrar la JVM
        try {
            // Usar broadcast para descubrir el servidor
            ipServer = InetAddress.getByName("255.255.255.255");
            conexion = new DatagramSocket();
            conexion.setBroadcast(true);
            // Timeout para no bloquear indefinidamente
            conexion.setSoTimeout(1000); // 1 segundo
            System.out.println("Cliente: Iniciando búsqueda de servidor por broadcast...");
        } catch (SocketException | UnknownHostException e) {
            e.printStackTrace();
        }
        enviarMensaje("Conexion");
        tiempoUltimoMensaje = System.currentTimeMillis();
    }

    public void enviarMensaje(String msg) {
        byte[] mensaje = msg.getBytes();
        DatagramPacket dp = new DatagramPacket(mensaje, mensaje.length, ipServer, puerto);
        try {
            conexion.send(dp);
            System.out.println("Cliente: Enviado mensaje -> " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        do {
            // Reenviar mensaje de conexión si no hay respuesta
            if (!conectado && (System.currentTimeMillis() - tiempoUltimoMensaje) > TIMEOUT_REENVIO) {
                enviarMensaje("Conexion");
                tiempoUltimoMensaje = System.currentTimeMillis();
            }

            byte[] buffer = new byte[1024];
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            try {
                conexion.receive(dp);
                procesarMensaje(dp);
            } catch (SocketTimeoutException e) {
                // Timeout normal, continuar el loop
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!fin);

        cerrarConexion();
    }

    private void procesarMensaje(DatagramPacket dp) {
        // Convertir correctamente los bytes a String
        String msg = new String(dp.getData(), 0, dp.getLength()).trim();
        System.out.println("Cliente: Recibido mensaje -> " + msg);

        if (msg.equals("OK")) {
            ipServer = dp.getAddress();
            conectado = true;
            jugadoresConectados = 1;
            System.out.println("Cliente: Conectado al servidor " + ipServer.getHostAddress());
        } else if (msg.startsWith("ESPERANDO:")) {
            // Mensaje opcional del servidor indicando cuántos jugadores hay
            try {
                jugadoresConectados = Integer.parseInt(msg.split(":")[1]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (msg.equals("INICIAR")) {
            Config.empiezaPartida = true;
            jugadoresConectados = 2;
            System.out.println("Cliente: ¡Iniciando partida!");
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
        if (conexion != null && !conexion.isClosed()) {
            try {
                conexion.close();
                System.out.println("Cliente: Socket cerrado correctamente");
            } catch (Exception e) {
                System.err.println("Cliente: Error al cerrar socket - " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
