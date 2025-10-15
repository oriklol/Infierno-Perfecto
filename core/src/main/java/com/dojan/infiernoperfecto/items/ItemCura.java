package com.dojan.infiernoperfecto.items;

import com.dojan.infiernoperfecto.elementos.Imagen;

public class ItemCura {
    private int cantidadCura;
    private int precio;
    private String nombre;
    private Imagen imagen;

    public ItemCura(int cantidadCura, int precio, String nombre, String rutaTextura) {
        this.cantidadCura = cantidadCura;
        this.precio = precio;
        this.nombre = nombre;
        this.imagen = new Imagen(rutaTextura);
    }

    public int getCantidadCura() {
        return cantidadCura;
    }

    public void setCantidadCura(int cantidadCura) {
        this.cantidadCura = cantidadCura;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setPosition(int x, int y) {
        imagen.setPosition(x, y);
    }

    public void dibujar() {
        imagen.dibujar();
    }

    public int getAlto() {
        return (int) imagen.getAlto();
    }

    public int getAncho() {
        return (int) imagen.getAncho();
    }

    public int getX() {
        return (int) imagen.getX();
    }

    public int getY() {
        return (int) imagen.getY();
    }


    public void usar() {

    }

    public void dispose() {
        if (imagen != null) {
            imagen.dispose();
        }
    }
}
