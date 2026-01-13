import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;

public class Problema {
    private static final int NUM_FILOSOFOS = 5;

    public static class Filosofo implements Runnable {
        private final int id;
        private final Lock palitoDireita;
        private final Lock palitoEsquerda;

        public Filosofo(int id, Lock palitoDireita, Lock palitoEsquerda) {
            this.id = id;
            this.palitoDireita = palitoDireita;
            this.palitoEsquerda = palitoEsquerda;
        }

        private void meditar() throws InterruptedException {
            System.out.println("Filósofo " + id + " está meditando.");
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
        }

        private void comer() throws InterruptedException {
            System.out.println("Filósofo " + id + " está com fome e tenta pegar o palito da direita.");
            palitoDireita.lock();
            Thread.sleep(500); 
            System.out.println("Filósofo " + id + " tenta pegar o palito da esquerda.");
            palitoEsquerda.lock();

            try {
                System.out.println("Filósofo " + id + " está comendo. 🍝");
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
            } finally {
                palitoDireita.unlock();
                palitoEsquerda.unlock();
                System.out.println("Filósofo " + id + " terminou de comer e liberou os palitos.");
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
            
            filosofos[i] = new Thread(new Filosofo(i, palitoDireita, palitoEsquerda));
            filosofos[i].start();
        }
    }
}