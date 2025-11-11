package com.dojan.infiernoperfecto.logica;

import com.dojan.infiernoperfecto.entidades.Personaje;
import com.dojan.infiernoperfecto.items.ItemCura;

public class ServicioTienda {

    public ResultadoCompra procesarCompra(Personaje jugador, ItemCura item) {
        // Validar dinero
        if (jugador.getMonedasActual() < item.getPrecio()) {
            return ResultadoCompra.error("Dinero insuficiente");
        }

        // Validar vida (si ya está lleno no tiene sentido comprar)
        if (jugador.getVidaActual() >= jugador.getVidaBase()) {
            return ResultadoCompra.error("Ya tienes vida completa");
        }

        // Aplicar compra
        jugador.comprar(item);

        return ResultadoCompra.exito(
            jugador.getVidaActual(),
            jugador.getMonedasActual()
        );
    }
}
