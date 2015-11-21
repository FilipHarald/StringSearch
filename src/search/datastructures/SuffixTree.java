package search.datastructures;

import java.util.HashMap;
import java.util.Map;

public class SuffixTree {
	
	private SuffixNode root;
	
	private Map<SuffixKey, SuffixNode> hash;
	private ActivePoint activePoint;
	private int remainder;
	private int length;
	
	public SuffixTree(char[] t) {
		hash = new HashMap<>(t.length);
		
		for (int i = 0; i < t.length; i++) {
			this.append(t[i]);
		}
	}
	
	private void append(char c) {
		
	}
	
	private class SuffixEdge {
		private int index;
		private int length;
	}
	
	private class SuffixNode {
		private int id;
	}
	
	private class ActivePoint {
		private SuffixNode node = null;
		private SuffixEdge edge = null;
		private int length = 0;
		
		public ActivePoint(SuffixNode node, SuffixEdge edge, int length) {
			super();
			this.node = node;
			this.edge = edge;
			this.length = length;
		}

		public SuffixNode getNode() {
			return node;
		}

		public void setNode(SuffixNode node) {
			this.node = node;
		}

		public SuffixEdge getEdge() {
			return edge;
		}

		public void setEdge(SuffixEdge edge) {
			this.edge = edge;
		}

		public int getLength() {
			return length;
		}

		public void setLength(int length) {
			this.length = length;
		}
	}
	
	private class SuffixKey {
		private SuffixNode node;
		private SuffixEdge edge;

		public SuffixKey(SuffixNode node, SuffixEdge edge) {
			this.node = node;
			this.edge = edge;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((edge == null) ? 0 : edge.hashCode());
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

			if (edge == null) {
				if (other.edge != null)
					return false;
			} else if (!edge.equals(other.edge))
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

