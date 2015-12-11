package search.algorithms;

import search.entities.AlgorithmResult;

/**
 * @author Albert Kaaman
 *
 */
public interface Algorithm {
	/**
	 * Runs the algorithm for the search pattern, p, in the text, t.
	 * @param t The text to  search in.
	 * @param p The pattern to search for.
	 * @return 
	 */
	AlgorithmResult run(char[] p);
	AlgorithmResult run(char[][] patterns);
	
	void preProcess(char[] t);
}
