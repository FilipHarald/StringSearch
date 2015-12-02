package search.datastructures;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.List;

import com.mxgraph.layout.hierarchical.*;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.layout.*;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;

import javax.imageio.ImageIO;

public class SuffixTree {
	private static int nodeId = 0;
	private final char[] text;
	private final SuffixNode root;
	private final Map<SuffixKey, SuffixEdge> hashMap;
	private int length;
	
	public SuffixTree(char[] text) {
		this.text = text;
		root = new SuffixNode();
		hashMap = new HashMap<>(text.length);

		build();

		/*
		ActivePoint activePoint = new ActivePoint(root, '\0', 0);
		int remainder = 0;
		
		for (char c : text) {
			System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////////////");
			this.add(c, remainder, activePoint, null);
			this.saveAsImage(length);
			length++;
			System.out.println("Length = " + length);
			//System.out.println(toString());
			
		}*/
		
	}

	private void build() {
		ActivePoint activePoint = new ActivePoint(root, '\0', 0);

		int i = 0, j = 0;
		while (i < text.length) {
			SuffixNode previouslyInsertedNode = null;
			int remainder = 1;
			while (remainder >= 1) {
				//length = j;
				System.out.println("remainder = " + remainder);
				System.out.println(activePoint);
				System.out.println("i="+i+",j="+j);
				StringBuilder sb = new StringBuilder();
				for (int z = i; z < j + 1; z++)
					sb.append(text[z]);
				System.out.println("Adding suffix " + sb.toString());

				char temp = activePoint.edgeFirstChar != '\0' ? activePoint.edgeFirstChar : text[i + activePoint.length];
				System.out.println("Getting edge with first char = " + temp);
				SuffixEdge edge = activePoint.node.getEdge(temp);

				System.out.println("Found edge = " + edge);

				if (edge != null) {
					System.out.println("text[" + (edge.textIndex + activePoint.length) + "] (" + text[edge.textIndex + activePoint.length] + ") == [" + (i + activePoint.length) + "] (" + text[i + activePoint.length] + ")?");
					if (text[edge.textIndex + activePoint.length] == text[i + activePoint.length]) {
						activePoint.edgeFirstChar = temp;
						activePoint.length++;
						remainder++;
						j++;
						length += 1;

						System.out.println("length++ (" + activePoint.length + ")");
						if (activePoint.length == edge.length) { // + 1
							System.out.println("activePoint length is equal to current edge length, moving activePoint node");
							activePoint.node = edge.endNode;
							activePoint.edgeFirstChar = '\0';
							activePoint.length = 0;
							i = j;
							remainder--;
						}
					} else {
						System.out.println("Splitting!");
						System.out.println("Remainder = " + remainder);

						SuffixNode insertedNode = new SuffixNode();
						SuffixNode oldNode = edge.endNode;
						int oldLength = edge.length;

						edge.endNode = insertedNode;
						edge.length = activePoint.length;

						System.out.println("New (old) edge = " + edge);

						int l = oldNode.hashMap.size() > 0 ? Math.abs(oldLength - edge.length) : -1;
						SuffixEdge newEdge = new SuffixEdge(oldNode, edge.textIndex + edge.length, l);
						SuffixEdge newEdge2 = new SuffixEdge(new SuffixNode(), j, -1);

						System.out.println("New edge 1 = " + newEdge);
						System.out.println("New edge 2 = " + newEdge2);

						for (Map.Entry<Character, SuffixEdge> child : edge.endNode.hashMap.entrySet()) {
							child.getValue().textIndex -= 1;
						}

						insertedNode.addEdge(text[edge.textIndex + edge.length], newEdge);
						insertedNode.addEdge(text[i + activePoint.length], newEdge2);

						remainder--;

						if (activePoint.node.equals(root)) {
							i++;
							activePoint.length--;
							if (activePoint.length == 0) {
								activePoint.edgeFirstChar = text[i];
							} else
								activePoint.edgeFirstChar = text[edge.textIndex + 1]; // + 1
							System.out.println("length-- (" + activePoint.length + ")");
						} else {
							System.out.println("Following suffix link to node = " + activePoint.node.suffixLink);
							activePoint.node = activePoint.node.suffixLink != null ? activePoint.node.suffixLink : root;
							activePoint.edgeFirstChar = text[i];

						}

						edge.endNode.suffixLink = root;
						if (previouslyInsertedNode != null)
							previouslyInsertedNode.suffixLink = edge.endNode;
						previouslyInsertedNode = edge.endNode;

						this.saveAsImage(i);
					}

				} else {
					SuffixEdge newEdge = new SuffixEdge(new SuffixNode(), i, -1);
					activePoint.node.addEdge(text[i], newEdge);
					remainder--;
					i++;
					j=i;
					if (i < text.length) {
						activePoint.edgeFirstChar = text[i];
						length += 1;
					}
					this.saveAsImage(i);
				}

			}
			length = i;

			this.saveAsImage(i);
		}
	}
	
