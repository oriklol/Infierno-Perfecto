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
    private static final int PUERTO_SERVIDOR = 6666;
    private static final int PUERTO_CLIENTE = 6667;
    private boolean partidaIniciada = false;

    public HiloServidor() {
        this.setDaemon(true);
        try {
            conexion = new DatagramSocket(PUERTO_SERVIDOR);
            conexion.setBroadcast(true);
            conexion.setSoTimeout(1000);
            System.out.println("Servidor: Iniciado en puerto " + PUERTO_SERVIDOR);
            System.out.println("Servidor: Esperando conexiones...");
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

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

    private void enviarBroadcast(String msg) {
        try {
            byte[] mensaje = msg.getBytes();
            InetAddress broadcast = InetAddress.getByName("255.255.255.255");
            DatagramPacket dp = new DatagramPacket(mensaje, mensaje.length, broadcast, PUERTO_CLIENTE);
            conexion.send(dp);
            System.out.println("Servidor: 📡 BROADCAST '" + msg + "' enviado a todos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void enviarATodos(String msg) {
        System.out.println("Servidor: Enviando a todos los clientes: '" + msg + "'");
        enviarBroadcast(msg);
        for (DireccionRed cliente : clientesConectados) {
            enviarUnicast(msg, cliente.getIp(), cliente.getPuerto());
        }
    }


    private void enviarAlOtroCliente(String msg, InetAddress ipExcluir, int puertoExcluir) {
        System.out.println("Servidor: Enviando al otro cliente: '" + msg + "'");
        System.out.println("Servidor: Cliente a EXCLUIR: " + ipExcluir.getHostAddress() + ":" + puertoExcluir);
        System.out.println("Servidor: Total clientes en lista: " + clientesConectados.size());

        for (DireccionRed cliente : clientesConectados) {
            System.out.println("Servidor: Evaluando cliente: " + cliente.getIp().getHostAddress() + ":" + cliente.getPuerto());

            boolean esElClienteQueSeDesconecto = cliente.getIp().equals(ipExcluir) && cliente.getPuerto() == puertoExcluir;

            System.out.println("Servidor: ¿Es el que se desconectó? " + esElClienteQueSeDesconecto);

            if (!esElClienteQueSeDesconecto) {
                enviarUnicast(msg, cliente.getIp(), cliente.getPuerto());
                System.out.println("Servidor: ✅ Mensaje enviado al cliente restante");
            } else {
                System.out.println("Servidor: ❌ Cliente excluido (el que se desconectó)");
            }
        }
    }

    private long ultimoHeartbeat = System.currentTimeMillis();
    private static final long INTERVALO_HEARTBEAT = 5000; // originalmente estaba en 3000; lo subo a 5000 para mayor tolerancia

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

            // Enviar heartbeat periódico
            if (System.currentTimeMillis() - ultimoHeartbeat > INTERVALO_HEARTBEAT) {
                if (clientesConectados.size() > 0) {
                    enviarATodos("HEARTBEAT");
                }
                ultimoHeartbeat = System.currentTimeMillis();
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
            manejarConexion(clienteIP, clientePuerto);

        } else if (msg.equals("Desconexion")) {
            manejarDesconexion(clienteIP, clientePuerto);
        }
    }

    private void manejarConexion(InetAddress clienteIP, int clientePuerto) {
        boolean yaConectado = false;
        for (DireccionRed cliente : clientesConectados) {
            if (cliente.getIp().equals(clienteIP) && cliente.getPuerto() == clientePuerto) {
                yaConectado = true;
                break;
            }
        }

        if (!yaConectado && clientesConectados.size() < MAX_CLIENTES) {
            clientesConectados.add(new DireccionRed(clienteIP, clientePuerto));
            enviarUnicast("OK", clienteIP, clientePuerto);
            System.out.println("Servidor: ✅ Cliente conectado. Total: " + clientesConectados.size() + "/" + MAX_CLIENTES);
            enviarATodos("ESPERANDO:" + clientesConectados.size());

            if (clientesConectados.size() == MAX_CLIENTES) {
                System.out.println("Servidor: ========================================");
                System.out.println("Servidor: ¡¡¡INICIANDO PARTIDA CON 2 JUGADORES!!!");
                System.out.println("Servidor: ========================================");
                partidaIniciada = true; // ✅ Marcar que la partida empezó

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
            enviarUnicast("OK", clienteIP, clientePuerto);
        } else {
            enviarUnicast("SERVIDOR_LLENO", clienteIP, clientePuerto);
        }
    }

    private void manejarDesconexion(InetAddress clienteIP, int clientePuerto) {
        DireccionRed clienteAEliminar = null;

        for (DireccionRed cliente : clientesConectados) {
            if (cliente.getIp().equals(clienteIP) && cliente.getPuerto() == clientePuerto) {
                clienteAEliminar = cliente;
                break;
            }
        }

        if (clienteAEliminar != null) {
            // ✅ SI LA PARTIDA YA HABÍA INICIADO
            if (partidaIniciada && clientesConectados.size() == 2) {
                System.out.println("Servidor: 🚨 JUGADOR SE DESCONECTÓ DURANTE LA PARTIDA");
                System.out.println("Servidor: Notificando al otro jugador y cerrando la partida...");

                // Enviar mensaje al otro cliente
                for (int i = 0; i < 3; i++) {
                    enviarAlOtroCliente("COMPANIERO_DESCONECTADO", clienteIP, clientePuerto);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // ✅ LIMPIAR COMPLETAMENTE - Desconectar a AMBOS clientes
                System.out.println("Servidor: Limpiando todos los clientes de la partida...");
                clientesConectados.clear(); // Eliminar TODOS los clientes
                partidaIniciada = false;
                System.out.println("Servidor: ✅ Partida terminada. Total clientes: " + clientesConectados.size() + "/2");

            } else {
                // Si no había iniciado la partida, solo eliminar el cliente normal
                clientesConectados.remove(clienteAEliminar);
                System.out.println("Servidor: ❌ Cliente desconectado. Total: " + clientesConectados.size() + "/" + MAX_CLIENTES);

                // Confirmar desconexión al cliente que se va
                enviarUnicast("DESCONECTADO", clienteIP, clientePuerto);

                // Actualizar contadores para el otro cliente
                if (clientesConectados.size() > 0) {
                    enviarATodos("ESPERANDO:" + clientesConectados.size());
                }
            }
        } else {
            System.out.println("Servidor: ⚠️ Cliente no encontrado para desconectar");
        }
    }

    public int getCantidadClientes() {
        return clientesConectados.size();
    }

    public void detener() {
        System.out.println("Servidor: Enviando notificación de cierre a todos los clientes...");
        for (int i = 0; i < 3; i++) {
            enviarATodos("SERVIDOR_CERRANDO");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        fin = true;
    }

    private void cerrarConexion() {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
        System.out.println("Servidor: Socket cerrado correctamente");
    }
}
