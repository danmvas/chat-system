# Chat com mensagens e transferência de arquivos

## Descrição

O projeto consiste em um chat com mensagens e transferência de arquivos entre dois ou mais usuários. O chat foi desenvolvido em Java e utiliza a biblioteca socket para a comunicação entre os usuários. O chat possui um classe Servidor que gerencia as conexões entre os usuários e uma classe Cliente que permite a comunicação entre os usuários, bem como outras classes auxiliares para estabelecer a comunicação.

## Como executar

Para executar o projeto, é necessário ter o Java instalado na máquina. Para executar o servidor, basta executar o arquivo Servidor.java. Para executar o cliente, basta executar o arquivo Cliente.java. O servidor deve ser executado antes do cliente.

Em um terminal de comando, execute o seguinte comando para executar o servidor:

```bash
javac Servidor.java
java Servidor
```

Em outro terminal de comando, execute o seguinte comando para executar o cliente:

```bash
javac Cliente.java
java Cliente
```

## Funcionamento

No terminal de comando dos clientes, é possível enviar mensagens ou arquivos para outros usuários conectados ao servidor. As funções do chat implementadas são as seguintes:

- **/u**: lista os usuários conectados ao servidor
- **/m <usuário> <mensagem>** manda uma mensagem para um usuário específico
- **/f <usuário> <arquivo>** manda um arquivo para um usuário específico