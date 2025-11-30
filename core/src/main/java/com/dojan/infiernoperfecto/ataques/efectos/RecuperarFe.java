package com.dojan.infiernoperfecto.ataques.efectos;

import com.dojan.infiernoperfecto.entidades.Personaje;
import com.dojan.infiernoperfecto.utiles.Random;

public class RecuperarFe extends EfectoSecundario {

    public RecuperarFe(int cantidadMin, int cantidadMax) {
        super(TipoEfecto.RECUPERAR_FE, cantidadMin, cantidadMax, 100);
    }

    @Override
    public String aplicar(Personaje objetivo) {
        int cantidadFe = Random.generarEntero(porcentajeMin, porcentajeMax);
        int feAntes = objetivo.getFeActual();
        objetivo.recuperarFe(cantidadFe);
        int feDespues = objetivo.getFeActual();
        int feRecuperada = feDespues - feAntes;

        return objetivo.getNombre() + " recuperó " + feRecuperada + " Fe";
    }
}
