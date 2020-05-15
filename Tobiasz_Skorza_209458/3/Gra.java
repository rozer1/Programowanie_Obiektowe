import java.util.Random;
import java.util.Scanner;

public class Gra{
	public static void main(String[] srgs){
			int losowa=0, wybrana=0,proby=0;
			String wybor="";
			Scanner odczyt = new Scanner(System.in);
			Random generator = new Random();
			
			
			do{	
				proby=0;
				losowa=generator.nextInt(100);
				System.out.print("Wygenerowano liczbez przedzialu 0-100\n");
				do{
					System.out.print("Wprowadz liczbe\n");
					wybrana=Integer.parseInt(odczyt.nextLine());
					proby=proby+1;
					System.out.print("Liczba prob: "+proby +"\n");
					if(wybrana==losowa){
						System.out.print("Brawo!!! Udalo ci sie trafic, aby zaczac gre od nowa wpisz nowa \n");
						wybor=odczyt.nextLine();
						System.out.print("\n\n\n\n\n");
					}
					else{
						if(wybrana>losowa){
							System.out.print("Niestety to nie jest ta liczba, wpisana liczba jest za duza\n");
							System.out.print("Aby sprobowac jeszcze raz wpisz tak, aby od nowa wylosowac liczbe wpisz nowa \n");
							wybor=odczyt.nextLine();	
							System.out.print("\n\n\n\n\n");
						}
						else{
							System.out.print("Niestety to nie jest ta liczba, wpisana liczba jest za mala\n");
							System.out.print("Aby sprobowac jeszcze raz wpisz tak, aby od nowa wylosowac liczbe wpisz nowa \n");
							wybor=odczyt.nextLine();
							System.out.print("\n\n\n\n\n");
						}
					}					
				}while(wybor.equals("tak"));
			}while(wybor.equals("nowa"));
			
	}
}