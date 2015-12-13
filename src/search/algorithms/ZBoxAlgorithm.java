package search.algorithms;

import java.util.LinkedList;
import java.util.List;

import search.entities.AlgorithmResult;

/**
 * Implementation of the z-box algorithm. Code based on Javascript example code by Jesper Larsson.
 * @author Albert Kaaman
 */
public class ZBoxAlgorithm implements Algorithm {
	private char[] t;
	
	@Override
	public AlgorithmResult run(char[] p) {
		List<Integer> matches = new LinkedList<>();
		long operations = 0;
	
		// S = P$T, using null character as $
		char[] s = concat(p, concat(new char[] {'\0'}, t));	
	
		int[] z = new int[s.length];
		int k, r = 0, l = 0;
		int m;
		int kp;
				
		for (k = 1; k < s.length; k++) {
			/* If we're outside a z box, count as usual until we fall off.
			 * When we fall off, create a z box.
			 */
			if (k > r) {
				m = 0;
				while (k+m < s.length && s[k+m] == s[m]) { m++; operations++; }
				z[k] = m;
				if (m > 0) { r = k+m-1; l = k; }
			/* If we're inside a z box, check against prototype z-box,
			 * as far as possible.
			 */
			} else {
				kp = k-l;
				operations++;
				if (z[kp] < r-k+1) {
					z[k] = z[kp];
				} else {
					m = 1;
					while (r+m < s.length && s[r+m] == s[r-k+m]) { m++; operations++; }
					z[k] = r+m-k;
					r = r+m-1;
					l = k;
				}
			}

			// Check if we have a match
			operations++;
			if (z[k] == p.length) { matches.add(k - p.length - 1); }
		}

		return new AlgorithmResult(matches, operations);
	}
	
	@Override
	public AlgorithmResult run(char[][] patterns) {
		LinkedList<Integer> matches = new LinkedList<Integer>();
		long operations = 0;
		for (char[] p : patterns) {
			AlgorithmResult result = run(p);
			matches.addAll(result.getMatches());
			operations += result.getOperations();
		}
		
		return new AlgorithmResult(matches, operations);
	}

	/**
	 * Method to concatenate two char arrays
	 * @param a First array
	 * @param b Second array
	 * @return Concatenated array
	 */
	private static char[] concat(char[] a, char[] b) {
	   int aLen = a.length;
	   int bLen = b.length;
	   char[] c= new char[aLen+bLen];
	   System.arraycopy(a, 0, c, 0, aLen);
	   System.arraycopy(b, 0, c, aLen, bLen);
	   return c;
	}

	@Override
	public void preProcess(char[] t) {
		this.t = t;
	}

}
