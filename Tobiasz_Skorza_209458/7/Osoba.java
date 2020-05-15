

public class Osoba extends Wpis {
	String imie;
	String nazwisko;
	String adres;
	
	public Osoba(String imie, String nazwisko, String adres){
	        this.imie = imie;
	        this.nazwisko = nazwisko;
	        this.adres = adres;
	};
	public String opis() {
		return String.format("%s", "Osoba: " + imie + " " + nazwisko + "\tAdres: " + adres);
	}
}
