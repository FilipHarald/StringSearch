package search.algorithms;

import java.util.LinkedList;
import java.util.List;

import search.datastructures.SuffixTree;
import search.entities.AlgorithmResult;

public class SuffixTreeSearch implements Algorithm {

	@Override
	public AlgorithmResult run(char[] t, char[] p) {
		List<Integer> matches = new LinkedList<>();
		long operations = 0;

		SuffixTree st = new SuffixTree(t);
		
		
		
		
		return new AlgorithmResult(matches, operations);
	}
	

}
