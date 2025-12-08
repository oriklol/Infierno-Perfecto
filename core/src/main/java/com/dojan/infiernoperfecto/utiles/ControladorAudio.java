package com.dojan.infiernoperfecto.utiles;

import com.dojan.infiernoperfecto.elementos.Musica;

public class ControladorAudio {

    // volumen inicial de ambos en 5 (de 0 a 10)
    public static int volumenMusica = 5;
    public static int volumenSFX = 5;

    // musica actualmente en reproduccion
    private static Musica musicaActual;

    // para evitar reiniciar la musica al cambiar de pantalla si ya se estaba reproduciendo
    private static boolean yaIniciada = false;

    // establece la musica actual solo si no hay ninguna ya iniciada
    public static void setMusicaActual(Musica musica) {
        if (!yaIniciada){
            musicaActual = musica;
            musicaActual.setLoopOn();
            musicaActual.setVolume(getVolumenMusica());
            yaIniciada=true;
        }
    }

    // actualiza el volumen de la musica actual
    public static void actualizarVolumenMusica() {
        if (musicaActual != null) {
            musicaActual.setVolume(getVolumenMusica());
        }
    }

    public static float getVolumenMusica() {
        return volumenMusica / 10f;
    }

    public static float getVolumenSFX() {
        return volumenSFX / 10f;
    }

    // ciclos de volumen entre 0 y 10
    public static void cicloVolumenMusica() {
        volumenMusica = (volumenMusica + 1) % 11;
        actualizarVolumenMusica(); // <- importante
    }

    public static void cicloVolumenSFX() {
        volumenSFX = (volumenSFX + 1) % 11;
    }

    // reproduce la musica actual si no se esta reproduciendo
    public static void reproducirMusica() {
        if (!musicaActual.isPlaying()) musicaActual.play();
    }

    // detiene la musica actual si se esta reproduciendo
    public static void pararMusica() {
        if (musicaActual.isPlaying()){
            musicaActual.stop();
            yaIniciada=false;
        }
    }

    public static void dispose(){
        if (musicaActual != null){
            try{
                musicaActual.dispose();
            }catch(Exception e){
                // ignore
            }
            musicaActual = null;
            yaIniciada = false;
        }
    }
}
