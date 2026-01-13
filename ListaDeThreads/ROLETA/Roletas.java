package ROLETA;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Roletas {

    private static final int NUM_ROLETAS = 5;
    private static final int PESSOAS_POR_ROLETA = 1000;

    public static class ContadorAtomico {
        private AtomicInteger total = new AtomicInteger(0);

        public void incrementar() {
            this.total.incrementAndGet();
        }

        public int getTotal() {
            return this.total.get();
        }
    }

    public static class Roleta implements Runnable {
        private final ContadorAtomico contador;
        private final int pessoasParaPassar;

        public Roleta(ContadorAtomico contador, int pessoasParaPassar) {
            this.contador = contador;
            this.pessoasParaPassar = pessoasParaPassar;
        }

        @Override
        public void run() {
            for (int i = 0; i < this.pessoasParaPassar; i++) {
                contador.incrementar();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ContadorAtomico contadorGeral = new ContadorAtomico();
        List<Thread> threads = new ArrayList<>();

        int totalEsperado = NUM_ROLETAS * PESSOAS_POR_ROLETA;

        for (int i = 0; i < NUM_ROLETAS; i++) {
            Thread t = new Thread(new Roleta(contadorGeral, PESSOAS_POR_ROLETA));
            threads.add(t);
            t.start();
        }

        for (Thread t : threads) {
            t.join();
        }

        System.out.println("Total Esperado: " + totalEsperado);
        System.out.println("Total Contado:   " + contadorGeral.getTotal());
        System.out.println("Contagem correta!");
    }
}