package search.algorithms;

import java.util.LinkedList;
import java.util.List;

import search.entities.AlgorithmResult;

/**
 * Implementation of the Rabin-Karp search algorithm.
 * @author Albert Kaaman & Filip Harald
 */
public class RabinKarpAlgorithm implements Algorithm {

	private static final int BIG_PRIME = 257;
	private static final long BIG_MOD = 1000000007;
	
	private char[] t;
	private long preSquared;
	
	@Override
	public AlgorithmResult run(char[] p) {
		return run(new char[][]{p});
	}

	/**
	 * Runs the algorithm for multiple patterns. All patterns need to be the same length.
	 * @param patterns The patterns to search for.
	 * @return Result of search
	 */
	@Override
	public AlgorithmResult run(char[][] patterns) {
		List<Integer> matches = new LinkedList<>();
		long operations = 0;
		long[] pHashes = new long[patterns.length];

		// We precompute the square of BIG_PRIME to save time when computing the next rolling hash
		preSquared = 1;
		for (int i = 1; i <= patterns[0].length - 1; i++)
			preSquared = (BIG_PRIME * preSquared) % BIG_MOD;

		// We hash all patterns
		for (int i = 0; i < patterns.length; i++)
			pHashes[i] = firstHash(patterns[i], patterns[i].length);

		long tHash = firstHash(t, patterns[0].length);

		for (int i = 0; i < t.length - patterns[0].length + 1; i++) {
			if (i > 0)
				tHash = rollingHash(tHash, t, i - 1, i - 1 + patterns[0].length);
			
			operations++;
			for (int j = 0; j < pHashes.length; j++) {
				if (tHash == pHashes[j])
					if (equal(patterns[j], i, i + patterns.length - 1))
						matches.add(i);
			}
				
		}
		
		return new AlgorithmResult(matches, operations);
	}

	@Override
	public void preProcess(char[] t) {
		this.t = t;
	}

	/**
	 * Checks if the array {@code p} equals the subarray of {@code t} starting from {@code left} and ending in {@code right}
	 * @param p Array to check
	 * @param left Starting index of subarray in {@code t}
	 * @param right Ending index of subarray in {@code t}
	 * @return True if arrays are equal, otherwise false
	 */
	private boolean equal(char[] p, int left, int right) {
		int j = 0;
		for (int i = left; i <= right; i++) {
			if (t[i] != p[j++])
				return false;
		}
		return true;
	}

	/**
	 * Computes initial hash for the substring {@code a}[0...{@code length}].
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
	 * Computes next rolling hash for subarray {@code t}[{@code oldLeft}+1...{@code newRight}].
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
