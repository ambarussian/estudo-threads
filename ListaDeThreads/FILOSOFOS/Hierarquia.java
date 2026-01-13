import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.ThreadLocalRandom;

public class Hierarquia {
    private static final int NUM_FILOSOFOS = 5;

    public static class FilosofoEsperto implements Runnable {
        private final int id;
        private final Lock firstLock;
        private final Lock secondLock;

        public FilosofoEsperto(int id, Lock palito1, Lock palito2) {
            this.id = id;
            if (System.identityHashCode(palito1) < System.identityHashCode(palito2)) {
                this.firstLock = palito1;
                this.secondLock = palito2;
            } else {
                this.firstLock = palito2;
                this.secondLock = palito1;
            }
        }

        private void meditar() throws InterruptedException {
            System.out.println("Filósofo " + id + " está meditando.");
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
        }

        private void comer() throws InterruptedException {
            System.out.println("Filósofo " + id + " está com fome e tenta pegar o primeiro palito.");
            firstLock.lock();
            System.out.println("Filósofo " + id + " tenta pegar o segundo palito.");
            secondLock.lock();

            try {
                System.out.println("Filósofo " + id + " está comendo. 🍝");
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
            } finally {
                firstLock.unlock();
                secondLock.unlock();
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
            
            filosofos[i] = new Thread(new FilosofoEsperto(i, palitoDireita, palitoEsquerda));
            filosofos[i].start();
        }
    }
}