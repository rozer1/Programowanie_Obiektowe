package Zadanie8;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Wysylanie implements Runnable{
    public Socket gniazdo;
    public Wysylanie(Socket socket){
        gniazdo=socket;
    }
    DateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    public void run() {
        try{
            Date czas;
            Scanner skan_tekstu=new Scanner(System.in);
            ObjectOutputStream wyjscie_objekt = new ObjectOutputStream(gniazdo.getOutputStream());
            while (true) {
                System.out.println("Podaj tresc notyfikacji");
                String tresc = skan_tekstu.nextLine();
                System.out.println("Podaj date notyfikacji w formacie yyyy-MM-dd HH:mm:ss");
                String data_odeslania = skan_tekstu.nextLine();
                czas = format.parse(data_odeslania);
                Notyfikacja nota_do_wyslania = new Notyfikacja(czas, tresc);
                nota_do_wyslania.compareDate(new Date());
                wyjscie_objekt.writeObject(nota_do_wyslania);
                wyjscie_objekt.flush();
                wyjscie_objekt.reset();
            }
        }
        catch (IOException blad_IO){
            System.out.println("blad wysylania");
        }
        catch (ParseException parsowanie) {
            System.out.println("Data w zlym formaice");
        }
        catch (Blad_daty data){
            System.out.println(data.GetWarning());
            data.printStackTrace();
        }
    }
}
