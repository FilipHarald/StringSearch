package search.entities;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

/**
 * Class representing test data used when evaluating search algorithms
 * @author Filip Harald
 *
 */
public class TestData {
	private final char[][] p;
	private final char[] t;
	
	/**
	 * Constructs a new TestData instance with supplied text and pattern
	 * @param t Text to search in
	 * @param p Pattern to search for
	 */
	public TestData(char[] t, char[][] p) {
		this.p = p;
		this.t = t;
	}

	/**
	 * @return Pattern to search for
	 */
	public char[][] getP() {
		return p;
	}

	/**
	 * @return Text to search in
	 */
	public char[] getT() {
		return t;
	}

	/**
	 * Loads text and pattern from files filename.text and filename.pattern. Assumes both files reside in /resources.
	 * @param filename Filename without any extension
	 * @return New TestData instance
	 */
	public static TestData loadFiles(String filename){
		return new TestData(loadText(filename), loadPatterns(filename));
	}
	
	/**
	 * Loads text and patterns from files with separate filenames. Assumes all files reside in /resources.
	 * @param tFilename Filename of text file without any extension
	 * @param pFilenames List of patterns to load
	 * @return New TestData instance
	 */
	public static TestData loadFiles(String tFilename, String... pFilenames) {
		List<StringBuilder> patterns = new LinkedList<>();

		for (String pFilename : pFilenames) {
			final StringBuilder sb = new StringBuilder();
			try {
				Files.lines(Paths.get("resources", pFilename + ".pattern")).forEachOrdered(s -> sb.append(s));
				patterns.add(sb);

			} catch (IOException e) {
			}
		}

		char[][] ps = new char[pFilenames.length][patterns.get(0).length()];

		for (int i = 0; i < patterns.size(); i++) {
			ps[i] = patterns.get(i).toString().toCharArray();
		}

		return new TestData(loadText(tFilename), ps);
	}
	
	/**
	 * Reads and returns an array containing all characters in a file. Assumes file resides in /resources.
	 * @param filename Filename of file to read 
	 * @return An array of chars
	 */
	private static char[] loadText(String filename) {
		return loadText(filename, Charset.forName("UTF-8"));
	}

	/**
	 * Reads a file (using the supplied {@code charset}), and returns an array containing all characters in the file.
	 * @param filename Filename of file to read
	 * @param charset Charset to read file in
	 * @return An array of chars
	 */
	private static char[] loadText(String filename, Charset charset){
		final StringBuilder sb = new StringBuilder();
		try {
			Files.lines(Paths.get("resources", filename + ".text"), Charset.forName("UTF-8")).forEachOrdered(s -> sb.append(s));
		} catch (MalformedInputException ex) {
			return loadText(filename, Charset.forName("Cp1252"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (sb.charAt(sb.length() - 1) != '$')
			sb.append("$");
		
		return sb.toString().toCharArray();
	}

	/**
	 * Reads all pattern files named filename followed by a digit.
	 * @param filename Filename to read
	 * @return An array of all patterns found
	 */
	private static char[][] loadPatterns(String filename){
		List<StringBuilder> patterns = new LinkedList<>();
		boolean fileExists = true;
		int counter = 1;
		while (fileExists) {
			final StringBuilder sb = new StringBuilder();
			try {
				Files.lines(Paths.get("resources", filename + counter + ".pattern")).forEachOrdered(s -> sb.append(s));
				counter++;
				patterns.add(sb);
				
			} catch (IOException e) {
				if (counter == 1) {
					e.printStackTrace();
				}
				
				fileExists = false;
			}
		}
		
		char[][] ps = new char[patterns.size()][patterns.get(0).length()];
		
		for (int i = 0; i < patterns.size(); i++) {
			ps[i] = patterns.get(i).toString().toCharArray();
		}
		
		return ps;
	}
	
}
