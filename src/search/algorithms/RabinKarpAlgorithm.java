package search.algorithms;

import java.util.LinkedList;
import java.util.List;

import search.entities.AlgorithmResult;

public class RabinKarpAlgorithm implements Algorithm {

	private static final int BIG_PRIME = 257;
	private static final long BIG_MOD = 1000000007;
	
	private char[] t;
	private long preSquared;
	
	@Override
	public AlgorithmResult run(char[] p) {
		List<Integer> matches = new LinkedList<>();
		long operations = 0;
		
		preSquared = 1;
		for (int i = 1; i <= p.length - 1; i++)
			preSquared = (BIG_PRIME * preSquared) % BIG_MOD;

		long pHash = firstHash(p, p.length), tHash = firstHash(t, p.length);
		//System.out.println("pHash="+pHash);

		for (int i = 0; i < t.length - p.length + 1; i++) {
			if (i > 0)
				tHash = rollingHash(tHash, t, i - 1, i - 1 + p.length);
			
			//System.out.println("tHash="+tHash);

			operations++;
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

	/**
	 * Computes initial hash for the substring a[0...length].
	 * Uses hash method from Algorithms 4th edition
	 *
	 * @param a char array
	 * @param length length of substring to hash
	 * @return hash of substring
	 */
	private long firstHash(char[] a, int length) {
		long hash = 0;
		
		for (int i = 0; i < length; i++) {
			hash = (BIG_PRIME * hash + a[i]) % BIG_MOD;
		}
		
		return hash;
	}

	/**
	 * Computes next rolling hash for substring t[oldLeft+1...newRight].
	 * Uses hash method from Algorithms 4th edition
	 *
	 * @param hash current hash
	 * @param t char array
	 * @param oldLeft previous index of leftmost character in substring
	 * @param newRight new index of rightmost character in substring
	 * @return new hash
	 */
	private long rollingHash(long hash, char[] t, int oldLeft, int newRight) {
		/**
		 * We add BIG_MOD to hash simply as a precaution if hash drops below 0.
		 * First line removes previous leftmost character, second line adds new rightmost character.
		 * After every computation we do modulus to keep hash from overflowing.
		 */
		hash = (hash + BIG_MOD - (preSquared * t[oldLeft] % BIG_MOD)) % BIG_MOD;
		hash = (hash * BIG_PRIME + t[newRight]) % BIG_MOD;

		return hash;
	}

}
