package com.dojan.infiernoperfecto.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Vector2;
import static com.dojan.infiernoperfecto.utiles.Render.batch;

public class Texto {
    BitmapFont fuente;
    private int x=0, y=0;
    private String texto;


    GlyphLayout layout;

    public Texto(String rutaFuente, int tamanio, Color color, boolean sombra){
        FreeTypeFontGenerator generador = new FreeTypeFontGenerator(Gdx.files.internal(rutaFuente));
        FreeTypeFontParameter parametros = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parametros.size = tamanio;
        parametros.color = color;
        if(sombra){
            parametros.shadowColor = Color.BLACK;
            parametros.shadowOffsetX = 2;
            parametros.shadowOffsetY = 5;
        }
        fuente = generador.generateFont(parametros);
        layout = new GlyphLayout();

    // free the generator, font keeps the data it needs
    generador.dispose();

    }


    public void dibujar(){
        fuente.draw(batch, this.texto, this.x,this.y);
    }

    public float getAncho(){
        return layout.width;
    }

    public float getAlto(){
        return layout.height;
    }

    public Vector2 getDimension(){
        return new Vector2(layout.width,layout.height);
    }

    public Vector2 getPosition(){
        return new Vector2(x,y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setTexto(String texto) {
        this.texto = texto;
        layout.setText(fuente, texto);
    }

    public void setPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    public void setX(int x){
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setColor(Color color){
        fuente.setColor(color);
    }

    public void dispose(){
        if (fuente != null) {
            fuente.dispose();
            fuente = null;
        }
    }
}
