package search;

import java.util.List;

public class SearchResult extends AlgorithmResult {

	private final String algorithm;
	private final int times;
	private final long duration;

	public SearchResult(Algorithm algorithm, List<Integer> matches, long operations, long duration, int times) {
		super(matches, operations);
		this.algorithm = algorithm.getClass().getSimpleName();
		this.duration = duration;
		this.times = times;
	}

	public long getDuration() {
		return duration;
	}

	public int getTimes() {
		return times;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	@Override
	public String toString() {
		return "SearchResult [algorithm=" + algorithm + ", times=" + times + ", duration=" + duration
				+ ", matches=" + getMatches() + ", operations=" + getOperations() + "]";
	}
	
	
			
}
