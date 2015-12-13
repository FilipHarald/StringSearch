package app;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import search.AlgorithmTester;
import search.algorithms.*;
import search.entities.TestData;

public class Main {

	public static void main(String[] args) {

		TestData data;
		// Test 1: many-as with one pattern
		data = TestData.loadFiles("many-as");
		// Test 2: bible-en with one pattern
		//data = TestData.loadFiles("bible-en", "bible-en1");
		// Test 3: bible-en with 5 patterns
		//data = TestData.loadFiles("bible-en");
		// Test 4: skull with one pattern
		//data = TestData.loadFiles("skull");
		// Test 4: repeat with one pattern ??
		//data = TestData.loadFiles("repeat");


		List<Algorithm> algorithms = new LinkedList<>();
		
		algorithms.add(new NaiveAlgorithm());
		algorithms.add(new RabinKarpAlgorithm());
		algorithms.add(new SuffixTreeSearch());
		algorithms.add(new ZBoxAlgorithm());

		AlgorithmTester.run(algorithms, data, 100);
	}
}
