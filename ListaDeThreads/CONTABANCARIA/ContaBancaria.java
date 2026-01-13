package CONTABANCARIA;

public class ContaBancaria {
    private double saldo;
    private final int id;

    public ContaBancaria(int id, double saldoInicial) {
        this.id = id;
        this.saldo = saldoInicial;
    }

    public synchronized void depositar(double valor) {
        this.saldo += valor;
        System.out.println("Deposito de " + valor + " na conta " + id + ". Saldo novo: " + this.saldo);
    }

    public synchronized boolean sacar(double valor) {
        if (this.saldo >= valor) {
            this.saldo -= valor;
            System.out.println("Saque de " + valor + " da conta " + id + ". Saldo novo: " + this.saldo);
            return true;
        } else {
            System.out.println("Tentativa de saque de " + valor + " da conta " + id + " falhou. Saldo insuficiente.");
            return false;
        }
    }

    public synchronized void aplicarJuros(double taxa) {
        double juros = this.saldo * taxa;
        this.saldo += juros;
        System.out.println("Credito de juros (" + taxa * 100 + "%) na conta " + id + ". Valor: " + juros + ". Saldo novo: " + this.saldo);
    }

    public synchronized boolean transferir(ContaBancaria destino, double valor) {
        System.out.println("Transferencia da conta " + this.id + " para " + destino.id + " no valor de " + valor);
        if (this.sacar(valor)) {
            destino.depositar(valor);
            return true;
        } else {
            System.out.println("Transferencia falhou. Saldo insuficiente na conta " + this.id);
            return false;
        }
    }

    public double getSaldo() {
        return this.saldo;
    }

    public int getId() {
        return this.id;
    }
}