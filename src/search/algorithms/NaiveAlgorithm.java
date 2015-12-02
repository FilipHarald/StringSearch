package search.algorithms;

import java.util.LinkedList;

import search.entities.AlgorithmResult;

/**
 * @author Filip Harald
 *
 */
public class NaiveAlgorithm implements Algorithm {
	private char[] t;
	
	@Override
	public AlgorithmResult run(char[] p) {
		LinkedList<Integer> matches = new LinkedList<Integer>();
		long operations = 0;
		for(int i = 0; i <= t.length-p.length; i++){
			boolean found = true;
			for(int j = 0; j < p.length; j++){
				operations++;
				if(t[i+j] != p[j]){
					found = false;
					break;
				}
			}
			if (found) matches.add(i);
		}
		return new AlgorithmResult(matches, operations);
	}
	
	@Override
	public void preProcess(char[] t) {
		this.t = t;
	}

}
