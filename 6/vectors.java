import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class vectors {
	public static class WektoryRoznejDlugosciException extends Exception {}

	public static void vectorGenerator(ArrayList<Integer> vector){
		vector.clear();
		String str;
		Scanner scanner = new Scanner(System.in);
		while (true) {
		        str = scanner.nextLine();
		        if(str.equals(""))
		                break;

		        try{
		                vector.add(Integer.parseInt(str));
		        } catch (NumberFormatException e) {
		                System.out.println("Nie poprawny format");
		        }
		}
	}

	public static boolean sumVectors(ArrayList<Integer> v1, ArrayList<Integer> v2) throws WektoryRoznejDlugosciException {
		if(v1.size() != v2.size()) throw new WektoryRoznejDlugosciException();

		ArrayList<Integer> v3 = new ArrayList<Integer>();
		for(int i = 0; i < v1.size(); i++) {
		        v3.add(v1.get(i) + v2.get(i));
		}
		System.out.println(v3);
		try {
		        vectorSave(v3);
		} catch(IOException e){
		        e.printStackTrace();
		}

		return true;
	}
	public static void vectorSave(ArrayList<Integer> vector) throws IOException {
		FileWriter writer = null;

		try {
		        writer = new FileWriter("vector.txt");
		        for(int i = 0; i < vector.size(); i++) {
		                writer.write(vector.get(i).toString()+" ");
		        }

		} finally {
		        if (writer != null)
		                writer.close();
		}
	}
	public static void main(String[] args) throws WektoryRoznejDlugosciException {
		ArrayList<Integer> v1 = new ArrayList<Integer>();
		ArrayList<Integer> v2 = new ArrayList<Integer>();
		boolean equals = false;


		System.out.println("Podaj pierwszy wektor");
		vectorGenerator(v1);
		System.out.println("Podaj drugi wektor");
		vectorGenerator(v2);

		System.out.println("Pierwszy: " + v1);
		System.out.println("Drugi: " + v2);

		while(!equals) {
		        try{
		                equals = sumVectors(v1, v2);
		        }
		        catch(WektoryRoznejDlugosciException e) {
		                System.out.println("Różne długości wektorów, podaj wektory jeszcze raz");
		                System.out.println("Podaj pierwszy wektor");
		                vectorGenerator(v1);
		                System.out.println("Podaj drugi wektor");
		                vectorGenerator(v2);
		                System.out.println("Długość pierwszego wektora: " + v1.size() + "\n Długość drugiego wektora: " + v2.size());

		        }
		}


	}
}
