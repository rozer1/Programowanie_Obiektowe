
import java.util.*;
import java.util.Map.Entry;
public class phoneBook {



	public static void main(String[] args) {
			TreeMap<NrTelefoniczny, Wpis> phoneBook = new TreeMap<NrTelefoniczny, Wpis>();

			Osoba wpis1 = new Osoba("Jan", "Dzban", "Zgierz");
			wpis1.telefon = new NrTelefoniczny(48, 568713931);
			phoneBook.put(wpis1.telefon, wpis1);

			Osoba wpis2 = new Osoba("Zbyszko", "Bogdan", "Pabianice");
			wpis2.telefon = new NrTelefoniczny(48, 581213567);
			phoneBook.put(wpis2.telefon, wpis2);

			Osoba wpis3 = new Osoba("Markus", "Sznajder", "Rotterdam");
			wpis3.telefon = new NrTelefoniczny(31, 888961531);
			phoneBook.put(wpis3.telefon, wpis3);

			Firma wpis4 = new Firma("BMW", "Monachium");
			wpis4.telefon = new NrTelefoniczny(49, 788123512);
			phoneBook.put(wpis4.telefon, wpis4);

			Firma wpis5 = new Firma("Sony", "Tokio");
			wpis5.telefon = new NrTelefoniczny(81, 712451974);
			phoneBook.put(wpis5.telefon, wpis5);

			Firma wpis6 = new Firma("Mirkopol", "Gdansk");
			wpis6.telefon = new NrTelefoniczny( 48, 468213151);
			phoneBook.put(wpis6.telefon, wpis6);


			Set<Entry<NrTelefoniczny, Wpis>> set = phoneBook.entrySet();
			Iterator<Entry<NrTelefoniczny, Wpis>> iterator = set.iterator();
	        while(iterator.hasNext()) {
	           Entry<NrTelefoniczny, Wpis> item = iterator.next();
	           System.out.println("-------------------------------------------------"
					+"\n" + item.getKey().printNumber());
	          System.out.println(item.getValue().opis());
	        }
	}

}
