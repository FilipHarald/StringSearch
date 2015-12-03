package search;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import search.algorithms.Algorithm;
import search.entities.SearchResult;
import search.entities.TestData;

/**
 * Tests the algorithms.
 * @author Albert Kaaman
 *
 */
public class AlgorithmTester {

	/**
	 * Runs the algorithms and prints result for the user to compare. 
	 * More specific it makes sure all algorithms are compiled to native code before running again and printing results.
	 * @param algorithms The different algortihms that should be compared
	 * @param data The data for which the algorithms both should search in and search for.
	 * @param times Number of times each algorithm should be run before calculating an avarage value.
	 */
	public static void run(List<Algorithm> algorithms, TestData data, int times) {
		
		List<SearchResult> results = new LinkedList<>();

		/**
		 * VM arguments = -XX:CompileThreshold=100
		 * -XX:+PrintCompilation to print out when JVM compiles what method
		 */
		try {
			System.out.println("Making sure all algorithms are compiled to native code...");

			TestData temp = TestData.loadFiles("alphabet");

			for (Algorithm algorithm : algorithms) {
				Search.run(algorithm, temp.copy(), 20);
			}

			Thread.sleep(2500);

			System.out.println("Done!");
			System.out.println();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("AlgorithmTester");
		System.out.println("T.length = " + data.getT().length);
		System.out.println("P.length = " + data.getP().length);
		System.out.println();

		for (Algorithm algorithm : algorithms) {
			System.out.println("Running algorithm " + algorithm.getClass().getSimpleName() + " over data " + times + " times");
			results.add(Search.run(algorithm, data.copy(), times));
		}
		
		for (SearchResult result : results) {
			System.out.println(result);
		}
		
	}
}
