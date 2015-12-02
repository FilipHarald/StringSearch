package search.algorithms;

import java.util.LinkedList;
import java.util.List;

import search.entities.AlgorithmResult;

public class RabinKarpAlgorithm implements Algorithm {

	private static final int BIG_PRIME = 1664525;
	private static final int BIG_MOD = Integer.MAX_VALUE;
	
	private char[] t;
	private long squared;
	
	// TODO: Efter att ha lagt till BIG_MOD så funkar Jesus fortfarande inte, och nu funkar inte heller Jesu (vi har inte testat mindre)
	@Override
	public AlgorithmResult run(char[] p) {
		List<Integer> matches = new LinkedList<>();
		long operations = 0;
		
		squared = (long)Math.pow(BIG_PRIME, p.length - 1);
		long pHash = firstHash(p, p.length), tHash = firstHash(t, p.length);
//		System.out.println("pHash="+pHash);
//		System.out.println("tHash="+tHash);
		
		for (int i = 0; i < t.length - p.length + 1; i++) {
			if (i > 0)
				tHash = rollingHash(tHash, t, i - 1, i - 1 + p.length);
			
//			System.out.println("tHash="+tHash);
			
			if (tHash == pHash)
				if (equal(p, i, i + p.length - 1))
					matches.add(i);
		}
		
		
		return new AlgorithmResult(matches, operations);
	}

	@Override
	public void preProcess(char[] t) {
		this.t = t;
	}
	
	private boolean equal(char[] p, int left, int right) {
		int j = 0;
		for (int i = left; i <= right; i++) {
			if (t[i] != p[j++])
				return false;
		}
		return true;
	}
	
	private long firstHash(char[] a, int length) {
		long hash = 0;
		
		for (int i = 0; i < length; i++) {
			hash += a[i] * Math.pow(BIG_PRIME, (length - i - 1));
		}
		
		return hash % BIG_MOD;
	}
	
	private long rollingHash(long oldHash, char[] t, int oldLeft, int newRight) {
		long newHash = (BIG_PRIME * (oldHash - (t[oldLeft] * squared))) + t[newRight];
		
		return newHash % BIG_MOD;
	}

}
