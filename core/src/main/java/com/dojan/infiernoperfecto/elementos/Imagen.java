package com.dojan.infiernoperfecto.elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import static com.dojan.infiernoperfecto.utiles.Render.batch;

public class Imagen {
    private Texture t;
    private final Sprite s;

    public Imagen(String ruta){
        t = new Texture(ruta);
        s = new Sprite(t);
    }

    public void dibujar(){
        s.draw(batch);
    }

    public void setTransparencia(float a){
        s.setAlpha(a);
    }

    public void setSize(float ancho, float largo){
        s.setSize(ancho, largo);
    }

    public void setPosition(int x, int y){
        s.setPosition(x,y);
    }

    public float getX(){
        return s.getX();
    }

    public float getY(){
        return s.getY();
    }

    public float getAncho(){
        return s.getWidth();
    }

    public float getAlto(){
        return s.getHeight();
    }

    public void dispose(){
        if (t != null) {
            t.dispose();
            t = null;
        }
    }

}
