package Zadanie8;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class Czasomierz {
    public Notyfikacja nota;
    public Socket gniazdo;
    public Czasomierz(Notyfikacja n, Socket g){
        nota=new Notyfikacja(n.godzina,n.komunikat);
        gniazdo=g;
    }

    public void uruchom() {
        try {
            DataOutputStream strumien_wyjsciowy = new DataOutputStream(gniazdo.getOutputStream());
            TimerTask wysylanie = new TimerTask() {
                @Override
                public void run() {
                    try {
                        String zwrot= "Uwaga komunikat! " + nota.komunikat + " O godzinie " + nota.godzina;
                        strumien_wyjsciowy.writeUTF(zwrot);
                        System.out.println("WYslano " + nota.komunikat + "  " + nota.godzina);
                        strumien_wyjsciowy.flush();
                    }
                    catch (IOException ioio){
                        ioio.printStackTrace();
                    }
                }
            };
            Timer czas=new Timer();
            czas.schedule(wysylanie,nota.godzina);
        }
        catch (IOException ioio){
            ioio.printStackTrace();
        }
    }
}
