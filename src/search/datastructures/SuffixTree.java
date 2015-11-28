package search.datastructures;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;

import javax.imageio.ImageIO;

public class SuffixTree {
	private final char[] text;
	private final SuffixNode root;
	private final Map<SuffixKey, SuffixEdge> hashMap;
	private int length;
	
	public SuffixTree(char[] text) {
		this.text = text;
		root = new SuffixNode(0);
		hashMap = new HashMap<>(text.length);
		
		ActivePoint activePoint = new ActivePoint(root, '\0', 0);
		int remainder = 1;
		
		for (char c : text) {
			System.out.println("/////////////////////////////////////////////////////////////////////////////////////////////////////////");
			this.add(c, remainder, activePoint, null);
			length++;
			System.out.println(toString());
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
		
		SuffixEdge edge = hashMap.get(new SuffixKey(activePoint.node, temp));
		
		System.out.println("found edge = " + edge);
		
		if (edge != null) {
			
			if (text[edge.textIndex + activePoint.length] == c) {
				if (temp == c) {					
					activePoint.edgeFirstChar = c;
				}
				activePoint.length++;
				System.out.println("length++");
				remainder++;
				
				if (activePoint.length == edge.length) {
					activePoint.node = edge.endNode;
					activePoint.edgeFirstChar = '\0';
					activePoint.length = 0;
				}
				
			} else {
				
				edge.length = edge.textIndex + activePoint.length - 1;
				
				SuffixEdge newEdge = new SuffixEdge(new SuffixNode(), edge.textIndex + activePoint.length, -1);
				SuffixEdge newEdge2 = new SuffixEdge(new SuffixNode(), length, -1);
				
				hashMap.put(new SuffixKey(edge.endNode, text[edge.textIndex + activePoint.length]), newEdge);
				hashMap.put(new SuffixKey(edge.endNode, c), newEdge2);
				
				remainder--;
				
				if (activePoint.node.equals(root)) {
					activePoint.length--;
					if (activePoint.length == 0)
						activePoint.edgeFirstChar = c;
					else
						activePoint.edgeFirstChar = text[edge.textIndex + 1];
					System.out.println("length--");
				} else {
					activePoint.node = activePoint.node.suffixLink != null ? activePoint.node.suffixLink : root;
				}
				
				if (prevSplitNode != null)
					prevSplitNode.suffixLink = edge.endNode;
								
				add(c, remainder, activePoint, edge.endNode);
				
			}
			
		} else {
			System.out.println("got here");
			edge = new SuffixEdge(new SuffixNode(), length, -1);
			
			hashMap.put(new SuffixKey(root, c), edge);
			
			activePoint.edgeFirstChar = '\0';
		}
		
	}

	public void saveAsImage() {
		DefaultDirectedGraph<SuffixNode, SuffixEdge> g = new DefaultDirectedGraph<SuffixNode, SuffixEdge>(SuffixEdge.class);

		for (Map.Entry<SuffixKey, SuffixEdge> entry : hashMap.entrySet()) {
			SuffixNode start = entry.getKey().node;
			SuffixNode end = entry.getValue().endNode;

			g.addVertex(start);
			g.addVertex(end);
			g.addEdge(start, end, entry.getValue());
		}

		JGraphXAdapter<SuffixNode, SuffixEdge> jgxa = new JGraphXAdapter<SuffixNode, SuffixEdge>(g);

		mxIGraphLayout asd = new mxHierarchicalLayout(jgxa);

		asd.execute(jgxa.getDefaultParent());

		BufferedImage bi = mxCellRenderer.createBufferedImage(jgxa, null, 1, Color.WHITE, true, null);

		try {
			ImageIO.write(bi, "PNG", new File("graph.png"));
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
			int end = length < 0 ? SuffixTree.this.length : length + 1;
			for (int i = textIndex; i < end ; i++)
				sb.append(text[i]);

			return "SuffixEdge [endNode=" + endNode + ", textIndex=" + textIndex + ", length=" + length + ", text=" + sb.toString() + "]";
			//return sb.toString();
		}
				
	}
	
	private class SuffixNode {
		private SuffixNode suffixLink;
		private int id;

		public SuffixNode(int id) {
			this.id = id;
		}

		public SuffixNode() {
			this.id = hashCode();
		}

		@Override
		public String toString() {
			//return "";
			return Integer.toString(hashCode());
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

