public class ResultadoCompra {
    private boolean exitoso;
    private String mensajeError;
    private float vidaNueva;
    private int monedasRestantes;

    public static ResultadoCompra error(String mensaje) {
        ResultadoCompra r = new ResultadoCompra();
        r.exitoso = false;
        r.mensajeError = mensaje;
        return r;
    }

    public static ResultadoCompra exito(float vidaNueva, int monedasRestantes) {
        ResultadoCompra r = new ResultadoCompra();
        r.exitoso = true;
        r.vidaNueva = vidaNueva;
        r.monedasRestantes = monedasRestantes;
        return r;
    }

    public String getMensajeError() {
        return mensajeError;
    }

    public float getVidaNueva() {
        return vidaNueva;
    }

    public int getMonedasRestantes() {
        return monedasRestantes;
    }
}
