package app;

import java.util.LinkedList;
import java.util.List;

import search.AlgorithmTester;
import search.algorithms.*;
import search.entities.TestData;

public class Main {

	public static void main(String[] args) {
		 
		TestData data = TestData.loadFiles("alphabet");
		
		List<Algorithm> algorithms = new LinkedList<>();
		
		
		//algorithms.add(new NaiveAlgorithm());
		//algorithms.add(new NaiveAlgorithm());
		//algorithms.add(new NaiveAlgorithm());
		//algorithms.add(new NaiveAlgorithm());
		algorithms.add(new ZBoxAlgorithm());
		algorithms.add(new ZBoxAlgorithm());
		//algorithms.add(new NaiveAlgorithm());
		
		
		AlgorithmTester.run(algorithms, data, 10);
		
	}
}
