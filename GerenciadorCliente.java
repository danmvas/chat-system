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
                        // String destinatario = partes[2];
                        // String caminhoArquivo = partes[3];
                        // servidor.enviarArquivoParaCliente(destinatario, nomeCliente, caminhoArquivo);
                    }
                } else if (mensagem.equals("/sair")) {
                    servidor.removerCliente(nomeCliente);
                    System.out.println(nomeCliente + " saiu do chat.");
                    System.out.println("(" + horinha() + ") " + nomeCliente + " saiu no chat");
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Erro na comunicação com o cliente: " + e.getMessage());
            servidor.removerCliente(nomeCliente);
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

        updateLog();
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

     private void updateLog() {
        // Verifique se o servidor possui um nome de arquivo de log válido
        String logFileName = servidor.getLogFileName();
        if (logFileName != null) {
            try {
                File logsDirectory = new File("logs");
                File arquivo = new File(logsDirectory, logFileName);

                FileWriter fw = new FileWriter(arquivo, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("(" + horinha() + ") " + nomeCliente + " entrou no chat. IP: " + ipzinho());
                bw.newLine();
                bw.close();
                fw.close();
            } catch (IOException e) {
                System.err.println("Erro ao atualizar o arquivo de log: " + e.getMessage());
            }
        }
    }

}
