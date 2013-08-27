_____________________________________________________________________________
v0.1

Funcionamento:
- Resource é um recurso na rede e espera por requisições na porta 12399
- Broker conecta e envia um objeto GridJob para ser executado
- Resource executa e envia a resposta
- 1 Conexão do Broker envia 1 lista de Jobs para executar
- 1 Conexão de resposta do Resource envia uma lista de resposta ordenados respectivamente a cada job

Para utilizar o Middleware:
- Criar objetos GridJob que serão distribuidos no grid
- Utilizar o método dispatchJobs da classe Broker para enviar os Jobs e aguardar a resposta
- Se não houver exceções utilizar o método getReplies para receber as resopstas na ordem enviada
_____________________________________________________________________________
