package search.algorithms;

import java.util.LinkedList;
import java.util.List;

import search.entities.AlgorithmResult;

public class ZBoxAlgorithm implements Algorithm {

	@Override
	public AlgorithmResult run(char[] t, char[] p) {
		List<Integer> matches = new LinkedList<>();
		long operations = 0;
		
		char[] s = concat(p, concat(new char[] {'\0'}, t));
		
		int[] z = new int[s.length];
		int k, r = 0, l = 0;
		int m;
		int kp;
				
		for (k = 1; k < s.length; k++) {
			if (k > r) {
				m = 0;
				while (k+m < s.length && s[k+m] == s[m]) { m++; operations++; }
				z[k] = m;
				if (m > 0) { r = k+m-1; l = k; }
			} else {
				kp = k-l;
				if (z[kp] < r-k+1) z[k] = z[kp];
				else {
					m = 1;
					while (r+m < s.length && s[r+m] == s[r-k+m]) { m++; operations++; }
					z[k] = r+m-k;
					r = r+m-1;
					l = k;
				}
			}
		}
		
		for (int i = p.length + 1; i < z.length; i++)
			if (z[i] == p.length) matches.add(i - (p.length+1));
		
		return new AlgorithmResult(matches, operations);
	}
	
	private static char[] concat(char[] a, char[] b) {
	   int aLen = a.length;
	   int bLen = b.length;
	   char[] c= new char[aLen+bLen];
	   System.arraycopy(a, 0, c, 0, aLen);
	   System.arraycopy(b, 0, c, aLen, bLen);
	   return c;
	}

}
