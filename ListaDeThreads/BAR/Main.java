import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {

        int numClientes = 10;
        int numGarcons = 3;
        int capacidadeGarcom = 4; // C
        int numRodadas = 5;

        if (capacidadeGarcom <= 0) {
            capacidadeGarcom = 1; 
        }

        System.out.printf("Iniciando Simulacao do Bar:\n" +
                " - Clientes: %d\n" +
                " - Garcons: %d\n" +
                " - Capacidade/Garcom: %d\n" +
                " - Rodadas/Cliente: %d\n\n",
                numClientes, numGarcons, capacidadeGarcom, numRodadas);

        Copa copa = new Copa(); 
        List<Cliente> todosClientes = new ArrayList<>();
        List<Garcom> todosGarcons = new ArrayList<>();

        for (int i = 0; i < numGarcons; i++) {
            todosGarcons.add(new Garcom(i, capacidadeGarcom, copa));
        }

        for (int i = 0; i < numClientes; i++) {
            Garcom garcomResponsavel = todosGarcons.get(i % numGarcons);
            Cliente cliente = new Cliente(i, numRodadas, garcomResponsavel);
            
            todosClientes.add(cliente);
            garcomResponsavel.adicionarCliente(cliente);
        }

        ExecutorService executor = Executors.newCachedThreadPool();
        System.out.println("BAR ABERTO. Iniciando threads...\n");

        for (Garcom g : todosGarcons) {
            executor.execute(g); 
        }
        for (Cliente c : todosClientes) {
            executor.execute(c); 
        }

        executor.shutdown();
        try {            
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("\nBAR FECHADO. Simulacao terminada.");
    }
}