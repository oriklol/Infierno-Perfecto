package com.dojan.infiernoperfecto.ataques.efectos;

import com.dojan.infiernoperfecto.entidades.Personaje;
import com.dojan.infiernoperfecto.utiles.Random;

public class Curacion extends EfectoSecundario {

    public Curacion(int cantidadMin, int cantidadMax) {
        super(TipoEfecto.CURACION, cantidadMin, cantidadMax, 100);
    }

    public Curacion(int cantidadMin, int cantidadMax, int probabilidad) {
        super(TipoEfecto.CURACION, cantidadMin, cantidadMax, probabilidad);
    }

    @Override
    public String aplicar(Personaje objetivo) {
        int cantidadCura = Random.generarEntero(porcentajeMin, porcentajeMax);
        float vidaAntes = objetivo.getVidaActual();
        objetivo.curar(cantidadCura);
        float vidaDespues = objetivo.getVidaActual();
        float curaReal = vidaDespues - vidaAntes;

        return objetivo.getNombre() + " recuperó " + (int)curaReal + " HP";
    }
}
