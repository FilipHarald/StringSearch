package search.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestData {
	private final char[] p;
	private final char[] t;
	
	public static void main(String[] args) {
		load("lol");
	}
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
	
	public TestData copy() {
		return new TestData(t.clone(), p.clone());
	}
	
	public static TestData loadFiles(String filename){
		return new TestData(load(filename + ".t"), load(filename + ".p"));
	}
	
	public static TestData loadFiles(String tFilename, String pFilename){
		return new TestData(load(tFilename + ".t"), load(pFilename + ".p"));
	}
	
	public static char[] load(String filename){
		final StringBuilder sb = new StringBuilder();
		try {
			Files.lines(Paths.get("resources", filename)).forEachOrdered(s -> sb.append(s));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().toCharArray();
	}
	
}
