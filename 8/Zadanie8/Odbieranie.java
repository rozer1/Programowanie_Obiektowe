package Zadanie8;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

public class Odbieranie implements Runnable{
    public Socket gniazdo;
    public Odbieranie(Socket socket){
        gniazdo=socket;
    }
    @Override
    public void run() {
        try {
            DataInputStream wejscie=new DataInputStream(gniazdo.getInputStream());
            while (true) {
                String zwrot=wejscie.readUTF();
                System.out.println(zwrot);
            }
        }
        catch(IOException blodIO){
            System.out.println("Blad odbieranie, Serwer off");
        }
    }
}
