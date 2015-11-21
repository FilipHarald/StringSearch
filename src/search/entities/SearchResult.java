package search.entities;

import java.util.List;
import java.util.concurrent.TimeUnit;

import search.algorithms.Algorithm;

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
		return "SearchResult [algorithm=" + algorithm + ", times=" + times + ", duration=" + TimeUnit.NANOSECONDS.toMicros(duration) + " μs"
				+ ", operations=" + getOperations()  + ", matches=" + getMatches() + "]";
	}
	
	
			
}
