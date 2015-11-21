package search.algorithms;

import search.entities.AlgorithmResult;

/**
 * @author Albert Kaaman
 *
 */
public interface Algorithm {
	/**
	 * Runs the algorithm for the search phrase, p, in the text, t.
	 * @param t The text to  search in.
	 * @param p The phrase to search for.
	 * @return 
	 */
	AlgorithmResult run(char[] t, char[] p);
}
