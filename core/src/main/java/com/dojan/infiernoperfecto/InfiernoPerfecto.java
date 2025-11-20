package com.dojan.infiernoperfecto;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dojan.infiernoperfecto.pantallas.PantallaMenu;
import com.dojan.infiernoperfecto.serverred.HiloServidor;
import com.dojan.infiernoperfecto.utiles.Render;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class InfiernoPerfecto extends Game {
    private HiloServidor hiloServidor;

    @Override
    public void create() {
        Render.app = this;
        Render.batch = new SpriteBatch();

        // ✅ CAMBIO: No iniciar el servidor automáticamente
        // Se iniciará cuando el usuario presione "Iniciar Servidor" en el menú
        System.out.println("=================================");
        System.out.println("   SERVIDOR - INFIERNO PERFECTO  ");
        System.out.println("=================================");
        System.out.println("Esperando que se inicie el servidor...");

        this.setScreen(new PantallaMenu());
    }

    @Override
    public void render() {
        super.render();

//        Gdx.gl.glClearColor(136f / 255f, 0f, 21f / 255f, 1f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    // ✅ NUEVO: Método para iniciar el servidor desde el menú
    public void iniciarServidor() {
        if (hiloServidor == null || !hiloServidor.isAlive()) {
            System.out.println("=================================");
            System.out.println("Iniciando servidor en puerto 6666...");
            System.out.println("=================================");

            hiloServidor = new HiloServidor();
            hiloServidor.start();

            System.out.println("✓ Servidor iniciado correctamente.");
            System.out.println("✓ Esperando conexiones de clientes...");
        } else {
            System.out.println("El servidor ya está en ejecución.");
        }
    }

    // ✅ NUEVO: Método para verificar si el servidor está en ejecución
    public boolean isServidorActivo() {
        return hiloServidor != null && hiloServidor.isAlive();
    }

    private void update(){

    }

    @Override
    public void dispose() {
        // ✅ Detener servidor de red correctamente
        if (hiloServidor != null) {
            try {
                hiloServidor.detener();
                // Dar tiempo para que el hilo se cierre
                hiloServidor.join(2000);
                System.out.println("Servidor: HiloServidor detenido");
            } catch (InterruptedException e) {
                System.err.println("Servidor: Error al esperar detencion de HiloServidor - " + e.getMessage());
                Thread.currentThread().interrupt();
            }
            hiloServidor = null;
        }

        // dispose global rendering resources
        try{
            if (Render.batch != null) {
                Render.batch.dispose();
                Render.batch = null;
            }
            if (Render.renderer != null) {
                Render.renderer.dispose();
                Render.renderer = null;
            }
        }catch(Exception e){
            // ignore
        }

        // dispose current screen (if set)
        if (getScreen() != null){
            try{ getScreen().dispose(); }catch(Exception e){ }
        }

        // dispose audio control
        com.dojan.infiernoperfecto.utiles.ControlAudio.dispose();
    }
}
