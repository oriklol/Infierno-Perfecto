package com.dojan.infiernoperfecto.pantallas.enciclopedia;

public enum ClaseEnciclopedia {
    PELEADOR("Peleador", "Un guerrero que lucha con espada y valentía"),
    MAGICO("Mago", "Un hechicero que domina los elementos"),
    ESCUDADOR("Tanque", "Un defensor que protege a sus aliados"),
    SOPORTE("Soporte", "Un sacerdote que cura y bendice");

    private final String nombre;
    private final String descripcion;

    ClaseEnciclopedia(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
}
