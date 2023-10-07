// import java.io.File;
// import java.io.FileInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
// import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Emissor implements Runnable {
    private Socket socket;
    private PrintWriter escritor;

    public Emissor(Socket socket) throws IOException {
        this.socket = socket;
        this.escritor = new PrintWriter(socket.getOutputStream(), true);
    }

    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {

            String name = "";
            do {
                System.out.println("Por favor, insira seu nome de login: ");
                name = scanner.nextLine();
            } while (name.isEmpty());
            escritor.println(name);

            String mensagem;
            while (true) {
                mensagem = scanner.nextLine();

                if (mensagem.startsWith("/f", 0)) {
                    // danhan minha heroína manda arquivo
                    String[] partes = mensagem.split(" ", 3);
                    String destinatario = partes[1];
                    String caminhoArquivo = partes[2];
                    enviarArquivo(destinatario, caminhoArquivo);
                } else if (mensagem.startsWith("/u")) {
                    escritor.println("/u");
                } else if (mensagem.startsWith("/sair")) {
                    desconectarCliente();
                    break;
                } else if (mensagem.startsWith("/m")) {
                    if (validaMensagem(mensagem)) {
                        escritor.println(mensagem);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("pinto");
            System.err.println("Erro na comunicação com o servidor: " + e.getMessage());
        } finally {
            desconectarCliente();
        }
    }

    public boolean validaMensagem(String mensagem) {
//        System.out.printf(mensagem);
//        String[] partes = mensagem.split(" ", 3);
//        String destinatario = partes[1];
//        String mensagemEnviada = partes[2];
//        if (destinatario.isEmpty() || mensagemEnviada.isEmpty()) {
//            return false;
//        }
        return true;
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

    void enviarArquivo(String destinatario, String caminhoArquivo) throws IOException {
        Path p = Paths.get(caminhoArquivo);
        String nomeArquivo = p.getFileName().toString();
        File file = new File(caminhoArquivo);
        long fileSize = file.length();

        // escritor.println("/f " + destinatario + " " + caminhoArquivo + " " + fileSize);
        escritor.println("/f " + destinatario + " " + nomeArquivo + " " + fileSize);

        try (FileInputStream fileInputStream = new FileInputStream(file);
             OutputStream socketOutputStream = socket.getOutputStream()) {
            byte[] buffer = new byte[4096];  // it's a common practice to use a buffer size of 4096
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                socketOutputStream.write(buffer, 0, bytesRead);
                socketOutputStream.flush();  // make sure all bytes are written
                // bytesRead
            }
            fileInputStream.close();

        } catch (Exception e) {
            System.err.println("Erro ao enviar arquivo: " + e.getMessage());
        }




        // byte[] buffer = new byte[4096];  
        // FileInputStream fileInputStream = new FileInputStream(file);
        // OutputStream socketOutputStream = socket.getOutputStream();  

        // // write file length into the stream
        // long fileSize = file.length();
        // escritor.println(Long.toString(fileSize));

        // int bytesRead;
        // while ((bytesRead = fileInputStream.read(buffer)) != -1) {
        //     socketOutputStream.write(buffer, 0, bytesRead);
        // }

        // socketOutputStream.flush();  // make sure all bytes are written
        // fileInputStream.close();  // don't forget to close your file stream
    }

    private void desconectarCliente() {
        escritor.println("/sair");
        System.exit(0);
    }
}