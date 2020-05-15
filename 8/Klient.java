import Zadanie8.*;
import java.net.Socket;
public class Klient{
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Podaj adres IP w argumencie");
            return;
        }
        try {
            Socket gniazdo = new Socket(args[0], 8888);
            new Thread(new Wysylanie(gniazdo)).start();
            new Thread(new Odbieranie(gniazdo)).start();

        }catch (Exception e){
            System.out.println("Cos sie zepsulo");
        }
    }
}
