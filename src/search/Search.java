package search;

import java.util.concurrent.TimeUnit;

import search.algorithms.Algorithm;
import search.entities.AlgorithmResult;
import search.entities.SearchResult;
import search.entities.TestData;

public class Search {
	public static SearchResult run(Algorithm algorithm, TestData data, int times){
		algorithm.preProcess(data.getT());
		
		AlgorithmResult result = null;
		long duration = 0;
				
		for (int i = 0; i < times; i++) {
			long start = System.nanoTime();
			result = algorithm.run(data.getP());
			duration += System.nanoTime() - start;
		}
		
		return new SearchResult(algorithm, result.getMatches(), result.getOperations(), duration/times, times);
	}
}
