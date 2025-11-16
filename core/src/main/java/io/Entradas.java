package io;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.dojan.infiernoperfecto.utiles.Config;

public class Entradas implements InputProcessor {
    private boolean arriba = false, abajo = false;
    private boolean enter = false;
    private boolean izquierda = false, derecha = false;
    private boolean esc = false;

    private int mouseX = 0, mouseY= 0;
    private boolean click = false;
//    private boolean cambioHecho = false;
    private boolean enterPresionado = false;
    private boolean enciclopedia;

    public Entradas(){

    }

    public boolean isAbajo() {
        return abajo;
    }

    public boolean isArriba() {
        return arriba;
    }

    public boolean isEnter() {
        return enter;
    }

    public boolean isClick() {
        return click;
    }

    public boolean isEnciclopedia() {
        return enciclopedia;
    }

    public boolean isDerecha() {
        return derecha;
    }

    public boolean isIzquierda() {
        return izquierda;
    }

    public boolean isEsc() {
        return esc;
    }

    public boolean isEnterPresionado() {
        if (enterPresionado) {
            enterPresionado = false; //
            return true;
        }
        return false;
    }
//    public boolean isCambioHecho() {
//        return cambioHecho;
//    }
//
//    public void setCambioHecho(boolean cambioHecho) {
//        this.cambioHecho = cambioHecho;
//    }

    @Override
    public boolean keyDown(int keycode) {

        //menu.setTiempo(0.8f);
        if(keycode == Keys.DOWN){
            this.abajo = true;
        }

        if (keycode == Keys.UP){
            this.arriba = true;
        }

        if (keycode == Keys.ENTER) {
            enter = true;
            enterPresionado = true;
        }

        if (keycode == Keys.LEFT){
            this.izquierda = true;
        }

        if (keycode == Keys.RIGHT){
            this.derecha = true;
        }

        if(keycode==Keys.ESCAPE){
            this.esc = true;
        }

        if (keycode==Keys.C){
            this.enciclopedia = true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Keys.DOWN){
            this.abajo = false;
        }

        if (keycode == Keys.UP){
            this.arriba = false;
        }

        if(keycode == Keys.ENTER){
            this.enter = false;
        }

        if (keycode == Keys.LEFT){
            this.izquierda = false;
        }

        if (keycode == Keys.RIGHT){
            this.derecha = false;
        }

        if(keycode==Keys.ESCAPE){
            this.esc = false;
        }

        if (keycode==Keys.C){
            this.enciclopedia = false;
        }

        return false;
    }

//    public void hacerCambio() {
//        if ((Gdx.input.isKeyJustPressed(Keys.ENTER) &&Gdx.input.justTouched())) {
//            cambioHecho = true;
//        }
//        if (!Gdx.input.isKeyPressed(Keys.ENTER) &&!Gdx.input.isTouched()) {
//            cambioHecho = false;
//        }
//    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }


    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        click = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        click = false;
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseX = screenX;
        mouseY = Config.ALTO - screenY;
        return false;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
