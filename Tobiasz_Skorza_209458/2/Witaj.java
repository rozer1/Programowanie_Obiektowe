
public class Witaj{
	public static void main(String[] args){
		int a=0,b=0;
		String s="";
		boolean poprawne=false;
		while(!poprawne){
			try{
				 a=Integer.parseInt(args[0]);
				 b=Integer.parseInt(args[1]);
				 s=args[2];
				}
				catch(Exception e){ 
				System.out.println("Wprowadzone dane są niepoprawne, sprobuj jeszcze raz");
				}
				poprawne=true;
			try{		
				s=s.substring(a,b+1);
				}
				catch(StringIndexOutOfBoundsException e) {
				System.out.println("Slowo jest za krotkie");
				System.exit(0);
				}
				poprawne=true;
		}

		System.out.println(s);
		
		
	}
	
}
