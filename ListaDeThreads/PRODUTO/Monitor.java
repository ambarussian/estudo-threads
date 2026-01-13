package PRODUTO;

import java.util.Queue;
import java.util.LinkedList;
import java.util.concurrent.ThreadLocalRandom;

public class Monitor {

    public static class Buffer {
        private final Queue<Integer> buffer = new LinkedList<>();
        private final int TAMANHO_BUFFER = 10;

        public synchronized void produzir(int item) throws InterruptedException {
            while (buffer.size() == TAMANHO_BUFFER) {
                System.out.println("Buffer CHEIO. Produtor espera.");
                wait();
            }
            
            buffer.add(item);
            System.out.println("Produtor produziu: " + item + " (Tamanho: " + buffer.size() + ")");
            notifyAll();
        }

        public synchronized int consumir() throws InterruptedException {
            while (buffer.isEmpty()) {
                System.out.println("Buffer VAZIO. Consumidor espera.");
                wait();
            }
            
            int item = buffer.remove();
            System.out.println("Consumidor consumiu: " + item + " (Tamanho: " + buffer.size() + ")");
            notifyAll();
            return item;
        }
    }

    public static class Produtor implements Runnable {
        private final Buffer buffer;

        public Produtor(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                int item = 0;
                while (true) {
                    item++;
                    buffer.produzir(item);
                    Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static class Consumidor implements Runnable {
        private final Buffer buffer;

        public Consumidor(Buffer buffer) {
            this.buffer = buffer;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    buffer.consumir();
                    Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 3000));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        Buffer buffer = new Buffer();
        
        Thread produtor = new Thread(new Produtor(buffer));
        Thread consumidor1 = new Thread(new Consumidor(buffer));
        Thread consumidor2 = new Thread(new Consumidor(buffer));

        produtor.start();
        consumidor1.start();
        consumidor2.start();
    }
}
