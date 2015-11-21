package app;

import java.util.LinkedList;
import java.util.List;

import search.AlgorithmTester;
import search.algorithms.Algorithm;
import search.algorithms.NaiveAlgorithm;
import search.entities.TestData;

public class Main {

	public static void main(String[] args) {
		 
		String t = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
		String p = "aaaaaaaaaaaaaaaaa";
		
		TestData data = new TestData(t.toCharArray(), p.toCharArray());
		
		List<Algorithm> algorithms = new LinkedList<>();
		
		algorithms.add(new NaiveAlgorithm());
		
		AlgorithmTester.run(algorithms, data, 10);
		
	}
}
