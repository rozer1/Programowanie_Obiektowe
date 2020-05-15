import java.math.RoundingMode;
import java.text.DecimalFormat;

public class Rownanie{
  private static DecimalFormat df = new DecimalFormat("0.00");
  public static void main(String[] args){
    
    int a=Integer.parseInt(args[0]);
    int b=Integer.parseInt(args[1]);
    int c=Integer.parseInt(args[2]);

    if(a==0)
    {
        System.out.print("To nie jest rownanie kwadratowe (A=0)!\n");
	double p1r=(-1*(double)c)/((double)b);
	System.out.print("X0="+p1r); 
	System.out.print("\n");   

    }
   else if (a!=0) {
	int delta=(b*b)-(4*a*c);
    if(delta<0) 
    {
	double p1r=(-1*(double)b)/(2*(double)a);
	double p1u=((-1)*Math.sqrt(delta*-1))/(2*a);
       	double p2r=(-1*(double)b)/(2*(double)a);
	double p2u=(Math.sqrt(delta*-1))/(2*a);
	System.out.print("\nRozwiazanie 1 rownania: x1="+df.format(p1r) +"+"+df.format(p1u) +"i");
        System.out.print("\nRozwiazanie 1 rownania: x2="+df.format(p2r) +"+"+df.format(p2u) +"i");
	System.out.print("\n");
         
    }
    else if (delta==0)
    {
        double p0=(-1*(double)b)/(2*(double)a);
        System.out.print("Jedynym rozwiazaniem rownania jest x0="+p0); 
	System.out.print("\n"); 
    }
    else if (delta>0)
    {
        double p1=((-1*(double)b)-Math.sqrt(delta))/(2*(double)a);
        double p2=((-1*(double)b)+Math.sqrt(delta))/(2*(double)a);
        System.out.print("\nRozwiazanie 1 rownania: x1="+p1);
        System.out.print("\nRozwiazanie 2 rownania: x2="+p2);
	System.out.print("\n");
    }    
   }
  }
}
