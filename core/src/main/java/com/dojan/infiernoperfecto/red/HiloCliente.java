package com.dojan.infiernoperfecto.red;

import com.dojan.infiernoperfecto.utiles.Config;

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

    private void enviarMensajeAlServidor(String msg) {
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
}
