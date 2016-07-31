package org.languagetool.spell;

import java.util.ArrayList;
import java.util.List;

import org.languagetool.spell.Tree.EndBehavior;

public class CompoundTree {

	private final Tree tree;
	private final Tree suffixTree;

	public CompoundTree(Tree tree, Tree suffixTree) {
		this.tree = tree;
		this.suffixTree = suffixTree;
	}

	public boolean containsWord(String word) {
		Tree nodeOrNull = getNodeOrNull(word, tree);
		if (nodeOrNull == null) {
			return false;
		}
		//System.out.println(nodeOrNull + " -> leaves: " + nodeOrNull.getLeaves().size() + ", end node: " + nodeOrNull.getEndNode());
		return nodeOrNull.getLeaves().size() == 0 || nodeOrNull.getEndNode() == Tree.EndBehavior.CanEnd || nodeOrNull.getEndNode() == Tree.EndBehavior.MustEnd;
	}

	//	private Tree getNodeOrNull(String rest, Tree node) {
	//		//System.out.println("================ " + rest + " node: " + node + ", end: " + (node != null && node.getEndNode()));
	//		if (rest.length() == 0) {
	//			//System.out.println("-> node: " + node);
	//			return node;
	//		}
	//		if (node != null && (node.getEndNode() == Tree.EndBehavior.CanEnd || node.getEndNode() == Tree.EndBehavior.MustEnd)) {
	//			Tree nodeOrNull = getNodeOrNull(rest, suffixTree);
	//			if (nodeOrNull != null) {
	//				return nodeOrNull; // e.g. "Haus+tür"
	//			} else {
	//				return null; // e.g. "Haus+asdf"
	//			}
	//		}
	//		if (node != null) {
	//			char firstChar = rest.charAt(0);
	//			Tree subNode = node.child(firstChar);
	//			// keep eating the input while iterating the tree:
	//			return getNodeOrNull(rest.substring(1), subNode);
	//		} else {
	//			return null;
	//		}
	//	}

	private Tree getNodeOrNull(String rest, Tree node) {
		if (rest.length() == 0) {
			if (node != null && node.getEndNode() == EndBehavior.CannotEnd) {
				return null;
			}
			return node;
		}
		if (node != null) {
			char firstChar = rest.charAt(0);
			Tree subNode = node.child(firstChar);
			if (subNode == null) {
				if (node.getEndNode() == EndBehavior.CannotEnd || node.getEndNode() == EndBehavior.CanEnd) {
					return getNodeOrNull(rest, suffixTree);
				}
				return getNodeOrNull(rest, subNode);
			}
			return getNodeOrNull(rest.substring(1), subNode);
		}
		return null;
	}

	public List<String> getSimilarWords(String word, int maxDist) {
		List<String> result = new ArrayList<>();
		getSimilarWords(word, tree, 0, maxDist, result);
		return result;
	}

	private Tree getSimilarWords(String rest, Tree node, int dist, int maxDist, List<String> result) {
		//System.out.println("================ '" + rest + "' node: " + node + ", end: " + (node != null && node.getEndNode()));
		if (rest.length() == 0) {
			//System.out.println("-> node: " + node);
			if (node != null && dist <= maxDist) {
				String pathToRoot = node.getPathToRoot(node);
				//System.out.println("pathToRoot: " + pathToRoot);
				result.add(pathToRoot);
			}
			return node;
		}
		if (node != null && (node.getEndNode() == Tree.EndBehavior.CanEnd || node.getEndNode() == Tree.EndBehavior.MustEnd)) {
			Tree nodeOrNull = getSimilarWords(rest, suffixTree, dist, maxDist, result);
			if (nodeOrNull != null) {
				//result.add(node.getPathToRoot(node));
				//System.out.println("pathToRoot*: " + node.getPathToRoot(node));
				if (result.size() > 0) {
					String pathToRoot = node.getPathToRoot(node);
					//System.out.println("pathToRoot: " + pathToRoot);
					// attach to result found previously (deeper in the call stack):
					result.add(pathToRoot + result.get(result.size() - 1));
					result.remove(result.get(result.size() - 2));
				}
				return nodeOrNull; // e.g. "Haus+tür"
			} else {
				return null; // e.g. "Haus+asdf"
			}
		}
		if (node != null) {
			char firstChar = rest.charAt(0);
			Tree subNode = node.child(firstChar);
			if (subNode == null) {
				// Replacement:
				for (Tree leaf : node.getLeaves()) {
					Tree res = getSimilarWords(rest.substring(1), leaf, dist + 1, maxDist, result);
					if (res != null) {
						return res;
					}
				}
				// TODO: insert, delete
			} else {
				// keep eating the input while iterating the tree:
				return getSimilarWords(rest.substring(1), subNode, dist, maxDist, result);
			}
		} else {
			return null;
		}
		return null;
	}

	public Tree getTree() {
		return tree;
	}

	public Tree getSuffixTree() {
		return suffixTree;
	}

}
