import java.util.Random;

public class Cliente extends Thread {
    private final int idCliente;
    private final Barbearia barbearia;
    private final Random rand = new Random();

    public Cliente(int id, Barbearia barbearia) {
        this.idCliente = id;
        this.barbearia = barbearia;
    }

    @Override
    public void run() {
        try {
            
            int tempoChegada = 1000 + rand.nextInt(5000);
            Thread.sleep(tempoChegada);

            barbearia.tentarCortarCabelo(idCliente);
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Cliente " + idCliente + " foi interrompido.");
        }
    }
}