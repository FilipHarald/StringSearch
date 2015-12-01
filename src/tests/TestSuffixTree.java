package tests;

import search.datastructures.SuffixTree;

public class TestSuffixTree {

	
	public static void main(String[] args) {
		
		SuffixTree st = new SuffixTree("abcabxabcd".toCharArray());
		
		// Den tomma noden 1284693 -> SuffixEdge [endNode=17225372, textIndex=1, length=0, text=] är den som ska var b, har fått length 0
		// Detta borde vara fixat!

		// Är något problem som får nod 1414644648 att hoppa av trädet i steg 7-9 nånstans

		//st.saveAsImage();
	}
}
