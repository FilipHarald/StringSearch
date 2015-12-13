package search.datastructures;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

/*
import com.mxgraph.layout.hierarchical.*;
import com.mxgraph.canvas.mxICanvas;
import com.mxgraph.canvas.mxSvgCanvas;
import com.mxgraph.layout.*;
import com.mxgraph.util.mxCellRenderer;
import com.mxgraph.util.mxCellRenderer.CanvasFactory;
import com.mxgraph.util.mxDomUtils;
import com.mxgraph.util.mxUtils;
import com.mxgraph.util.mxXmlUtils;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;
*/

import search.entities.Counter;

/*
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;

import javax.imageio.ImageIO;
*/

/**
 * Implementation of a Suffix Tree using Ukkonen's algorithm.
 *
 * _Heavily_ inspired by http://pastie.org/5925812#47,70,72,81,84-85,87,92,95,102-103
 *
 * Helpful resources
 * http://felix-halim.net/pg/suffix-tree/
 * http://hwv.dk/st/
 *
 * @author Albert Kaaman
 */
public class SuffixTree implements Serializable {
	private static int nodeId = 2;
	private final char[] text;
	private final SuffixNode root;
	private int length;

	private SuffixNode needSuffixLink = null;
	
	public SuffixTree(char[] text) {
		this.text = text;
		root = new SuffixNode(text.length);

		build();
	}

	/**
	 * Adds a suffix link from previous node in need of one {@code needSuffixLink}, to the supplied {@code node}. Sets the supplied node
	 * as the next node in need of a link.
	 * @param node Node to link to
	 */
	private void addLink(SuffixNode node) {
		if (needSuffixLink != null)
			needSuffixLink.suffixLink = node;
		needSuffixLink = node;
	}

	/**
	 * Builds the suffix tree
	 */
	private void build() {
		SuffixNode activeNode = root;
		char activeEdgeFirstChar = text[0];
		int activeLength = 0;

		int currentSuffixStart = 0;
		int remainingSuffixes = 0;
		int counter = 0;

		length = text.length;

		// While we still haven't reached the end of the string
		while (currentSuffixStart < text.length) {
			remainingSuffixes++;
			needSuffixLink = null;

			// While there are remaining suffixes to insert
			while (remainingSuffixes >= 1) {
				SuffixEdge foundEdge = activeNode.getEdge(activeEdgeFirstChar);

				if (foundEdge == null) {
					// No outgoing edge from active node starting with activeEdge char, create new one
					SuffixNode newNode = new SuffixNode();
					SuffixEdge newEdge = new SuffixEdge(newNode, currentSuffixStart, -1);

					// Add edge to activenode
					activeNode.addEdge(activeEdgeFirstChar, newEdge);

					addLink(activeNode);

				} else {
					if (foundEdge.length > 0 && activeLength >= foundEdge.length) {
						// We've travelled as far along an edge as we can, we need to switch active node
						// and then start looking from that node.
						activeLength -= foundEdge.length;
						currentSuffixStart += foundEdge.length;
						activeNode = foundEdge.endNode;
						activeEdgeFirstChar = text[currentSuffixStart];
						//remainingSuffixes--;
						continue;

					} else if (text[currentSuffixStart + activeLength] == text[foundEdge.textIndex + activeLength]) {
						// We're still travelling along active edge so simply increase the activeLength
						activeLength++;
						//remainingSuffixes++;

						addLink(activeNode);

						break;
					} else {
						// Current suffix differs from active edge, we need to split edge, insert new node.

						SuffixNode insertedNode = new SuffixNode();
						SuffixNode leafNode = new SuffixNode();
						SuffixNode oldNode = foundEdge.endNode;
						int newLength = oldNode.hashMap.size() > 0 ? Math.abs(foundEdge.length - activeLength) : -1;

						foundEdge.endNode = insertedNode;
						foundEdge.length = activeLength;

						SuffixEdge remainingEdge = new SuffixEdge(oldNode, foundEdge.textIndex + foundEdge.length, newLength);
						SuffixEdge leafEdge = new SuffixEdge(leafNode, currentSuffixStart + activeLength, -1);

						insertedNode.addEdge(text[foundEdge.textIndex + foundEdge.length], remainingEdge);
						insertedNode.addEdge(text[currentSuffixStart + activeLength], leafEdge);

						addLink(insertedNode);

					}
				}

				remainingSuffixes--;

				//this.saveAsImage(counter++);

				if (activeNode.equals(root)) {
					// If we're at the root, then we can move up currentSuffixStart to insert the next suffix
					if (activeLength > 0)
						activeLength--;
					currentSuffixStart++;

					// have to check currentSuffix here or suffer arrayindexoutofbounds. better solution please?!
					if (currentSuffixStart >= text.length)
						break;

					activeEdgeFirstChar = text[currentSuffixStart];
				} else {
					// Otherwise we need to follow the active node's suffix link
					activeNode = activeNode.suffixLink != null ? activeNode.suffixLink : root;
				}

			}
		}
	}

