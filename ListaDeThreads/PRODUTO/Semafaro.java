package PRODUTO;

import java.util.concurrent.Semaphore;
import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Semafaro {

    private static final int TAMANHO_BUFFER = 10;
    private static final Queue<Integer> buffer = new LinkedList<>();

    private static final Semaphore mutex = new Semaphore(1);
    private static final Semaphore full = new Semaphore(0);
    private static final Semaphore empty = new Semaphore(TAMANHO_BUFFER);

    public static class Produtor implements Runnable {
        @Override
        public void run() {
            try {
                int item = 0;
                while (true) {
                    item++;
                    empty.acquire();
                    mutex.acquire();
                    
                    buffer.add(item);
                    System.out.println("Produtor produziu: " + item + " (Tamanho: " + buffer.size() + ")");
                    
                    mutex.release();
                    full.release();
                    
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static class Consumidor implements Runnable {
        @Override
        public void run() {
            try {
                while (true) {
                    full.acquire();
                    mutex.acquire();
                    
                    int item = buffer.remove();
                    System.out.println("Consumidor consumiu: " + item + " (Tamanho: " + buffer.size() + ")");
                    
                    mutex.release();
                    empty.release();
                    
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Thread produtor = new Thread(new Produtor());
        Thread consumidor1 = new Thread(new Consumidor());
        Thread consumidor2 = new Thread(new Consumidor());

        produtor.start();
        consumidor1.start();
        consumidor2.start();
    }
}
