package com.dojan.infiernoperfecto;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.dojan.infiernoperfecto.pantallas.PantallaCreditos;
import com.dojan.infiernoperfecto.serverred.HiloServidor;
import com.dojan.infiernoperfecto.utiles.Render;


/** {@link ApplicationListener} implementation shared by all platforms. */
public class InfiernoPerfecto extends Game {
    private HiloServidor hiloServidor;

    @Override
    public void create() {
        Render.app = this;
        Render.batch = new SpriteBatch();
        
        // Iniciar el servidor de red en un hilo separado
        System.out.println("=================================");
        System.out.println("   SERVIDOR - INFIERNO PERFECTO  ");
        System.out.println("=================================");
        System.out.println("Iniciando servidor en puerto 6666...");
        
        hiloServidor = new HiloServidor();
        hiloServidor.start();
        
        System.out.println("✓ Servidor iniciado correctamente.");
        System.out.println("✓ Esperando conexiones de clientes...");
        
        this.setScreen(new PantallaCreditos()); // PantallaMenu debe ser reemplazado por PantallaCreditos si se desea iniciar en la pantalla de créditos
    }

    @Override
    public void render() {
        super.render();

//        Gdx.gl.glClearColor(136f / 255f, 0f, 21f / 255f, 1f);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    }

    private void update(){

    }

    @Override
    public void dispose() {
        // Detener servidor de red
        if (hiloServidor != null) {
            hiloServidor.detener();
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
