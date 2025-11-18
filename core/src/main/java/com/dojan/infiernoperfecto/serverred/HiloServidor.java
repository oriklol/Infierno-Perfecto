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
    private DatagramSocket conexion;
    private boolean fin = false;
    private List<DireccionRed> clientesConectados = new ArrayList<>();
    private static final int MAX_CLIENTES = 2;

    public HiloServidor() {
        this.setDaemon(true);  // ✅ DAEMON THREAD: Se termina automáticamente al cerrar la JVM
        try {
            conexion = new DatagramSocket(6666);
            conexion.setSoTimeout(1000); // Timeout para poder verificar fin
            System.out.println("Servidor: Iniciado en puerto 6666");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje(String msg, InetAddress ip, int puerto) {
        byte[] mensaje = msg.getBytes();
        DatagramPacket dp = new DatagramPacket(mensaje, mensaje.length, ip, puerto);
        try {
            conexion.send(dp);
            System.out.println("Servidor: Enviado '" + msg + "' a " + ip.getHostAddress() + ":" + puerto);
        } catch (IOException e) {
            e.printStackTrace();
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
        // Convertir correctamente los bytes a String
        String msg = new String(dp.getData(), 0, dp.getLength()).trim();
        InetAddress clienteIP = dp.getAddress();
        int clientePuerto = dp.getPort();

        System.out.println("Servidor: Recibido '" + msg + "' de " + clienteIP.getHostAddress() + ":" + clientePuerto);

        if (msg.equals("Conexion")) {
            // Verificar si el cliente ya está conectado
            boolean yaConectado = false;
            for (DireccionRed cliente : clientesConectados) {
                if (cliente.getIp().equals(clienteIP) && cliente.getPuerto() == clientePuerto) {
                    yaConectado = true;
                    break;
                }
            }

            if (!yaConectado && clientesConectados.size() < MAX_CLIENTES) {
                // Agregar nuevo cliente
                clientesConectados.add(new DireccionRed(clienteIP, clientePuerto));
                enviarMensaje("OK", clienteIP, clientePuerto);
                System.out.println("Servidor: Cliente conectado. Total: " + clientesConectados.size() + "/" + MAX_CLIENTES);

                // Notificar a todos cuántos jugadores hay
                for (DireccionRed cliente : clientesConectados) {
                    enviarMensaje("ESPERANDO:" + clientesConectados.size(), cliente.getIp(), cliente.getPuerto());
                }

                // Si hay 2 clientes, iniciar partida
                if (clientesConectados.size() == MAX_CLIENTES) {
                    System.out.println("Servidor: ¡Iniciando partida con 2 jugadores!");
                    for (DireccionRed cliente : clientesConectados) {
                        enviarMensaje("INICIAR", cliente.getIp(), cliente.getPuerto());
                    }
                }
            } else if (yaConectado) {
                // Reenviar OK si ya está conectado (por si se perdió el paquete)
                enviarMensaje("OK", clienteIP, clientePuerto);
            }
        } else if (msg.equals("Desconexion")) {
            // Remover cliente
            clientesConectados.removeIf(c -> c.getIp().equals(clienteIP) && c.getPuerto() == clientePuerto);
            System.out.println("Servidor: Cliente desconectado. Total: " + clientesConectados.size());
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
            try {
                conexion.close();
                System.out.println("Servidor: Socket cerrado correctamente");
            } catch (Exception e) {
                System.err.println("Servidor: Error al cerrar socket - " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
