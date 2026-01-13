import java.util.Random;

public class Barbeiro extends Thread {
    private final Barbearia barbearia;
    private final Random rand = new Random();

    public Barbeiro(Barbearia barbearia) {
        this.barbearia = barbearia;
    }

    @Override
    public void run() {
        try {
            while (true) {

                barbearia.esperarProximoCliente();

                int tempoCorte = 2000 + rand.nextInt(3000);
                Thread.sleep(tempoCorte);

                barbearia.finalizarCorte();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Barbeiro foi interrompido e foi para casa.");
        }
    }
}