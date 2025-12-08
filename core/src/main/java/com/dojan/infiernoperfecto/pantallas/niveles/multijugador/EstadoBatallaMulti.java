package com.dojan.infiernoperfecto.pantallas.niveles.multijugador;

public enum EstadoBatallaMulti {
    ESPERANDO_CONEXION,
    ESPERANDO_JUGADORES,
    ESPERANDO_DATOS_BATALLA,
    SELECCION_ENEMIGO,
    SELECCION_ATAQUE,
    ESPERANDO_OTRO_JUGADOR,
    RESULTADOS_COMBATE,
    FIN_BATALLA,
    TIENDA // Nuevo estado para transición a tienda
}