	private void add(char c, int remainder, ActivePoint activePoint, SuffixNode prevSplitNode) {

		System.out.println(activePoint);
		System.out.println("Adding " + c);

		char temp;
		if (activePoint.edgeFirstChar != '\0') {
			temp = activePoint.edgeFirstChar;
		} else {
			temp = c;
		}
		
		//SuffixEdge edge = hashMap.get(new SuffixKey(activePoint.node, temp));
		SuffixEdge edge = activePoint.node.hashMap.get(temp);
		
		System.out.println("found edge = " + edge);
		
		if (edge != null) {
			
			if (text[edge.textIndex + activePoint.length] == c) {
//				System.out.println("Found char on current edge");
				
				if (temp == c) {					
					activePoint.edgeFirstChar = c;
				}
				activePoint.length++;
				System.out.println("length++ (" + activePoint.length + ")");
				remainder++;
				
				if (activePoint.length == edge.length) { // + 1
					activePoint.node = edge.endNode;
					activePoint.edgeFirstChar = '\0';
					activePoint.length = 0;
				}
				
			} else {
				//System.out.println("Splitting edge " + edge);
				edge.length = length - activePoint.length - edge.textIndex; /*edge.textIndex + activePoint.length - 1; */
				
				System.out.println("New current edge = " + edge);
				
				SuffixEdge newEdge = new SuffixEdge(new SuffixNode(), edge.textIndex + edge.length, -1);
				SuffixEdge newEdge2 = new SuffixEdge(new SuffixNode(), length, -1);
				
				System.out.println("New edge 1 = " + newEdge);
				System.out.println("New edge 2 = " + newEdge2);
				
				for (Map.Entry<Character, SuffixEdge> child : edge.endNode.hashMap.entrySet()) {
					child.getValue().textIndex -= 1;
				}
				
				//hashMap.put(new SuffixKey(edge.endNode, text[edge.textIndex + activePoint.length]), newEdge);
				edge.endNode.hashMap.put(text[edge.textIndex + activePoint.length], newEdge);
				//hashMap.put(new SuffixKey(edge.endNode, c), newEdge2);
				edge.endNode.hashMap.put(c, newEdge2);
				
				remainder--;
				
				if (activePoint.node.equals(root)) {
					activePoint.length--;
					if (activePoint.length == 0)
						activePoint.edgeFirstChar = c;
					else
						activePoint.edgeFirstChar = text[edge.textIndex + 1]; // + 1
					System.out.println("length-- (" + activePoint.length + ")");
				} else {
					activePoint.node = activePoint.node.suffixLink != null ? activePoint.node.suffixLink : root;
				}

				if (prevSplitNode != null)
					prevSplitNode.suffixLink = edge.endNode;
								
				add(c, remainder, activePoint, edge.endNode);
				
			}
			
		} else {
			System.out.println("got here");
			
			if (!activePoint.node.equals(root))
				activePoint.node.hashMap.put(c, new SuffixEdge(new SuffixNode(), length, -1));
				//hashMap.put(new SuffixKey(activePoint.node, c), new SuffixEdge(new SuffixNode(), length, -1));
			
			//hashMap.put(new SuffixKey(root, c), new SuffixEdge(new SuffixNode(), length, -1));
			root.hashMap.put(c, new SuffixEdge(new SuffixNode(), length, -1));
			
			
			//activePoint.edgeFirstChar = '\0';
			//activePoint.node = root;
		}
		
	}
	
	private void findLeaves(List<Integer> matches, SuffixNode node, int currentLength) {
		System.out.println(node);
		for (Map.Entry<Character, SuffixEdge> entry : node.hashMap.entrySet()) {
			if (entry.getValue().endNode.hashMap.size() <= 0) {
				matches.add(entry.getValue().textIndex - currentLength);
			} else {
				findLeaves(matches, entry.getValue().endNode, currentLength + entry.getValue().length);
			}
		}		
	}
	
	public List<Integer> find(String pattern) {
		List<Integer> matches = new LinkedList<>();
		
		SuffixNode node = root;
		int currentIndex = 0;
		int currentLength = 0;
		
		while (node != null) {
			System.out.println(node);
			if (currentLength == pattern.length()) {
				System.out.println("here");
				// If we've gotten to a node and currentLength is equal to pattern length, then all leaf nodes starting from this node are matching suffixes to pattern
				/*
				int suffixLength = currentLength;
				for (Map.Entry<Character, SuffixEdge> entry : node.hashMap.entrySet()) {
					SuffixNode endNode = entry.getValue().endNode;
					
					if (endNode.hashMap.size() > 0) {
						suffixLength += entry.getValue().length;
					} else {
						suffixLength -= 
						matches.add(entry.getValue().textIndex - suffixLength);
					}	
				}
				*/
				findLeaves(matches, node, currentLength);
				
				return matches;
				
			} else {
				//SuffixEdge edge = hashMap.get(new SuffixKey(node, pattern.charAt(currentIndex)));			
				SuffixEdge edge = node.hashMap.get(pattern.charAt(currentIndex));
				System.out.println(edge);
				SuffixNode nextNode = null;
				if (edge != null) {
					nextNode = edge.endNode;
					int end = edge.length < 0 ? text.length - edge.textIndex : edge.length;
					for (int i = 0; i < end; i++) {
						if (currentIndex + i >= pattern.length()) {
							matches.add(edge.textIndex - (i - currentIndex) - 1);
							return matches;  // single match since we finished on an edge
						} else if (text[edge.textIndex + i] != pattern.charAt(currentIndex + i)) {
							return matches; // matches is empty at this point
						}
					}
					currentLength += edge.length;
					node = nextNode;
					currentIndex = currentIndex + edge.length;
				}
			}
			
		}
		return null;
	}
	
