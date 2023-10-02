import java.io.BufferedReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
// import java.time.LocalTime;
import java.util.Date;

public class Receptor implements Runnable {
    private BufferedReader leitor;

    public Receptor(BufferedReader leitor) {
        this.leitor = leitor;
    }

    public void run() {
        try {
            String mensagem;
            while ((mensagem = leitor.readLine()) != null) {
                Date date = new Date();
                SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
                String time = formatTime.format(date);
                System.out.println("(" + time + ") " + mensagem);
            }
        } catch (IOException e) {
            System.err.println("Erro na comunicação com o servidor: " + e.getMessage());
        }
    }
}