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
        private static final int PUERTO_SERVIDOR = 6666;  // Puerto donde escucha el servidor
        private static final int PUERTO_CLIENTE = 6667;    // ✅ Puerto donde escuchan los clientes

        public HiloServidor() {
            this.setDaemon(true);
            try {
                conexion = new DatagramSocket(PUERTO_SERVIDOR);
                conexion.setBroadcast(true); // ✅ CRUCIAL para broadcast
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

        // ✅ Enviar BROADCAST a todos los clientes (puerto 6667)
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

        // ✅ Método que envía a todos usando broadcast Y unicast (para compatibilidad)
        private void enviarATodos(String msg) {
            System.out.println("Servidor: Enviando a todos los clientes: '" + msg + "'");

            // Enviar por broadcast (para clientes en puerto 6667)
            enviarBroadcast(msg);

            // TAMBIÉN enviar por unicast a cada cliente conocido
            // (esto funciona incluso si usan puertos aleatorios)
            for (DireccionRed cliente : clientesConectados) {
                enviarUnicast(msg, cliente.getIp(), cliente.getPuerto());
            }
        }

        private long ultimoHeartbeat = System.currentTimeMillis();
        private static final long INTERVALO_HEARTBEAT = 3000; // Enviar heartbeat cada 3 segundos

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

                // ✅ Enviar heartbeat periódico a todos los clientes conectados
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
                // ✅ NUEVO: Manejar desconexión
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

                // Responder SOLO a este cliente por UNICAST
                enviarUnicast("OK", clienteIP, clientePuerto);

                System.out.println("Servidor: ✅ Cliente conectado. Total: " + clientesConectados.size() + "/" + MAX_CLIENTES);

                // Notificar a TODOS los clientes el nuevo estado por BROADCAST
                enviarATodos("ESPERANDO:" + clientesConectados.size());

                if (clientesConectados.size() == MAX_CLIENTES) {
                    System.out.println("Servidor: ========================================");
                    System.out.println("Servidor: ¡¡¡INICIANDO PARTIDA CON 2 JUGADORES!!!");
                    System.out.println("Servidor: ========================================");

                    // Enviar INICIAR a todos los clientes varias veces
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

        // ✅ NUEVO: Método para manejar desconexión de clientes
        private void manejarDesconexion(InetAddress clienteIP, int clientePuerto) {
            DireccionRed clienteAEliminar = null;

            // Buscar el cliente en la lista
            for (DireccionRed cliente : clientesConectados) {
                if (cliente.getIp().equals(clienteIP) && cliente.getPuerto() == clientePuerto) {
                    clienteAEliminar = cliente;
                    break;
                }
            }

            // Si se encontró, eliminarlo
            if (clienteAEliminar != null) {
                clientesConectados.remove(clienteAEliminar);
                System.out.println("Servidor: ❌ Cliente desconectado. Total: " + clientesConectados.size() + "/" + MAX_CLIENTES);

                // Confirmar desconexión al cliente
                enviarUnicast("DESCONECTADO", clienteIP, clientePuerto);

                // Notificar a TODOS los clientes restantes el nuevo estado
                if (clientesConectados.size() > 0) {
                    enviarATodos("ESPERANDO:" + clientesConectados.size());
                }
            } else {
                System.out.println("Servidor: ⚠️ Cliente no encontrado para desconectar");
            }
        }

        public int getCantidadClientes() {
            return clientesConectados.size();
        }

        public void detener() {
            // ✅ Notificar a los clientes ANTES de cerrar
            System.out.println("Servidor: Enviando notificación de cierre a todos los clientes...");
            for (int i = 0; i < 3; i++) {
                enviarATodos("SERVIDOR_CERRANDO");
                try {
                    Thread.sleep(100); // Pequeña pausa para asegurar envío
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
