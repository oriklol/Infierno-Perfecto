package com.dojan.infiernoperfecto.elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class Musica {

    private final Music musica;


    public Musica(String ruta){
        this.musica = Gdx.audio.newMusic(Gdx.files.internal(ruta));
    }
    public void setLoopOn(){
        musica.setLooping(true);
    }

    public void setLoopOff(){
        musica.setLooping(false);
    }

    public void play(){
        musica.play();
    }

    public void stop(){
        musica.stop();
    }

    public void dispose(){
        musica.dispose();
    }

    public boolean isPlaying(){
        return musica.isPlaying();
    }

    public void setVolume(float volumenMusica) {
        this.musica.setVolume(volumenMusica);
    }
}
