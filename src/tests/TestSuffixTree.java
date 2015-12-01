package tests;

import java.util.List;

import search.datastructures.SuffixTree;
import search.entities.TestData;

public class TestSuffixTree {

	
	public static void main(String[] args) {
		
		TestData data = TestData.loadFiles("alphabet");
		
		//SuffixTree st = new SuffixTree("abcabxabcd$".toCharArray());
		SuffixTree st = new SuffixTree(data.getT());
		
		// Den tomma noden 1284693 -> SuffixEdge [endNode=17225372, textIndex=1, length=0, text=] är den som ska var b, har fått length 0
		// Detta borde vara fixat!

		// Är något problem som får nod 1414644648 att hoppa av trädet i steg 7-9 nånstans

		//st.saveAsImage(1);
		
		
		/*
		List<Integer> matches = st.find("jo");
		System.out.println("Matches=");
		for (int m : matches)
			System.out.println(m);
		*/
	}
}
