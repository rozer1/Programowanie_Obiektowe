import Zadanie8.Czasomierz;
import Zadanie8.Notyfikacja;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

public class Serwer {

    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(8888)) {
            System.out.println("The server is running...");
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new Notyfikator(listener.accept()));
            }
        }
    }

    private static class Notyfikator implements Runnable {
        private Socket socket;

        Notyfikator(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Polaczono klienta: " + socket);
            try {
                ObjectInputStream inw=new ObjectInputStream(socket.getInputStream());
                while (true){
                    Notyfikacja odebrano=(Notyfikacja) inw.readObject();
                    System.out.println("Przyjeto komunikat " + odebrano.komunikat + "  " + odebrano.godzina);
                    Czasomierz czas=new Czasomierz(odebrano,socket);
                    czas.uruchom();
                }
            }catch (Exception e) {
                System.out.println("Error:" + socket);
            } finally {
                try { socket.close(); } catch (IOException e) {}
                System.out.println("Closed: " + socket);
            }
        }
    }
}
