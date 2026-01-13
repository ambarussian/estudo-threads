
import java.util.List;
import java.util.Random;
import java.util.concurrent.Semaphore;


public class Copa {
    
    private final Semaphore bartender;
    private final Random rand = new Random();

    public Copa() {
        this.bartender = new Semaphore(1);
    }

    
    public void fazerPedidos(int idGarcom, List<Pedido> pedidos) throws InterruptedException {
        bartender.acquire();
        
        try {
            System.out.printf(">> [Bartender] Atendendo Garçom %d (Pedidos: %d)\n", idGarcom, pedidos.size());

            Thread.sleep(500 + rand.nextInt(500 * pedidos.size()));
            
            System.out.printf("<< [Bartender] Pedidos do Garçom %d prontos.\n", idGarcom);
            
        } finally {
            bartender.release();
        }
    }
}