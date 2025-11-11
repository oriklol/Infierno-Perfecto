public class ComandoComprar implements ComandoJugador {
    private int itemIndex;

    public ComandoComprar(int itemIndex) {
        this.itemIndex = itemIndex;
    }

    @Override
    public String getTipo() {
        return "COMPRAR";
    }

    public int getItemIndex() {
        return itemIndex;
    }
}
