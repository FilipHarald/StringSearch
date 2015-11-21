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
		
		System.out.println("AlgorithmTester");
		System.out.println("T = " + Arrays.toString(data.getT()));
		System.out.println("P = " + Arrays.toString(data.getP()));
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
