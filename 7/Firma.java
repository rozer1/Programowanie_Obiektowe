public class Firma extends Wpis {
	String nazwa;
	String adres ;
	public Firma(String nazwa, String adres){
	        this.nazwa = nazwa;
			this.adres = adres;
	}
	public String opis() {
		return String.format("%s", "Nazwa firmy: " + nazwa + "\tAdres: " + adres);
	}
}
