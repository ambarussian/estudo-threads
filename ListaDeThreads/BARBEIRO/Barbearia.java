public class Barbearia {

    private final int numCadeiras;
    private int clientesEsperando = 0;
    

    private boolean barbeiroOcupado = false;
    private int clienteSendoAtendido = -1; 

    
    public Barbearia(int numCadeiras) {
        this.numCadeiras = numCadeiras;
        System.out.println("Barbearia aberta com " + numCadeiras + " cadeiras de espera.");
    }

  
    public synchronized void esperarProximoCliente() throws InterruptedException {
   
        while (clientesEsperando == 0 && !barbeiroOcupado) {
            System.out.println("Barbeiro: Zzz... (Não há clientes. Dormindo)");
            wait(); 
        }
        

        while (!barbeiroOcupado) {

             System.out.println("Barbeiro: Fui acordado, esperando cliente sentar...");
             wait();
        }
        
        System.out.println("Barbeiro: Cortando cabelo do Cliente " + clienteSendoAtendido + "...");
    }

    
    public synchronized void finalizarCorte() {
        System.out.println("Barbeiro: Corte do Cliente " + clienteSendoAtendido + " finalizado. Próximo!");
        
        barbeiroOcupado = false;
        clienteSendoAtendido = -1;
        
        notifyAll();
    }

    // --- Método do Cliente ---

    public synchronized void tentarCortarCabelo(int idCliente) throws InterruptedException {
      
        System.out.println("[Cliente " + idCliente + "] Chegou na barbearia.");

        if (clientesEsperando == numCadeiras) {
            System.out.println("[Cliente " + idCliente + "] BARBEARIA CHEIA! Indo embora.");
            return;
        }

        clientesEsperando++;
        System.out.println("[Cliente " + idCliente + "] Sentou na cadeira de espera. (Total esperando: " + clientesEsperando + ")");

        while (barbeiroOcupado) {
            System.out.println("[Cliente " + idCliente + "] Esperando o barbeiro ficar livre...");
            wait();
        }

        clientesEsperando--; 
        barbeiroOcupado = true; 
        clienteSendoAtendido = idCliente; 
        
        System.out.println("[Cliente " + idCliente + "] MINHA VEZ! Acordando o barbeiro. (Restam: " + clientesEsperando + ")");
        
        notifyAll();

        while (barbeiroOcupado && clienteSendoAtendido == idCliente) {
            System.out.println("[Cliente " + idCliente + "] Barbeiro está cortando meu cabelo...");
            wait(); 
        }

        System.out.println("[Cliente " + idCliente + "] Corte finalizado! Saindo da barbearia.");
    }
}