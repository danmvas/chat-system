// import java.io.File;
// import java.io.FileInputStream;
import java.io.IOException;
// import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Emissor implements Runnable {
    // private Socket socket;
    private PrintWriter escritor;

    public Emissor(Socket socket) throws IOException {
        // this.socket = socket;
        this.escritor = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Por favor, insira seu nome de login: ");
            escritor.println(scanner.nextLine());
            
            String mensagem;
            while (true) {
                mensagem = scanner.nextLine();
                
                if (mensagem.startsWith("/f", 0)) {
                    // danhan minha heroína manda arquivo
                    // String[] partes = mensagem.split(" ", 3);
                    // String destinatario = partes[1];
                    // String caminhoArquivo = partes[2];
                    // enviarArquivo(destinatario, caminhoArquivo);
                } else if (mensagem.startsWith("/sair")) {
                    desconectarCliente();
                    break;
                } else {
                    escritor.println(mensagem);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro na comunicação com o cliente: " + e.getMessage());
        } finally {
            desconectarCliente();
        }
    }

    // public void enviarArquivo(String destinatario, String caminhoArquivo) {
    //     long bytes = 0;
    //     File file = new File(caminhoArquivo);
    //     FileInputStream fileInputStream = new FileInputStream(file);
        
    //     // send file size
    //     escritor.writeLong(file.length());  
    //     // break file into chunks
    //     byte[] buffer = new byte[4*1024];
    //     while ((bytes=fileInputStream.read(buffer))!=-1){
    //         escritor.write(buffer,0,bytes);
    //         escritor.flush();
    //     }
    //     fileInputStream.close();
    // }

    // void enviarArquivo(String destinatario, String caminhoArquivo) throws IOException {
    //     byte[] buffer = new byte[4096];  // it's a common practice to use a buffer size of 4096
    //     FileInputStream fileInputStream = new FileInputStream(caminhoArquivo);
    //     OutputStream socketOutputStream = socket.getOutputStream();  // assuming socket is your Socket instance

    //     int bytesRead;
    //     while ((bytesRead = fileInputStream.read(buffer)) != -1) {
    //         socketOutputStream.write(buffer, 0, bytesRead);
    //     }

    //     socketOutputStream.flush();  // make sure all bytes are written
    //     fileInputStream.close();  // don't forget to close your file stream
    // }

    private void desconectarCliente() {
        escritor.println("/sair");
        System.exit(0);
    }
}