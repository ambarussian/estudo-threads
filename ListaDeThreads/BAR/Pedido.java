public class Pedido {
    private final int idCliente;
    private final String item;

    public Pedido(int idCliente) {
        this.idCliente = idCliente;
        String[] itens = {"Cerveja", "Caipirinha", "Agua", "Petisco"};
        this.item = itens[new java.util.Random().nextInt(itens.length)];
    }

    @Override
    public String toString() {
        return String.format("Pedido (Cliente %d: %s)", idCliente, item);
    }
}