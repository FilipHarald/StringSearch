package search;

public class Search {
	public static SearchResult run(Algorithm algorithm, TestData data, int times){
		
		AlgorithmResult result = null;
		long duration = 0;
		
		for (int i = 0; i < times; i++) {
			long start = System.nanoTime();
			result = algorithm.run(data.getT(), data.getP());
			duration += System.nanoTime() - start;
		}
		
		return new SearchResult(algorithm, result.getMatches(), result.getOperations(), duration, times);
	}
}
