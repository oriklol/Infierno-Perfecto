package com.dojan.infiernoperfecto.comandos;

public class ComandoAtacar implements ComandoJugador {
    private int enemigoIndex;
    private int ataqueIndex;

    public ComandoAtacar(int enemigoIndex, int ataqueIndex) {
        this.enemigoIndex = enemigoIndex;
        this.ataqueIndex = ataqueIndex;
    }

    @Override
    public String getTipo() {
        return "ATACAR";
    }

    public int getAtaqueIndex() {
        return ataqueIndex;
    }

    public int getEnemigoIndex() {
        return enemigoIndex;
    }
}
