public class ResultadoCombate {
    private boolean valido;
    private String mensajeError;

    private float danio;
    private String efectoMensaje;
    private boolean objetivoMurio;
    private float vidaObjetivoRestante;
    private int feAtacanteRestante;

    public static ResultadoCombate invalido(String mensaje) {
        ResultadoCombate r = new ResultadoCombate();
        r.valido = false;
        r.mensajeError = mensaje;
        return r;
    }

    public ResultadoCombate(float danio, String efectoMensaje, boolean objetivoMurio,
                            float vidaObjetivoRestante, int feAtacanteRestante) {
        this.valido = true;
        this.danio = danio;
        this.efectoMensaje = efectoMensaje;
        this.objetivoMurio = objetivoMurio;
        this.vidaObjetivoRestante = vidaObjetivoRestante;
        this.feAtacanteRestante = feAtacanteRestante;
    }

    public float getDanio(){
        return danio;
    }

    public String getEfectoMensaje() {
        return efectoMensaje;
    }

    public float getVidaObjetivoRestante() {
        return vidaObjetivoRestante;
    }

    public int getFeAtacanteRestante() {
        return feAtacanteRestante;
    }

    public String getMensajeError() {
        return mensajeError;
    }
}
