package Zadanie8;

class Blad_daty extends Exception{
    public Blad_daty(){}
    public String GetWarning(){
        return "Podana data jest z przeszlosci";
    }
}
