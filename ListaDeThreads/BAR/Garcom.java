import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue; // MODIFICADO
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Garcom implements Runnable {
    private final int id;
    private final int capacidade; // C
    private final Copa copa;
    
    private final List<Cliente> meusClientes = new ArrayList<>();
    private int clientesAtivos;

    private final LinkedBlockingQueue<Reporte> filaDeReporte = new LinkedBlockingQueue<>();

    private volatile CountDownLatch latchDaRodada; 
    
    private final Lock travaEstado = new ReentrantLock();
    private int clientesQueSairam = 0; 

    public Garcom(int id, int capacidade, Copa copa) {
        this.id = id;
        this.capacidade = capacidade;
        this.copa = copa;
    }

    public void adicionarCliente(Cliente c) {
        this.meusClientes.add(c);
    }

    @Override
    public void run() {
        this.clientesAtivos = meusClientes.size();
        System.out.printf("[Garcom %d] Iniciando servico para %d clientes.\n", id, clientesAtivos);

        try {
            while (clientesAtivos > 0) {
                
                int clientesEsperados = Math.min(capacidade, clientesAtivos);
                latchDaRodada = new CountDownLatch(clientesEsperados);
                
                System.out.printf("\n[Garcom %d] (Nova Rodada) Aguardando %d clientes se reportarem...\n", id, clientesEsperados);
                latchDaRodada.await(); 

                travaEstado.lock();
                try {
                    if (clientesQueSairam > 0) {
                        clientesAtivos -= clientesQueSairam;
                        clientesQueSairam = 0;
                    }
                } finally {
                    travaEstado.unlock();
                }

                List<Reporte> reportesDaRodada = new ArrayList<>();
                filaDeReporte.drainTo(reportesDaRodada, clientesEsperados);

                List<Pedido> pedidosParaCopa = reportesDaRodada.stream()
                        .filter(Reporte::querPedir)
                        .map(Reporte::getPedido)
                        .collect(Collectors.toList());

                if (!pedidosParaCopa.isEmpty()) {
                    System.out.printf("[Garçom %d] Coletou %d pedidos (de %d clientes). Indo ao bar...\n", id, pedidosParaCopa.size(), clientesEsperados);
                    copa.fazerPedidos(id, pedidosParaCopa);

                    System.out.printf("[Garçom %d] Entregando pedidos (na ordem de chegada):\n", id);
                    for (Reporte reporte : reportesDaRodada) {
                        if (reporte.querPedir()) {
                            System.out.printf("   -> Entregando para Cliente %d\n", reporte.getCliente().getId());
                            reporte.getCliente().receberPedido(); 
                        }
                    }
                    
                } else {
                     System.out.printf("[Garcom %d] Grupo reportou. Ninguem pediu. Pronto para proxima rodada.\n", id);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("[Garcom %d] Encerrando servico. Todos os clientes foram embora.\n", id);
    }

    public void reportar(Cliente cliente, boolean querPedir) throws InterruptedException {
        Reporte reporte = new Reporte(cliente, querPedir);

        filaDeReporte.put(reporte); 

        CountDownLatch latchAtual = latchDaRodada;
        if (latchAtual != null) {
            latchAtual.countDown();
        }
    }

    public void clienteSaindo(Cliente cliente) throws InterruptedException {
        travaEstado.lock();
        try {
            clientesQueSairam++;
        } finally {
            travaEstado.unlock();
        }

        Reporte reporteSaindo = new Reporte(cliente, false);
        filaDeReporte.put(reporteSaindo); 

        CountDownLatch latchAtual = latchDaRodada;
        if (latchAtual != null) {
            latchAtual.countDown();
        }
    }
}