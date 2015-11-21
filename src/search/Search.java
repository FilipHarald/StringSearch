package search;

import java.util.concurrent.TimeUnit;

import search.algorithms.Algorithm;
import search.entities.AlgorithmResult;
import search.entities.SearchResult;
import search.entities.TestData;

public class Search {
	public static SearchResult run(Algorithm algorithm, TestData data, int times){
		
		AlgorithmResult result = null;
		long duration = 0;
				
		for (int i = 0; i < 5 + times; i++) {
			long start = System.nanoTime();
			result = algorithm.run(data.getT(), data.getP());
			// Do 5 silent runs to get rid of JVM caching/profiling optimizations
			if (i < 5)
				continue;
			duration += System.nanoTime() - start;
			//System.out.println(TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - start));
		}
		
		return new SearchResult(algorithm, result.getMatches(), result.getOperations(), duration/times, times);
	}
}
