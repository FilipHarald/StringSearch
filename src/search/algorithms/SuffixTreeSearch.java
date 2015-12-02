package search.algorithms;

import java.util.LinkedList;
import java.util.List;

import search.datastructures.SuffixTree;
import search.entities.AlgorithmResult;

public class SuffixTreeSearch implements Algorithm {
	private SuffixTree st;

	@Override
	public AlgorithmResult run(char[] p) {
		List<Integer> matches = new LinkedList<>();
		long operations = 0;
		//TODO: tar inte hänsyn till operations än!!!!
		return new AlgorithmResult(st.find(p), operations);
	}

	@Override
	public void preProcess(char[] t){
		st = new SuffixTree(t);
	}


}
