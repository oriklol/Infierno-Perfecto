package com.dojan.infiernoperfecto.utiles;

public class Random {
    private final static java.util.Random r = new java.util.Random();

    private Random() {
    }

    public static int generarEntero(int MIN, int MAX) {
        return r.nextInt(MAX - MIN + 1) + MIN;
    }

    public static int generarEntero(int NRO) {
        return r.nextInt(NRO);
    }

    public static boolean verificarAcierto(int porcentaje) {
        int nroAleatorio = generarEntero(1, 100);
        return nroAleatorio <= porcentaje;
    }
}
