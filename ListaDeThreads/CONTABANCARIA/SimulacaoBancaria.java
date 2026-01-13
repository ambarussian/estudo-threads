package CONTABANCARIA;

public class SimulacaoBancaria {

    public static void main(String[] args) throws InterruptedException {
        ContaBancaria contaA = new ContaBancaria(1, 1000.0);
        ContaBancaria contaB = new ContaBancaria(2, 500.0);

        System.out.println("Saldos Iniciais:");
        System.out.println("Conta " + contaA.getId() + ": " + contaA.getSaldo());
        System.out.println("Conta " + contaB.getId() + ": " + contaB.getSaldo());
        System.out.println("--- Iniciando Acoes Concorrentes ---");

        
        Thread t1 = new Thread(() -> {
            contaA.transferir(contaB, 100.0);
        });

        Thread t2 = new Thread(() -> {
            contaA.depositar(50.0);
        });

        Thread t3 = new Thread(() -> {
            contaA.aplicarJuros(0.01); 
        });

        
        Thread t4 = new Thread(() -> {
            contaB.sacar(200.0);
        });

        Thread t5 = new Thread(() -> {
            contaB.depositar(300.0);
        });
        
        Thread t6 = new Thread(() -> {
            contaB.aplicarJuros(0.02);
        });

        Thread t7 = new Thread(() -> {
            contaB.transferir(contaA, 150.0);
        });


        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        t7.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        t7.join();

        System.out.println("--- Acoes Finalizadas ---");
        System.out.println("Saldos Finais:");
        System.out.println("Conta " + contaA.getId() + ": " + contaA.getSaldo());
        System.out.println("Conta " + contaB.getId() + ": " + contaB.getSaldo());
    }
}