# Lista de estudos - Threads em JAVA

## Introdução
Este estudo diz respeito a um conjunto de exercícios práticos sobre threads e programação concorrente, organizados em pastas. Cada pasta contém problemas que simulam cenários reais, permitindo a compreensão do funcionamento de threads, sincronização e gerenciamento de recursos compartilhados. Com isso, foi possível estudar de forma progressiva e prática os conceitos fundamentais de concorrência em programação.

## Objetivos
- Compreender e praticar conceitos relacionados a threads e controle de concorrência;
- Implementar soluções para problemas clássicos, como sincronização, controle de acesso a recursos compartilhados e simulação de cenários multi-thread;
- Estudar problemas que envolvem situações do cotidiano, estes entitulados BAR, BARBEIRO, CONTABANCARIA, FILOSOFOS, PRODUTO e ROLETA.

## Arquitetura e descrição dos exercícios

- `BAR`: simula o controle de acesso de clientes a um bar, onde apenas um número limitado de pessoas pode ser atendido simultaneamente, introduzindo conceitos de semáforos e controle de concorrência.  
- `BARBEIRO`: baseado no clássico problema do barbeiro dorminhoco, este exercício explora a coordenação de threads entre barbeiros e clientes, incluindo espera, notificação e sinalização.  
- `CONTABANCARIA`: simula transações bancárias concorrentes, abordando problemas de consistência de dados e condições de corrida, como depósitos e saques simultâneos.  
- `FILOSOFOS`: resolve o problema do jantar dos filósofos com quatro soluções diferentes, cada uma abordando estratégias diferentes para fins de comparação e compreensão de como pequenas mudanças na lógica de sincronização afetam o comportamento do sistema.    
- `PRODUTO`: trata da produção e consumo de itens em uma fila compartilhada, permitindo estudar buffers, sincronização e sinalização entre threads produtoras e consumidoras.  
- `ROLETA`: simula o controle de acesso a uma catraca, permitindo que múltiplas threads (pessoas) tentem passar simultaneamente. O exercício trabalha sincronização, exclusão mútua e controle de fluxo em situações concorrentes.
