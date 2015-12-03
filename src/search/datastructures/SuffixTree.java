package search.datastructures;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.List;

import com.mxgraph.layout.hierarchical.*;
import com.mxgraph.layout.orthogonal.mxOrthogonalLayout;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.layout.*;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.view.mxGraph;

import org.jgrapht.ext.JGraphModelAdapter;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;

import javax.imageio.ImageIO;
import javax.swing.*;

public class SuffixTree implements Serializable {
	private static int nodeId = 0;
	private final char[] text;
	private final SuffixNode root;
	private final Map<SuffixKey, SuffixEdge> hashMap;
	private int length;

	private SuffixNode needSuffixLink = null;
	
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
		//if (text.length <= 50)
			//this.saveAsImage(0);
		//this.saveAsSVG();

	}

	private void addSuffixLink(SuffixNode node) {
		if (needSuffixLink != null && !needSuffixLink.equals(root)) {
			if (needSuffixLink.suffixLink == null)
				needSuffixLink.suffixLink = node;
		}
		needSuffixLink = node;
	}

	private void build() {
		SuffixNode activeNode = root;
		char activeEdgeFirstChar = '\0';
		int activeLength = 0;

		int currentSuffixStart = 0;
		int currentSuffixEnd = 0;

		int counter = 0;

		while (currentSuffixStart < text.length) {
			
			// at beginning of every suffix we reset remaining counter and previously inserted pointer
			needSuffixLink = null;
			int remainingSuffixes = 1;
			activeLength = 0;

			// continue looping as long as we have remaining suffixes to insert
			while (remainingSuffixes >= 1) {
				length = currentSuffixEnd;
				this.saveAsImage(counter++);
				
				char edgeFirstChar = activeEdgeFirstChar != '\0' ? activeEdgeFirstChar : text[currentSuffixStart + activeLength];
				SuffixEdge foundEdge = activeNode.getEdge(edgeFirstChar);

				if (foundEdge != null) {

					if (foundEdge.length > 0 && activeLength >= foundEdge.length) {
						// If activeLength is equal to foundEdge.length then we've hit the end of the current edge.
						// We need to
						// 1) Set the new activeNode to the edge's endNode so that we can continue looking for insertion points
						// 2) Reset activeLength to 0 since we're on a new edge
						// 3) Reset activeEdgeFirstChar since we're on a new edge (and a new suffix)
						// 4) Set the start position of our new current suffix to the end position of the old one
						// The implicit suffix that we've 'forgotten' by resetting these things is still accounted for <-- IS THIS RIGHT??

						activeNode = foundEdge.endNode;
						activeLength = 0;
						//activeEdgeFirstChar = text[currentSuffixStart + activeLength];
						activeEdgeFirstChar = '\0';
						//currentSuffixStart = currentSuffixEnd;
						currentSuffixStart += foundEdge.length;

					} else if (text[foundEdge.textIndex + activeLength] == text[currentSuffixStart + activeLength]) {
						// If we can travel along the found edge using the current suffix to insert, we
						// 1) increment the activeLength (current position on edge)
						// 2) increment remainingSuffixes, since we've only implicitly added the suffix
						// 3) increment currentSuffixEnd, since we need to keep track of the end of the current suffix

						activeEdgeFirstChar = edgeFirstChar;
						activeLength++;
						remainingSuffixes++;
						currentSuffixEnd++;

						addSuffixLink(activeNode);

					} else {
						// If we've dropped off the edge somewhere in the middle of it we need to split the edge by doing
						// 1) Creating a new node to insert, save the old node (it will be the end node of the edge with the remaining suffix from the split)
						// 2) Calculate the length of new edge going from inserted node with remaining suffix
						// 3) Set the foundEdge's endNode to new inserted node
						// 4) Set the foundEdge's new length to the current active point on the edge (activeLength)
						// 5) Create edge for remaining suffix and new suffix end, and add edges to insertedNode

						SuffixNode insertedNode = new SuffixNode();

						SuffixNode oldNode = foundEdge.endNode;
						int newLength = oldNode.hashMap.size() > 0 ? Math.abs(foundEdge.length - activeLength) : -1;

						foundEdge.endNode = insertedNode;
						foundEdge.length = activeLength;

						SuffixEdge edgeRemaining = new SuffixEdge(oldNode, foundEdge.textIndex + foundEdge.length, newLength);
						SuffixEdge edgeNewSuffix = new SuffixEdge(new SuffixNode(), currentSuffixEnd, -1);
						insertedNode.addEdge(text[foundEdge.textIndex + foundEdge.length], edgeRemaining);
						insertedNode.addEdge(text[currentSuffixStart + activeLength], edgeNewSuffix);

						addSuffixLink(insertedNode);

						// After all that stuff is done, we decrement remainingSuffixes since we just inserted one (by splitting an edge)
						remainingSuffixes--;

						// If the current active node is the root, we can
						// 1) Increment currentSuffixStart to try and insert the next remaining suffix
						// 2) Decrement activeLength to keep in sync with current suffix
						if (activeNode.equals(root)) {
							currentSuffixStart++;
							activeLength--;
							activeEdgeFirstChar = text[currentSuffixStart];
						} else {
							// If the current active node is not the root, then we follow the suffix link of the active node
							activeNode = activeNode.suffixLink;
						}

					}
				} else {
					// If we get there then we didn't find an outgoing edge from the activeNode that started with the char found in currentSuffixStart
					// 1) Add that char as a new suffix (with an implicit endless length)
					// 2) Decrement remainingSuffixes since we just added one
					SuffixEdge newEdge = new SuffixEdge(new SuffixNode(), currentSuffixStart, -1);
					activeNode.addEdge(text[currentSuffixStart], newEdge);
					remainingSuffixes--;

					addSuffixLink(activeNode);

					// Do we have to follow the suffix link here too??
					if (!activeNode.equals(root))
						activeNode = activeNode.suffixLink;
					else {
						// If we're on the root, simply increment suffix start and end to point at the next character
						currentSuffixStart++;
						currentSuffixEnd = currentSuffixStart;
						activeEdgeFirstChar = '\0';

					}
				}
			}
		}

		length = text.length;
	}
	
	private void findLeaves(List<Integer> matches, SuffixNode node, int currentLength) {
		if (node.hashMap.size() > 0) {
			for (Map.Entry<Character, SuffixEdge> entry : node.hashMap.entrySet()) {
				System.out.println("Edge = " + entry.getValue());
				if (entry.getValue().endNode.hashMap.size() <= 0) {
					System.out.println("At leaf node " + entry.getValue().endNode);
					matches.add(entry.getValue().textIndex - currentLength);
				} else {
					findLeaves(matches, entry.getValue().endNode, currentLength + entry.getValue().length);
				}
			}
		} 
	}
	
	public List<Integer> find(String pattern) {
		return find(pattern.toCharArray());
	}

	// TODO: We have to handle case where pattern is shorter than an edge, there might still be multiple leaf nodes
	// Example T="the good and the bad", P="the"
	// If there is an edge "the " (note space!) with child edges "good and the bad" and "bad", we need both!
	public List<Integer> find(char[] pattern) {
		List<Integer> matches = new LinkedList<>();
		
		SuffixNode node = root;
		int currentIndex = 0;
		boolean matchFound = false;
		
		while (node != null) {
			
			SuffixEdge edge = node.hashMap.get(pattern[currentIndex]);
			System.out.println("Found edge with char " + pattern[currentIndex] + " = " + edge);
			node = null;
			if (edge != null) {
				node = edge.endNode;
				int edgeLength = edge.length < 0 ? text.length - edge.textIndex : edge.length;
				for (int i = 0; i < edgeLength; i++) {
					if (currentIndex + i >= pattern.length - 1) {
						matchFound = true;
						break;
					} else if (text[edge.textIndex + i] != pattern[currentIndex + i]) {
						return matches; // matches is empty at this point
					}
				}			
				
				//node = nextNode;

				if (matchFound) {
					if (node.hashMap.size() == 0) {
						matches.add(edge.textIndex - currentIndex);
					} else {
						
						findLeaves(matches, node, currentIndex + edgeLength);
					}
					
					return matches;
					
				} else {
					currentIndex += edgeLength;
				}
			}
			
		}
		return matches;
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

	public void showInWindow() {
		DefaultDirectedGraph<SuffixNode, SuffixEdge> g = new DefaultDirectedGraph<SuffixNode, SuffixEdge>(SuffixEdge.class);
		g.addVertex(root);
		addEdges(g, root);

		JGraphXAdapter<SuffixNode, SuffixEdge> jgxa = new JGraphXAdapter<SuffixNode, SuffixEdge>(g);

		mxIGraphLayout asd = new mxHierarchicalLayout(jgxa);
		//mxIGraphLayout asd = new mxCircleLayout(jgxa);

		asd.execute(jgxa.getDefaultParent());

		GraphWindow.show(jgxa);
	}
	
	public void saveAsSVG() {
		DefaultDirectedGraph<SuffixNode, SuffixEdge> g = new DefaultDirectedGraph<SuffixNode, SuffixEdge>(SuffixEdge.class);

		g.addVertex(root);
		addEdges(g, root);

		JGraphXAdapter<SuffixNode, SuffixEdge> jgxa = new JGraphXAdapter<SuffixNode, SuffixEdge>(g);

		mxIGraphLayout asd = new mxHierarchicalLayout(jgxa);

		asd.execute(jgxa.getDefaultParent());

		String filename = "graph.svg";
		mxSvgCanvas canvas = (mxSvgCanvas) mxCellRenderer.drawCells(
			jgxa, null, 1, null, new CanvasFactory() {
			public mxICanvas createCanvas(int width, int height) {
				mxSvgCanvas canvas = new mxSvgCanvas(mxDomUtils
					.createSvgDocument(width, height));
					canvas.setEmbedded(true);
					return canvas;
				}
			});
		  try {
			mxUtils.writeFile(mxXmlUtils.getXml(canvas.getDocument()), filename);
		  } catch (IOException e) {
			e.printStackTrace();
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

	private class SuffixEdge implements Serializable {
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
				sb.append(text[i] == ' ' ? '_' : text[i]);

			sb.append("["+textIndex+","+length+"]");
			//return "SuffixEdge [endNode=" + endNode + ", textIndex=" + textIndex + ", length=" + length + ", text=" + sb.toString() + "]";
			return sb.toString();
		}
				
	}
	
	private class SuffixNode implements Serializable {
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

