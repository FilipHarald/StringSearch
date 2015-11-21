package search;

import java.util.LinkedList;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		 
		String t = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String p = "aaaaaaaaaaaaaaaaa";
		
		TestData data = new TestData(t.toCharArray(), p.toCharArray());
		
		List<Algorithm> algorithms = new LinkedList<>();
		
		AlgorithmTester.run(algorithms, data, 10);
		
	}
}
