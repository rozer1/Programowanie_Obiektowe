package Zadanie8;

import java.io.Serializable;
import java.util.Date;

public class Notyfikacja implements Serializable {
    public Date godzina;
    public String komunikat;

    Notyfikacja(Date data, String tresc){
        godzina=data;
        komunikat=tresc;
    }
    public int compareDate(Date do_porownania) throws Blad_daty {
        long temp=this.godzina.getTime()-do_porownania.getTime();
        if (Math.abs(temp)<=1000){
            return 1;
        }
        else if(temp<0){
            throw new Blad_daty();
        }
        else{
            return 0;
        }
    }
}
