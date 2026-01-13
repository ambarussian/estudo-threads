public class Reporte {
    private final Cliente cliente;
    private final boolean querPedir;
    private final Pedido pedido;

    public Reporte(Cliente cliente, boolean querPedir) {
        this.cliente = cliente;
        this.querPedir = querPedir;
        if (querPedir) {
            this.pedido = new Pedido(cliente.getId());
        } else {
            this.pedido = null;
        }
    }

    public Cliente getCliente() { return cliente; }
    public boolean querPedir() { return querPedir; }
    public Pedido getPedido() { return pedido; }
}