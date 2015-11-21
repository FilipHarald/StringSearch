package search;

import java.util.LinkedList;

/**
 * @author Filip Harald
 *
 */
public class NaiveAlgorithm implements Algorithm {
	
	@Override
	public AlgorithmResult run(char[] t, char[] p) {
		LinkedList<Integer> matches = new LinkedList<Integer>();
		long operations = 0;
		for(int i = 0; i <= t.length-p.length; i++){
			boolean found = true;
			for(int j = 0; j < p.length; j++){
				operations++;
				if(!(t[i+j] == p[j])){
					found = false;
					break;
				}
			}
			if(found) matches.add(i);
		}
		return new AlgorithmResult(matches, operations);
	}

}
