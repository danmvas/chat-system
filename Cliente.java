import java.io.*;
import java.net.*;

public class Cliente {
    private Socket socket;

    public static void main(String[] args) {
        new Cliente().executarCliente();
    }

    public void executarCliente() {
        try {
            socket = new Socket("127.0.0.1", 12345);
            System.out.println("Conexão estabelecida com o servidor");

            BufferedReader leitor = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // PrintWriter escritor = new PrintWriter(socket.getOutputStream(), true);

            Thread threadReceptor = new Thread(new Receptor(leitor));
            Thread threadEmissor = new Thread(new Emissor(socket));

            threadReceptor.start();
            threadEmissor.start();
        } catch (IOException e) {
            System.err.println("Erro ao conectar ao servidor: " + e.getMessage());
        }
    }
}
