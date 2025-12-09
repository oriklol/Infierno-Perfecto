package com.dojan.infiernoperfecto.utiles;

import com.dojan.infiernoperfecto.entidades.Personaje;

public class Config {

//    public static int[][] Resoluciones = {{800,600},{1280,720}};
//    private int indiceResolucion = 0;

    // Personaje seleccionado
    public static Personaje personajeSeleccionado;

    // Nivel y piso actuales
    public static int nivel=1;
    public static int piso = 1;

    // Resolución inicial
    public static int ANCHO = 800;
    public static int ALTO = 600;

    // Estado de la partida
    public static boolean empiezaPartida = false;

    // FASE 5.2: Flag para modo multijugador
    public static boolean esPartidaMultijugador = true;

    public static void resetearPartida() {
        nivel = 1;
        piso = 1;
        empiezaPartida = false;
        // NO resetear personajeSeleccionado aquí (se hace en PantallaHistoria)
        System.out.println("✅ Config reseteado: Nivel=" + nivel + ", Piso=" + piso);
    }

//    public void aumentarNivel() {
//        if (nivel<4){
//            nivel++;
//        }else {
//            nivel=1;
//        }
//    }

//    public void aumentarPiso(){
//        piso++;
//    }

//    public void modificarResolucion() {
//        this.indiceResolucion++;
//        if(this.indiceResolucion>= Resoluciones.length){
//            this.indiceResolucion = 0;
//        }
//        ANCHO = Resoluciones[indiceResolucion][0];
//        ALTO = Resoluciones[indiceResolucion][1];
//        System.out.println("Resolución cambiada a: " + ANCHO + "x" + ALTO);
//        Gdx.graphics.setWindowedMode(ANCHO,ALTO);
//    }

//    public int getIndiceResolucion() {
//        return indiceResolucion;
//    }
//
//    public void setIndiceResolucion(int i) {
//        this.indiceResolucion = i;
//    }
}
