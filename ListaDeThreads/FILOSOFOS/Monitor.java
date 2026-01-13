import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;

public class Monitor {
    private static final int NUM_FILOSOFOS = 5;

    public static class GerenciadorDePalitos {
        private final Lock lock = new ReentrantLock();
        private final Condition condicao = lock.newCondition();
        private final boolean[] palitosLivres;
        
        public GerenciadorDePalitos(int numPalitos) {
            this.palitosLivres = new boolean[numPalitos];
            Arrays.fill(this.palitosLivres, true);
        }

        public void pegarPalitos(int id) throws InterruptedException {
            int palitoDireita = id;
            int palitoEsquerda = (id + 1) % NUM_FILOSOFOS;
            
            lock.lock();
            try {
                while (!(palitosLivres[palitoDireita] && palitosLivres[palitoEsquerda])) {
                    System.out.println("Filósofo " + id + " espera (palitos " + palitoDireita + " ou " + palitoEsquerda + " ocupados).");
                    condicao.await();
                }
                
                palitosLivres[palitoDireita] = false;
                palitosLivres[palitoEsquerda] = false;
                System.out.println("Filósofo " + id + " pegou os palitos " + palitoDireita + " e " + palitoEsquerda + ".");
            } finally {
                lock.unlock();
            }
        }

        public void liberarPalitos(int id) {
            int palitoDireita = id;
            int palitoEsquerda = (id + 1) % NUM_FILOSOFOS;
            
            lock.lock();
            try {
                palitosLivres[palitoDireita] = true;
                palitosLivres[palitoEsquerda] = true;
                System.out.println("Filósofo " + id + " liberou os palitos " + palitoDireita + " e " + palitoEsquerda + ".");
                condicao.signalAll();
            } finally {
                lock.unlock();
            }
        }
    }

    public static class FilosofoComMonitor implements Runnable {
        private final int id;
        private final GerenciadorDePalitos monitor;

        public FilosofoComMonitor(int id, GerenciadorDePalitos monitor) {
            this.id = id;
            this.monitor = monitor;
        }

        private void meditar() throws InterruptedException {
            System.out.println("Filósofo " + id + " está meditando.");
            Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
        }

        private void comer() throws InterruptedException {
            System.out.println("Filósofo " + id + " está com fome e chama o monitor.");
            monitor.pegarPalitos(id);
            
            try {
                System.out.println("Filósofo " + id + " está comendo. 🍝");
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
            } finally {
                monitor.liberarPalitos(id);
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
        GerenciadorDePalitos monitor = new GerenciadorDePalitos(NUM_FILOSOFOS);
        Thread[] filosofos = new Thread[NUM_FILOSOFOS];
        
        for (int i = 0; i < NUM_FILOSOFOS; i++) {
            filosofos[i] = new Thread(new FilosofoComMonitor(i, monitor));
            filosofos[i].start();
        }
    }
}