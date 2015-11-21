package search;

import java.util.LinkedList;
import java.util.List;

public class AlgorithmTester {

	public static List<SearchResult> run(List<Algorithm> algorithms, TestData data, int times) {
		
		List<SearchResult> results = new LinkedList<>();
		
		for (Algorithm algorithm : algorithms) {
			results.add(Search.run(algorithm, data, times));
		}
		
		
		return null;
	}
}
