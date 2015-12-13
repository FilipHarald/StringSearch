package search;

import java.util.Collections;

import search.algorithms.Algorithm;
import search.entities.AlgorithmResult;
import search.entities.SearchResult;
import search.entities.TestData;

/**
 * Static class used to test a single algorithm
 * @author Filip Harald & Albert Kaaman
 */
public class Search {
	/**
	 * Searches 'times' times for the data.pattern in the data.text using the specified algorithm.
	 * @param algorithm Algorithm to test
	 * @param data Data to use in test
	 * @param times Times to run test
	 * @return The result of the searches
	 */
	public static SearchResult run(Algorithm algorithm, TestData data, int times){
		if(data.getP().length <= 0 || data.getT().length <= 0){
			System.out.println("Trying to search with algorithm " + algorithm + " Either pattern file or text file is empty");
			return null;
		}

		algorithm.preProcess(data.getT());
		
		AlgorithmResult result = null;
		long duration = 0;

		for (int i = 0; i < times; i++) {
			long start = System.nanoTime();
			result = algorithm.run(data.getP());
			duration += System.nanoTime() - start;
		}

		// We sort matches here since suffix tree does not return an already ordered list
		Collections.sort(result.getMatches());
		
		return new SearchResult(algorithm, result.getMatches(), result.getOperations(), duration/times, times);
	}
}