	/**
	 * Recursively finds all leaf nodes of {@code node}, adding the starting index of the substring leading
	 * to the node to {@code matches}.
	 *
	 * @param matches List of matches
	 * @param node Node to follow
	 * @param currentLength Current length of all substrings down to the followed node
	 */
	private void findLeaves(List<Integer> matches, SuffixNode node, int currentLength) {
		if (node.hashMap.size() > 0) {
			for (Map.Entry<Character, SuffixEdge> entry : node.hashMap.entrySet()) {
				if (entry.getValue().endNode.hashMap.size() <= 0) {
					matches.add(entry.getValue().textIndex - currentLength);
				} else {
					findLeaves(matches, entry.getValue().endNode, currentLength + entry.getValue().length);
				}
			}
		} 
	}

	/**
	 * Searches for {@code pattern] in the Suffix Tree.
	 * @param pattern Pattern to search for
	 * @return A list of matches
	 */
	public List<Integer> find(String pattern) {
		return find(pattern.toCharArray());
	}

	/**
	 * Searches for {@code pattern} in the Suffix Tree
	 * @param pattern Pattern to search for, as a char array
	 * @return A list of matches
	 */
	public List<Integer> find(char[] pattern) {
		return find(pattern, new Counter());
	}

	/**
	 * Searches for {@code pattern} in the Suffix Tree. Counts the number of operations and saves the value to {@code operations}
	 * @param pattern Pattern to search for, as a char array
	 * @param operations Counter to save number of operations
	 * @return A list of matches
	 */
	public List<Integer> find(char[] pattern, Counter operations) {
		List<Integer> matches = new LinkedList<>();
		
		SuffixNode node = root;
		int currentIndex = 0;
		boolean matchFound = false;
		
		while (node != null) {
			SuffixEdge edge = node.hashMap.get(pattern[currentIndex]);
			node = null;
			if (edge != null) {
				node = edge.endNode;
				int edgeLength = edge.length < 0 ? text.length - edge.textIndex : edge.length;
				for (int i = 0; i < edgeLength; i++) {
					operations.increment();

					if (currentIndex + i >= pattern.length - 1) {
						// If we've travelled along the current edge equal to or greater
						// than the pattern's length, then we have a match
						matchFound = true;
						break;
					} else if (text[edge.textIndex + i] != pattern[currentIndex + i]) {
						// If the current character on the edge does not equal the pattern's character, we've fallen off
						// and return an empty list.
						return matches;
					}
				}			

				if (matchFound) {
					if (node.hashMap.size() == 0) {
						// If the matched node has no children, then the only match is the edge leading to it
						matches.add(edge.textIndex - currentIndex);
					} else {
						// Otherwise we need to find all leaves under it. The edges leading to the leaves are all matches.
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

	/*
	private void addEdges(DefaultDirectedGraph<SuffixNode, SuffixEdge> g, SuffixNode n) {
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
	*/

	/**
	 * Internal class representing an edge in the SuffixTree
	 */
	private class SuffixEdge implements Serializable {
		private SuffixNode endNode;
		private int textIndex;
		private int length;

		public SuffixEdge(SuffixNode endNode, int index, int length) {
			this.endNode = endNode;
			this.textIndex = index;
			this.length = length;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();

			/*
			int end = length < 0 ? SuffixTree.this.length : textIndex + length;
			for (int i = textIndex; i < end ; i++)
				sb.append(text[i] == ' ' ? '_' : text[i]);
			*/
			sb.append(text[textIndex]+"["+textIndex+","+length+"]");
			//return "SuffixEdge [endNode=" + endNode + ", textIndex=" + textIndex + ", length=" + length + ", text=" + sb.toString() + "]";
			return sb.toString();
		}
				
	}

	/**
	 * Internal class representing a node in the SuffixTree. Each node holds a record of the outgoing
	 * edges from that node, in a hash map.
	 */
	private class SuffixNode implements Serializable {
		private int id;
		private SuffixNode suffixLink = null;
		private final Map<Character, SuffixEdge> hashMap;

		public SuffixNode() {
			this(10);
		}

		public SuffixNode(int initialSize) {
			this.id = SuffixTree.nodeId++;
			hashMap = new HashMap<>(initialSize);
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

	/*
	public class GraphWindow extends JFrame {

		public GraphWindow(mxGraph graph)
		{
			super("Hello, World!");

			Object parent = graph.getDefaultParent();

			mxGraphComponent graphComponent = new mxGraphComponent(graph);

			getContentPane().add(graphComponent);
		}

		public void show(mxGraph g)
		{
			GraphWindow frame = new GraphWindow(g);
			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.setSize(400, 320);
			frame.setVisible(true);
		}
	}*/

}

