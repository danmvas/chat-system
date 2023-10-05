[x] Retirar funcionalidade de enviar mensagem pra todo mundo
[x] Desconectar clientes


[x] A comunicação deve ser realizada entre os clientes, por intermédio do servidor;
[x] Toda mensagem enviada de um cliente deve ser direcionada a um único destino, isto é, outro cliente (exemplo: Alice envia uma mensagem para Bob);
[x] Permitir listar todos os clientes conectados pelo comando /users;
[x] O servidor deve ser responsável apenas por rotear as mensagens entre os clientes;
[x] Os clientes devem ser capazes de enviar e receber mensagens de texto;
[ ] Os clientes devem ser capazes de enviar e receber arquivos;
[x] A qualquer momento o cliente pode finalizar a comunicação ao informar o comando /sair; e
[x] O servidor deve manter um log em arquivo dos clientes que se conectaram, contendo os endereços IP e a data e hora de conexão.
[x] O envio de mensagens deve utilizar o comando /send message <mensagem>;
[x] As mensagens devem ser exibidas pelo destinatário na saída padrão (System.out), mostrando o nome do remetente seguido da mensagem;
[ ] O envio de arquivos deve utilizar o comando /send file <caminho do arquivo>;
[ ] O remetente deve informar o caminho do arquivo e o programa cliente deve ler os bytes do arquivo e enviá-los via socket;
[ ] O destinatário deve gravar os bytes recebidos com o nome original do arquivo no diretório corrente onde o programa foi executado;
