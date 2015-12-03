package app;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import search.AlgorithmTester;
import search.algorithms.*;
import search.entities.TestData;

public class Main {

	public static void main(String[] args) {
		 
		TestData data = TestData.loadFiles("many-as");
		
		List<Algorithm> algorithms = new LinkedList<>();
		
		//System.out.println(TimeUnit.NANOSECONDS.toMicros(System.nanoTime()));
		//algorithms.add(new NaiveAlgorithm());
		//algorithms.add(new NaiveAlgorithm());
		
		
		algorithms.add(new RabinKarpAlgorithm());
		algorithms.add(new ZBoxAlgorithm());
		algorithms.add(new NaiveAlgorithm());
		
		
		//algorithms.add(new ZBoxAlgorithm());
		//algorithms.add(new NaiveAlgorithm());
		
		
		AlgorithmTester.run(algorithms, data, 20);
		
	}
}
