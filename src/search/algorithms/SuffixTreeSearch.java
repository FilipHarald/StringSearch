package search.algorithms;

import java.util.LinkedList;

import search.datastructures.SuffixTree;
import search.entities.AlgorithmResult;
import search.entities.Counter;

/**
 * Implementation of a Suffix Tree search. Uses the {@link search.datastructures.SuffixTree} class.
 * @author Albert Kaaman
 */
public class SuffixTreeSearch implements Algorithm {
	private SuffixTree st;

	@Override
	public AlgorithmResult run(char[] p) {
		// We're using a simple custom counter class here so that we can effectively pass
		// operations by reference to the find method.
		Counter operations = new Counter();

		return new AlgorithmResult(st.find(p, operations), operations.get());
	}
	
	@Override
	public AlgorithmResult run(char[][] patterns) {
		LinkedList<Integer> matches = new LinkedList<Integer>();
		Counter operations = new Counter();
		for (char[] p : patterns) {
			matches.addAll(st.find(p, operations));
		}
		
		return new AlgorithmResult(matches, operations.get());
	}

	@Override
	public void preProcess(char[] t){
		st = new SuffixTree(t);
	}


}
