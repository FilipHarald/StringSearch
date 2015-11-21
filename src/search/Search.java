package search;

import java.util.LinkedList;

public abstract class Search {
	private char[] p;
	private char[] t;
	private float operations;
	
	public Search(char[] p, char[] t, float operations){
		this.p = p;
		this.t = t;
		this.operations = operations;
	}
	
	public LinkedList<Integer> run(){
		return new LinkedList<Integer>();
	}
}
