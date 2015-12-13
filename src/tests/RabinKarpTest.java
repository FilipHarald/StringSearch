package tests;

import org.junit.Test;

import junit.framework.TestCase;
import search.algorithms.Algorithm;
import search.algorithms.RabinKarpAlgorithm;
import search.entities.AlgorithmResult;
import search.entities.TestData;

public class RabinKarpTest extends TestCase{

	private TestData data;
	private Algorithm algorithm;
	private AlgorithmResult result;
	
	
	@Override
	protected void setUp() throws Exception {
		algorithm = new RabinKarpAlgorithm();
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
		assertTrue("First occurrance of Jesus", result.getMatches().get(0) == 3067567);
		assertTrue("Number of times Jesus is mentioned", result.getMatches().size() == 977);
	}
}
