import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Semaforo {
    private static final int NUM_FILOSOFOS = 5;
    private static final Semaphore garcom = new Semaphore(NUM_FILOSOFOS - 1);

    public static class FilosofoComGarcom implements Runnable {
        private final int id;
        private final Lock palitoDireita;
        private final Lock palitoEsquerda;

        public FilosofoComGarcom(int id, Lock palitoDireita, Lock palitoEsquerda) {
            this.id = id;
            this.palitoDireita = palitoDireita;
            this.palitoEsquerda = palitoEsquerda;
        }

        private void meditar() throws InterruptedException {
            System.out.println("Filósofo " + id + " está meditando.");
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
        }

        private void comer() throws InterruptedException {
            System.out.println("Filósofo " + id + " está com fome e pede permissão ao garçom.");
            garcom.acquire();

            try {
                System.out.println("Filósofo " + id + " (com permissão) tenta pegar palito da direita.");
                palitoDireita.lock();
                System.out.println("Filósofo " + id + " tenta pegar palito da esquerda.");
                palitoEsquerda.lock();

                try {
                    System.out.println("Filósofo " + id + " está comendo. 🍝");
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
                } finally {
                    palitoDireita.unlock();
                    palitoEsquerda.unlock();
                }
            } finally {
                garcom.release();
                System.out.println("Filósofo " + id + " terminou de comer e liberou seu lugar.");
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    meditar();
                    comer();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public static void main(String[] args) {
        Lock[] palitos = new ReentrantLock[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            palitos[i] = new ReentrantLock();
        }

        Thread[] filosofos = new Thread[NUM_FILOSOFOS];
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            Lock palitoDireita = palitos[i];
            Lock palitoEsquerda = palitos[(i + 1) % NUM_FILOSOFOS];
            
            filosofos[i] = new Thread(new FilosofoComGarcom(i, palitoDireita, palitoEsquerda));
            filosofos[i].start();
        }
    }
}