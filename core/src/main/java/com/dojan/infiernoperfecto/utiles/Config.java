package com.dojan.infiernoperfecto.utiles;

import com.badlogic.gdx.Gdx;
import com.dojan.infiernoperfecto.entidades.Personaje;

public class Config {

    public static int[][] Resoluciones = {{800,600},{1280,720}};
    private int indiceResolucion = 0;

    public static Personaje personajeSeleccionado;
    public static int nivel=1;
    public static int piso = 1;

    public static int ANCHO = 800;
    public static int ALTO = 600;

    public void aumentarNivel() {
        if (nivel<4){
            nivel++;
        }else {
            nivel=1;
        }
    }

    public void aumentarPiso(){
        piso++;
    }

    public void modificarResolucion() {
        this.indiceResolucion++;
        if(this.indiceResolucion>= Resoluciones.length){
            this.indiceResolucion = 0;
        }
        ANCHO = Resoluciones[indiceResolucion][0];
        ALTO = Resoluciones[indiceResolucion][1];
        System.out.println("Resolución cambiada a: " + ANCHO + "x" + ALTO);
        Gdx.graphics.setWindowedMode(ANCHO,ALTO);
    }

    public int getIndiceResolucion() {
        return indiceResolucion;
    }

    public void setIndiceResolucion(int i) {
        this.indiceResolucion = i;
    }
}
