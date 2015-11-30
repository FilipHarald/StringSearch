package search.entities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Class representing test data used when evaluating search algorithms
 * @author Filip Harald
 *
 */
public class TestData {
	private final char[] p;
	private final char[] t;
	
	/**
	 * Constructs a new TestData instance with supplied text and pattern
	 * @param t Text to search in
	 * @param p Pattern to search for
	 */
	public TestData(char[] t, char[] p) {
		this.p = p;
		this.t = t;
	}

	/**
	 * @return Pattern to search for
	 */
	public char[] getP() {
		return p;
	}

	/**
	 * @return Text to search in
	 */
	public char[] getT() {
		return t;
	}
	
	/**
	 * @return A new copy of this TestData instance
	 */
	public TestData copy() {
		return new TestData(t.clone(), p.clone());
	}
	
	/**
	 * Loads text and pattern from files filename.text and filename.pattern. Assumes both files reside in /resources.
	 * @param filename Filename without any extension
	 * @return New TestData instance
	 */
	public static TestData loadFiles(String filename){
		return new TestData(load(filename + ".text"), load(filename + ".pattern"));
	}
	
	/**
	 * Loads text and pattern from files with separate filenames. Assumes both files reside in /resources.
	 * @param tFilename Filename of text file without any extension
	 * @param pFilename Filename of pattern file without any extension
	 * @return New TestData instance
	 */
	public static TestData loadFiles(String tFilename, String pFilename){
		return new TestData(load(tFilename + ".text"), load(pFilename + ".pattern"));
	}
	
	/**
	 * Reads and returns an array containing all characters in a file. Assumes file resides in /resources.
	 * @param filename Filename of file to read 
	 * @return An array of chars
	 */
	private static char[] load(String filename){
		final StringBuilder sb = new StringBuilder();
		try {
			Files.lines(Paths.get("resources", filename)).forEachOrdered(s -> sb.append(s));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString().toCharArray();
	}
	
}
