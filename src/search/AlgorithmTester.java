package search;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import search.algorithms.Algorithm;
import search.entities.SearchResult;
import search.entities.TestData;

public class AlgorithmTester {

	public static void run(List<Algorithm> algorithms, TestData data, int times) {
		
		List<SearchResult> results = new LinkedList<>();

		/**
		 * VM arguments = -XX:CompileThreshold=100
		 * -XX:+PrintCompilation to print out when JVM compiles what method
		 */
		try {
			System.out.println("Making sure all algorithms are compiled to native code...");

			for (Algorithm algorithm : algorithms) {
				Search.run(algorithm, data.copy(), 20);
			}

			Thread.sleep(2500);

			System.out.println("Done!");
			System.out.println();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("AlgorithmTester");
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
