package search.algorithms;

import java.util.LinkedList;
import java.util.List;

import search.datastructures.SuffixTree;
import search.entities.AlgorithmResult;
import search.entities.Counter;

public class SuffixTreeSearch implements Algorithm {
	private SuffixTree st;

	@Override
	public AlgorithmResult run(char[] p) {
		Counter operations = new Counter();
		//TODO: tar inte hänsyn till operations än!!!!
		return new AlgorithmResult(st.find(p, operations), operations.get());
	}

	@Override
	public void preProcess(char[] t){
		st = new SuffixTree(t);
	}


}
