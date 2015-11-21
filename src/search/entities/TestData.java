package search.entities;

public class TestData {
	private final char[] p;
	private final char[] t;
	
	public TestData(char[] t, char[] p) {
		this.p = p;
		this.t = t;
	}

	public char[] getP() {
		return p;
	}

	public char[] getT() {
		return t;
	}
}
