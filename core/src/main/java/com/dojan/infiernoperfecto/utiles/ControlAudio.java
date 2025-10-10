package com.dojan.infiernoperfecto.utiles;

import com.dojan.infiernoperfecto.elementos.Musica;

public class ControlAudio {

    public static int volumenMusica = 5;
    public static int volumenSFX = 5;

    private static Musica musicaActual;

    private static boolean yaIniciada = false;

    public static void setMusicaActual(Musica musica) {
        if (!yaIniciada){
            musicaActual = musica;
            musicaActual.setLoopOn();
            musicaActual.setVolume(getVolumenMusica());
            yaIniciada=true;
        }
    }

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

    public static void cicloVolumenMusica() {
        volumenMusica = (volumenMusica + 1) % 11;
        actualizarVolumenMusica(); // <- importante
    }

    public static void cicloVolumenSFX() {
        volumenSFX = (volumenSFX + 1) % 11;
    }

    public static void reproducirMusica() {
        if (!musicaActual.isPlaying()) musicaActual.play();
    }

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
