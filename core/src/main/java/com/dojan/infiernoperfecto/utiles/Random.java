package com.dojan.infiernoperfecto.utiles;

public class Random {
    private final static java.util.Random r = new java.util.Random();

    private Random() {
    }

    // funcion que genera un entero entre dos numeros
    public static int generarEntero(int MIN, int MAX) {
        return r.nextInt(MAX - MIN + 1) + MIN;
    }

    // funcion que genera un entero detro de NRO
    public static int generarEntero(int NRO) {
        return r.nextInt(NRO);
    }

    // funcion que verifica si se acierta un porcentaje
    public static boolean verificarAcierto(int porcentaje) {
        int nroAleatorio = generarEntero(1, 100);
        return nroAleatorio <= porcentaje;
    }
}
