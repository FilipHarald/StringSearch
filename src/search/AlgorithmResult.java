package search;

import java.util.List;

public class AlgorithmResult {
	private final List<Integer> matches;
	private final long operations;
	
	public AlgorithmResult(List<Integer> matches, long operations) {
		this.matches = matches;
		this.operations = operations;
	}

	public List<Integer> getMatches() {
		return matches;
	}

	public long getOperations() {
		return operations;
	}
	
}
