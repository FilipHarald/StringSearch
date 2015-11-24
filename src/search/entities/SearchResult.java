package search.entities;

import java.util.List;
import java.util.concurrent.TimeUnit;

import search.algorithms.Algorithm;

/**
 * Class representing a result of one or more searches
 * @author Albert Kaaman
 *
 */
public class SearchResult extends AlgorithmResult {

	private final String algorithm;
	private final int repeats;
	private final long duration;

	/**
	 * Constructs a new SearchResult instance
	 * @param algorithm Algorithm used in search
	 * @param matches Matches found
	 * @param operations Operations done
	 * @param duration Duration of search (averaged over number of repeats)
	 * @param repeats Number of times to repeat search
	 */
	public SearchResult(Algorithm algorithm, List<Integer> matches, long operations, long duration, int repeats) {
		super(matches, operations);
		this.algorithm = algorithm.getClass().getSimpleName();
		this.duration = duration;
		this.repeats = repeats;
	}

	/**
	 * @return Duration of search (averaged over number of repeats)
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @return Number of times search was repeated
	 */
	public int getRepeats() {
		return repeats;
	}

	/**
	 * @return Name of algorithm used in search
	 */
	public String getAlgorithm() {
		return algorithm;
	}

	@Override
	public String toString() {
		return "SearchResult [algorithm=" + algorithm + ", repeats=" + repeats + ", duration=" + TimeUnit.NANOSECONDS.toMicros(duration) + " μs"
				+ ", operations=" + getOperations()  + ", matches=" + getMatches() + "]";
	}
	
	
			
}
