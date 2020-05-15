public class NrTelefoniczny implements Comparable<NrTelefoniczny> {
	int nrKierunkowy;
	int nrTelefonu;
	public NrTelefoniczny(int nrKierunkowy, int nrTelefonu) {
		this.nrKierunkowy = nrKierunkowy;
		this.nrTelefonu = nrTelefonu;
	}
	public String printNumber() {
	    return String.format("%s", "Numer: +" + nrKierunkowy + " " + nrTelefonu);
	}
	public int compareTo(NrTelefoniczny other) {
		return Integer.compare(this.nrTelefonu, other.nrTelefonu);
	}
}
