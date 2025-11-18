package com.dojan.infiernoperfecto.serverred;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class HiloServidor extends Thread {
    private DatagramSocket conexion; // Socket único para recibir y enviar
    private boolean fin = false;
    private List<DireccionRed> clientesConectados = new ArrayList<>();
    private static final int MAX_CLIENTES = 2;
    private static final int PUERTO_SERVIDOR = 6666;

    public HiloServidor() {
        this.setDaemon(true);
        try {
            conexion = new DatagramSocket(PUERTO_SERVIDOR);
            conexion.setSoTimeout(1000);
            System.out.println("Servidor: Iniciado en puerto " + PUERTO_SERVIDOR);
            System.out.println("Servidor: Esperando conexiones...");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    // ✅ Enviar UNICAST a un cliente específico
    private void enviarUnicast(String msg, InetAddress ip, int puerto) {
        try {
            byte[] mensaje = msg.getBytes();
            DatagramPacket dp = new DatagramPacket(mensaje, mensaje.length, ip, puerto);
            conexion.send(dp);
            System.out.println("Servidor: Enviado UNICAST '" + msg + "' a " + ip.getHostAddress() + ":" + puerto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ✅ Enviar a TODOS los clientes conectados
    private void enviarATodos(String msg) {
        System.out.println("Servidor: Enviando a todos los clientes: '" + msg + "'");
        for (DireccionRed cliente : clientesConectados) {
            enviarUnicast(msg, cliente.getIp(), cliente.getPuerto());
        }
    }

    @Override
    public void run() {
        do {
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
        String msg = new String(dp.getData(), 0, dp.getLength()).trim();
        InetAddress clienteIP = dp.getAddress();
        int clientePuerto = dp.getPort();

        System.out.println("Servidor: Recibido '" + msg + "' de " + clienteIP.getHostAddress() + ":" + clientePuerto);

        if (msg.equals("Conexion")) {
            boolean yaConectado = false;
            for (DireccionRed cliente : clientesConectados) {
                if (cliente.getIp().equals(clienteIP) && cliente.getPuerto() == clientePuerto) {
                    yaConectado = true;
                    break;
                }
            }

            if (!yaConectado && clientesConectados.size() < MAX_CLIENTES) {
                clientesConectados.add(new DireccionRed(clienteIP, clientePuerto));

                // ✅ Responder SOLO a este cliente por UNICAST
                enviarUnicast("OK", clienteIP, clientePuerto);

                System.out.println("Servidor: ✅ Cliente conectado. Total: " + clientesConectados.size() + "/" + MAX_CLIENTES);

                // ✅ Notificar a TODOS los clientes el nuevo estado
                enviarATodos("ESPERANDO:" + clientesConectados.size());

                if (clientesConectados.size() == MAX_CLIENTES) {
                    System.out.println("Servidor: ========================================");
                    System.out.println("Servidor: ¡¡¡INICIANDO PARTIDA CON 2 JUGADORES!!!");
                    System.out.println("Servidor: ========================================");

                    // ✅ Enviar INICIAR a todos los clientes varias veces
                    for (int i = 0; i < 5; i++) {
                        enviarATodos("INICIAR");
                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (yaConectado) {
                // Cliente ya conectado, reenviar OK
                enviarUnicast("OK", clienteIP, clientePuerto);
            } else {
                // Servidor lleno
                enviarUnicast("SERVIDOR_LLENO", clienteIP, clientePuerto);
            }
        }
    }

    public int getCantidadClientes() {
        return clientesConectados.size();
    }

    public void detener() {
        fin = true;
    }

    private void cerrarConexion() {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
        System.out.println("Servidor: Socket cerrado correctamente");
    }
}
