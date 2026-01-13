public class Main {
    public static void main(String[] args) {
       
        int NUM_CADEIRAS_ESPERA = 5; 
        int TOTAL_CLIENTES = 15;     

        Barbearia barbearia = new Barbearia(NUM_CADEIRAS_ESPERA);

        Barbeiro barbeiro = new Barbeiro(barbearia);
        barbeiro.start();

        for (int i = 1; i <= TOTAL_CLIENTES; i++) {
            Cliente cliente = new Cliente(i, barbearia);
            cliente.start();
        }
    }
    
}
