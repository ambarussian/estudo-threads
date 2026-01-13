import java.util.Random;
import java.util.concurrent.Semaphore;

public class Cliente implements Runnable {
    private final int id;
    private final int numRodadas; 
    private final Garcom meuGarcom;
    private final Random rand = new Random();
    private final Semaphore semEntrega = new Semaphore(0);

    public Cliente(int id, int numRodadas, Garcom garcom) {
        this.id = id;
        this.numRodadas = numRodadas;
        this.meuGarcom = garcom;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < numRodadas; i++) {
                
                Thread.sleep(500 + rand.nextInt(1500));

                boolean querPedir = rand.nextInt(10) < 7;

                if (querPedir) {
                    System.out.printf("[Cliente %d] Quer fazer um pedido (Rodada %d/%d). Chamando garcom.\n", id, i + 1, numRodadas);
                    
                    meuGarcom.reportar(this, true); 
                    
                    semEntrega.acquire();
                    
                    System.out.printf("[Cliente %d] Pedido recebido! Consumindo...\n", id);
                    Thread.sleep(1000 + rand.nextInt(2000));
                    
                } else {
                    System.out.printf("[Cliente %d] Satisfeito (Rodada %d/%d). Nao vai pedir.\n", id, i + 1, numRodadas);
                    meuGarcom.reportar(this, false); 
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try {
            System.out.printf("[Cliente %d] Indo embora.\n", id);
            meuGarcom.clienteSaindo(this);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void receberPedido() {
        semEntrega.release();
    }
}