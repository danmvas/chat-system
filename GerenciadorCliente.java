import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GerenciadorCliente extends Thread {
    private Socket socket;
    private PrintWriter escritor;
    private String nomeCliente;
    private final Servidor servidor;

    public GerenciadorCliente(Socket socket, Servidor servidor) {
        this.socket = socket;
        this.servidor = servidor;
    }

    public void run() {
        // System.out.println(myStr.replace(' ', '-'));
        try {
            escritor = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            solicitarLogin();

            String mensagem;
            while ((mensagem = leitor.readLine()) != null) {
                if (mensagem.startsWith("/m")) {
                    String[] partes = mensagem.split(" ", 3);
                    String destinatario = partes[1];
                    String mensagemEnviada = partes[2];
                    servidor.enviarMensagemParaCliente(destinatario, nomeCliente + ": " + mensagemEnviada);
                } else if (mensagem.startsWith("/u")) {
                    List<String> clientesConectados = servidor.getClientesConectados();
                    escritor.println("Usuários conectados: " + String.join(", ", clientesConectados));
                } else if (mensagem.startsWith("/f")) {
                    String[] partes = mensagem.split(" ", 4);
                    if (partes.length == 4) {
                        String destinatario = partes[1];
                        String caminhoArquivo = partes[2];
                        long tamanhoArquivo = Long.parseLong(partes[3]);
                        String remetente = nomeCliente;

                        receberArquivo(destinatario, remetente, caminhoArquivo, tamanhoArquivo, socket);
                    }
                } else if (mensagem.equals("/sair")) {
                    servidor.removerCliente(nomeCliente, ipzinho());
                    System.out.println(nomeCliente + " saiu do chat.");
                    System.out.println("(" + horinha() + ") " + nomeCliente + " saiu no chat");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro na comunicação com o cliente: " + e.getMessage());
            servidor.removerCliente(nomeCliente, ipzinho());
            System.exit(0);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.err.println("Erro ao fechar o socket: " + e.getMessage());
            }
        }
    }

    // void receiveFile(String newFileName){
    //     byte[] buffer = new byte[Integer.MAX_VALUE];
    //     int bytes = dataInputStream.read(buffer,0,buffer.length);
    //     fileOutputStream = new FileOutputStream(newFileName);
    //     fileOutputStream.write(buffer,0,bytes);
    // }

    private void solicitarLogin() throws IOException {
        // PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        // pw.println("Por favor, insira seu nome de login: ");
        nomeCliente = br.readLine().replace(" ", "-");
        servidor.adicionarCliente(nomeCliente, escritor);

        // pw.println("Login bem-sucedido! Você está conectado como " + nomeCliente);
        System.out.println("(" + horinha() + ") " + nomeCliente + " entrou no chat. IP: " + ipzinho());

        servidor.updateLog(nomeCliente + " entrou no chat. IP: " + ipzinho());
    }

    public String horinha() {
        Date date = new Date();
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        String time = formatTime.format(date);
        return time;
    }

    public String ipzinho() {
        String ip = socket.getInetAddress().getHostAddress();
        return ip;
    }

    // private void receberArquivo(String destinatario, String remetente, String nomeArquivo, long tamanhoArquivo, Socket socket) {
    //     try {
    //         System.out.println("tamo indo");
    //         String pathPadrao = "./serverFiles/";
    //         File arquivo = new File(pathPadrao + nomeArquivo);
    //         FileOutputStream fileOutputStream = new FileOutputStream(arquivo);
    //         InputStream socketInputStream = socket.getInputStream();
    //         byte[] buffer = new byte[4096];
    //         int bytesRead = 0;
    //         long bytesReceived = 0;
    //         System.out.println("to aqui");
    //         while (bytesReceived < tamanhoArquivo && (bytesRead = socketInputStream.read(buffer)) != -1) {
    //             System.out.println("entrei");
    //             bytesRead = socketInputStream.read(buffer);
    //             fileOutputStream.write(buffer,0,bytesRead);
    //             bytesReceived += bytesRead;
    //         }
    //         fileOutputStream.close();
    //         System.out.println("Arquivo " + arquivo.getName() + " recebido de " + remetente);
    //     } catch (IOException e) {
    //         System.err.println("Erro ao receber o arquivo: " + e.getMessage());
    //         System.out.println(e.getStackTrace());
    //     }
    // }

    private void receberArquivo(String destinatario, String remetente, String nomeArquivo, long tamanhoArquivo, Socket socket) {
        // o servidor nao precisa gravar o arquivo
        // pode redirecionar para o destinatario direto dentro do laco em que esta lendo do bufferedReader
        try {
//            System.out.println("tamo indo");
//            String diretorioPath = "./serverFiles/";
//            File directory = new File(diretorioPath);
//            if (!directory.exists()) {
//                directory.mkdirs();  // Create the directory if it does not exist
//            }

//            File arquivo = new File(diretorioPath + nomeArquivo);
//            FileOutputStream fileOutputStream = new FileOutputStream(arquivo);
//            System.out.println("to aqui");
            PrintWriter escritor = servidor.getClientPrintWriter(destinatario);
            escritor.println("/f " + remetente + " " + nomeArquivo + " " + tamanhoArquivo);

            byte[] buffer = new byte[4096];
            int bytesRead = 0;
            long bytesReceived = 0;
            InputStream socketInputStream = socket.getInputStream();

            // mudar para enviar e ler byte a byte com read()
            // certificar-se de que o read() esteja lendo 1 byte
            // utilizar como alternativa o metodo readNBytes

            while (bytesReceived < tamanhoArquivo && (bytesRead = socketInputStream.read(buffer)) != -1) {
                escritor.write
                escritor.flush();
//                System.out.println("entrei");
//                fileOutputStream.write(buffer, 0, bytesRead);
                bytesReceived += bytesRead;
            }
            fileOutputStream.close();
            System.out.println("Arquivo " + arquivo.getName() + " recebido de " + remetente);
        } catch (IOException e) {
            System.err.println("Erro ao receber o arquivo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // private void updateLog() {
    //     // Verifique se o servidor possui um nome de arquivo de log válido
    //     String logFileName = servidor.getLogFileName();
    //     if (logFileName != null) {
    //         try {
    //             File logsDirectory = new File("logs");
    //             File arquivo = new File(logsDirectory, logFileName);

    //             FileWriter fw = new FileWriter(arquivo, true);
    //             BufferedWriter bw = new BufferedWriter(fw);
    //             bw.write("(" + horinha() + ") " + nomeCliente + " entrou no chat. IP: " + ipzinho());
    //             bw.newLine();
    //             bw.close();
    //             fw.close();
    //         } catch (IOException e) {
    //             System.err.println("Erro ao atualizar o arquivo de log: " + e.getMessage());
    //         }
    //     }
    // }

}
