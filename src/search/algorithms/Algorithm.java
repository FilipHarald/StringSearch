package search.algorithms;

import search.entities.AlgorithmResult;

/**
 * Interface used by all search algorithms
 * @author Albert Kaaman
 */
public interface Algorithm {
	/**
	 * Runs the algorithm for the search pattern {@code p}
	 * @param p The pattern to search for.
	 * @return Result of search
	 */
	AlgorithmResult run(char[] p);

	/**
	 * Runs the algorithm for multiple patterns.
	 * @param patterns The patterns to search for.
	 * @return Result of search
	 */
	AlgorithmResult run(char[][] patterns);

	/**
	 * Preprocesses the text {@code t}
	 * @param t Text to preprocess
	 */
	void preProcess(char[] t);
}
