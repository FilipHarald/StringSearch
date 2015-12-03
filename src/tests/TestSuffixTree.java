package tests;

import java.util.List;

import search.datastructures.SuffixTree;
import search.entities.TestData;

public class TestSuffixTree {

	
	public static void main(String[] args) {

		TestData data = TestData.loadFiles("lorem");

		//SuffixTree st = new SuffixTree("abcabxabcd$".toCharArray()); // FUNGERAR!
		//SuffixTree st = new SuffixTree("joijoijoi$".toCharArray()); // FUNGERAR!
		//SuffixTree st = new SuffixTree("abc abd al$".toCharArray()); // FUNGERAR!
		//SuffixTree st = new SuffixTree("the b lit a the w lit$".toCharArray()); // VERKAR FUNGERA!
		//SuffixTree st = new SuffixTree("the good and the bad$".toCharArray()); // VERKAR FUNGERA!
		SuffixTree st = new SuffixTree(data.getT());
		//SuffixTree st = new SuffixTree("cdddcdc$".toCharArray());
		
		// Den tomma noden 1284693 -> SuffixEdge [endNode=17225372, textIndex=1, length=0, text=] är den som ska var b, har fått length 0
		// Detta borde vara fixat!

		// Är något problem som får nod 1414644648 att hoppa av trädet i steg 7-9 nånstans

		//st.saveAsImage(1);
		//st.showInWindow();

		// TODO: works up until adding last character $ (it add it starting at 19, but it starts at 18). Start checking around Counter == 26
		
		List<Integer> matches = st.find("s");
		
		System.out.println("Matches=");
		for (int m : matches)
			System.out.println(m);
		
		
	}
}
