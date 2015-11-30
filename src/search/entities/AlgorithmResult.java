package search.entities;

import java.util.List;

/**
 * Class representing a result of a single algorithm run 
 * @author Albert Kaaman
 *
 */
public class AlgorithmResult {
	private final List<Integer> matches;
	private final long operations;
	
	/**
	 * Constructs a new AlgorithmResult instance
	 * @param matches Matches found in run
	 * @param operations Operations done in run
	 */
	public AlgorithmResult(List<Integer> matches, long operations) {
		this.matches = matches;
		this.operations = operations;
	}

	/**
	 * @return Matches found in runs
	 */
	public List<Integer> getMatches() {
		return matches;
	}

	/**
	 * @return Operations done in run
	 */
	public long getOperations() {
		return operations;
	}
	
}
