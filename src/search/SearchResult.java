package search;

import java.util.List;

public class SearchResult extends AlgorithmResult {

	private final long duration;

	public SearchResult(List<Integer> matches, long operations, long duration) {
		super(matches, operations);
		this.duration = duration;
	}

	public long getDuration() {
		return duration;
	}
		
}
