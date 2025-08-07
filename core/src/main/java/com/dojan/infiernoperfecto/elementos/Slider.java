package com.dojan.infiernoperfecto.elementos;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.Entradas;
import com.badlogic.gdx.graphics.Color;

import java.awt.*;

public class Slider {
    private float x, y;
    private float width, height;
    private float valor;
    private boolean arrastrando = false;
    private boolean adentro = false;
    Entradas entradas = new Entradas();

    public Slider(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.valor = 0.5f;

    }

    public void actualizar(){
        if(entradas.isClick()){
            adentro = entradas.getMouseX() >= x && entradas.getMouseX()<= x + width && entradas.getMouseY() >= y && entradas.getMouseY() <= y +height;

        }
        if(adentro || arrastrando){
            arrastrando = true;
            valor = (entradas.getMouseX() - x) / width;
            valor = Math.max(0, Math.min(1, valor));
        }else{
            arrastrando = false;
        }
    }

    public void dibujar(ShapeRenderer shape){
        // Fondo del slider
        shape.setColor(Color.WHITE);
        shape.rect(x, y, width, height);


        // Valor actual
        shape.setColor(Color.GREEN);
        shape.rect(x, y, valor * width, height);

        // "Perilla"
        shape.setColor(Color.WHITE);
        shape.rect(x + valor * width - 5, y - 5, 10, height + 10);
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = Math.max(0, Math.min(1, valor));
    }

}
