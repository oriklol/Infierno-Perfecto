package com.dojan.infiernoperfecto.enciclopedia;

import com.badlogic.gdx.graphics.Color;

public enum CategoriaEnciclopedia {
    ATAQUE_FISICO("Ataque Físico", Color.valueOf("FF6B6B")),
    ATAQUE_MAGICO("Ataque Mágico",  Color.valueOf("4ECDC4")),
    ATAQUE_SAGRADO("Ataque Sagrado",  Color.valueOf("FFD93D")),
    ATAQUE_BUFF("Mejora",  Color.valueOf("95E1D3")),
    ATAQUE_DEBUFF("Debilitamiento",  Color.valueOf("F38181")),
    ATAQUE_CURACION("Curación",  Color.valueOf("A8E6CF")),
    ATAQUE_ESPECIAL("Especial",  Color.valueOf("C7CEEA")),
    ITEM_CONSUMIBLE("Item Consumible",  Color.valueOf("FDCB82")),
    ENEMIGO_NORMAL("Enemigo Normal",  Color.valueOf("B4B4B4")),
    ENEMIGO_JEFE("Jefe",  Color.valueOf("8B0000"));

    private final String nombre;
    private final Color color;

    CategoriaEnciclopedia(String nombre, Color color) {
        this.nombre = nombre;
        this.color = color;
    }

    public String getNombre() { return nombre; }
    public Color getColor() { return color; }


}