	private void addEdges(DefaultDirectedGraph<SuffixNode, SuffixEdge> g, SuffixNode n) {
//		if (!g.containsVertex(n))
//			g.addVertex(n);
		for (Map.Entry<Character, SuffixEdge> child : n.hashMap.entrySet()) {
			g.addVertex(child.getValue().endNode);
			g.addEdge(n, child.getValue().endNode, child.getValue());
			addEdges(g, child.getValue().endNode);
		}
	}

	public void saveAsImage(int length) {
		DefaultDirectedGraph<SuffixNode, SuffixEdge> g = new DefaultDirectedGraph<SuffixNode, SuffixEdge>(SuffixEdge.class);

		/*
		for (Map.Entry<SuffixKey, SuffixEdge> entry : hashMap.entrySet()) {
			SuffixNode start = entry.getKey().node;
			SuffixNode end = entry.getValue().endNode;

			if (!g.containsVertex(start))
				g.addVertex(start);
			if (!g.containsVertex(end))
				g.addVertex(end);
			g.addEdge(start, end, entry.getValue());
		}*/
		
		g.addVertex(root);
		addEdges(g, root);
		
		JGraphXAdapter<SuffixNode, SuffixEdge> jgxa = new JGraphXAdapter<SuffixNode, SuffixEdge>(g);

		mxIGraphLayout asd = new mxHierarchicalLayout(jgxa);

		asd.execute(jgxa.getDefaultParent());

		BufferedImage bi = mxCellRenderer.createBufferedImage(jgxa, null, 1, Color.WHITE, true, null);

		try {
			ImageIO.write(bi, "PNG", new File("graph" + length + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<SuffixKey, SuffixEdge> entries : hashMap.entrySet()) {
			
			sb.append(entries.getKey().node.toString() + " -> " + entries.getValue().toString() + "\n");
			
		}
		
		return sb.toString();
	}

	private class SuffixEdge {
		private SuffixNode endNode;
		private int textIndex;
		private int length;

		public SuffixEdge() {
			super();
		}

		public SuffixEdge(SuffixNode endNode, int index, int length) {
			this.endNode = endNode;
			this.textIndex = index;
			this.length = length;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			int end = length < 0 ? SuffixTree.this.length : textIndex + length;
			for (int i = textIndex; i < end ; i++)
				sb.append(text[i]);

			sb.append("["+textIndex+","+length+"]");
			//return "SuffixEdge [endNode=" + endNode + ", textIndex=" + textIndex + ", length=" + length + ", text=" + sb.toString() + "]";
			return sb.toString();
		}
				
	}
	
	private class SuffixNode {
		private int id;
		private SuffixNode suffixLink = null;
		private final Map<Character, SuffixEdge> hashMap = new HashMap<>();

		public SuffixNode(int id) {
			this.id = id;
		}

		public SuffixNode() {
			this.id = SuffixTree.nodeId++;
		}

		@Override
		public String toString() {
			String s = String.format("%010d", id);
			if (suffixLink != null)
				return s + "\n(" + suffixLink.id + ")";
			else
				return s;
		}

		public SuffixEdge getEdge(char c) {
			return hashMap.get(c);
		}

		public void addEdge(char c, SuffixEdge e) {
			hashMap.put(c, e);
		}
		
		
	}
	
	private class ActivePoint {
		private SuffixNode node = null;
		private char edgeFirstChar = '\0';
		private int length = 0;
		
		public ActivePoint(SuffixNode node, char edgeFirstChar, int length) {
			this.node = node;
			this.edgeFirstChar = edgeFirstChar;
			this.length = length;
		}

		@Override
		public String toString() {
			return "ActivePoint{" +
					"node=" + node +
					", edgeFirstChar=" + edgeFirstChar +
					", length=" + length +
					'}';
		}
	}
	
	private class SuffixKey {
		private SuffixNode node;
		private char edgeFirstChar;

		public SuffixKey(SuffixNode node, char edgeFirstChar) {
			this.node = node;
			this.edgeFirstChar = edgeFirstChar;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + edgeFirstChar;
			result = prime * result + ((node == null) ? 0 : node.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SuffixKey other = (SuffixKey) obj;
			
			if (edgeFirstChar != other.edgeFirstChar)
				return false;
			if (node == null) {
				if (other.node != null)
					return false;
			} else if (!node.equals(other.node))
				return false;
			return true;
		}


	}
}

