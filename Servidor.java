import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
// import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Servidor {
    private static final int PORTA = 12345;
    private static String logFileName;
    private final ConcurrentHashMap<String, PrintWriter> clientes = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, Date> horarioLogin = new ConcurrentHashMap<>();

     public Servidor() {
        logFileName = "LOG-" + this.dataHorinha()  + ".txt";
    }

    public static void main(String[] args) {
        System.out.println("Servidor iniciado. Aguardando conexões...");
        Servidor servidor = new Servidor();
        servidor.criaLog(logFileName);

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            while (true) {
                Socket clienteSocket = serverSocket.accept();
                new GerenciadorCliente(clienteSocket, servidor).start();
            }
        } catch (IOException e) {
            System.err.println("Erro ao iniciar o servidor: " + e.getMessage());
        }
    }

    public void adicionarCliente(String nome, PrintWriter escritor) {
        clientes.put(nome, escritor);
        horarioLogin.put(nome, new Date());
        enviarMensagemParaTodos(nome + " entrou no chat.");
    }

    public void removerCliente(String nome) {
        clientes.remove(nome);
        horarioLogin.remove(nome);
        enviarMensagemParaTodos(nome + " saiu do chat.");
    }

    public void enviarMensagemParaCliente(String destinatario, String mensagem) {
        PrintWriter cliente = clientes.get(destinatario);
        if (cliente != null) {
            cliente.println(mensagem);
        } else {
            enviarMensagemParaTodos("Cliente " + destinatario + " não encontrado.");
        }
    }

    public void enviarMensagemParaTodos(String mensagem) {
        for (PrintWriter cliente : clientes.values()) {
            cliente.println(mensagem);
        }
    }

    public List<String> getClientesConectados() {
        return new ArrayList<>(clientes.keySet());
    }

    public String dataHorinha() {
        Date date = new Date();
        LocalDate formatDate = LocalDate.now();
        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
        String time = formatTime.format(date);
        return formatDate + "-" + time;
    }

    private void criaLog(String logFileName) {
        try {
            File logsDirectory = new File("logs");
            if (!logsDirectory.exists()) {
                logsDirectory.mkdir();
            }           

            File arquivo = new File(logsDirectory, logFileName);
            arquivo.createNewFile();

        } catch (IOException e) {
            System.err.println("Erro ao criar o arquivo de log: " + e.getMessage());
        }
    }

	public String getLogFileName() {
        return logFileName;
	}

        // public void enviarArquivoParaCliente(String destinatario, String remetente, String caminhoArquivo) {
    //     PrintWriter cliente = clientes.get(destinatario);
    //     if (cliente != null) {
    //         try {
    //             File arquivo = new File(caminhoArquivo);
    //             if (arquivo.exists() && arquivo.isFile()) {
    //                 byte[] arquivoBytes = Files.readAllBytes(arquivo.toPath());

    //                 cliente.println(arquivoBytes.length);
    //                 cliente.flush();

    //                 cliente.println(Base64.getEncoder().encodeToString(arquivoBytes));
    //                 cliente.flush();

    //                 cliente.println("Arquivo enviado com sucesso!");
    //             } else {
    //                 cliente.println("Arquivo não encontrado: " + caminhoArquivo);
    //             }
    //         } catch (IOException e) {
    //             e.printStackTrace();
    //         }
    //     } else {
    //         enviarMensagemParaCliente(remetente, "Cliente " + destinatario + " não encontrado.");
    //     }
    // }

        // private static void sendFile(String path) throws Exception{
    //     int bytes = 0;
    //     File file = new File(path);
    //     FileInputStream fileInputStream = new FileInputStream(file);
        
    //     // send file size
    //     dataOutputStream.writeLong(file.length());  
    //     // break file into chunks
    //     byte[] buffer = new byte[4*1024];
    //     while ((bytes=fileInputStream.read(buffer))!=-1){
    //         dataOutputStream.write(buffer,0,bytes);
    //         dataOutputStream.flush();
    //     }
    //     fileInputStream.close();
    // }
}
