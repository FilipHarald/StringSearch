package search.entities;

/**
 * Simple counter class
 * @author Albert Kaaman
 */
public class Counter {
	private long count;
	
	public void increment() {
		count += 1;
	}
	
	public long get() {
		return count;
	}
}
