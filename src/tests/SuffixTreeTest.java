package tests;

import static org.junit.Assert.*;

import org.junit.Test;

import junit.framework.TestCase;
import search.Search;
import search.algorithms.SuffixTreeSearch;
import search.entities.AlgorithmResult;
import search.entities.TestData;

import java.util.Collections;

public class SuffixTreeTest extends TestCase{

	private TestData data;
	private SuffixTreeSearch algorithm;
	private AlgorithmResult result;
	
	
	@Override
	protected void setUp() throws Exception {
		algorithm = new SuffixTreeSearch();
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception {
		data = null;
		algorithm = null;
		result = null;
		super.tearDown();
	}

	@Test
	public void testBible() {
		data = TestData.loadFiles("bible-en", "jesus");
		algorithm.preProcess(data.getT());
		result = algorithm.run(data.getP());
		Collections.sort(result.getMatches());
		assertTrue("First occurrance of Jesus", result.getMatches().get(0) == 3067567);
		assertTrue("Number of times Jesus is mentioned", result.getMatches().size() == 977);
	}
}
