package org.languagetool.spell;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Tree {
  
  enum EndBehavior {
    CanEnd, CannotEnd, MustEnd
  }

  private static final boolean DEBUG = false;
  private static final char NULL_CHAR = '\u0000';

  private final List<Tree> leaves = new LinkedList<>();
  private final Tree parent;
  private final char data;
  private final EndBehavior endBehavior;
  
  private int depth;

  public Tree(char data, Tree parent, EndBehavior endBehavior) {
    this.data = data;
    this.parent = parent;
    this.endBehavior = endBehavior;
  }
  
  public Tree() {
    this(NULL_CHAR, null, null);
  }
  
  public void add(String s) {
    add(s, this, EndBehavior.MustEnd);
  }
  
  public void add(String s, EndBehavior endBehavior) {
    add(s, this, endBehavior);
  }
  
  public List<Tree> getLeaves() {
    return leaves;
  }
  
  public EndBehavior getEndNode() {
    return endBehavior;
  }
  
  @SuppressWarnings("AssignmentToMethodParameter")
  public void add(String s, Tree node, EndBehavior endBehavior) {
    int i = 0;
    int n = s.length();
    while (i < n) {
      Tree child = node.child(s.charAt(i));
      if (child != null) {
        node = child;
        i++;
      } else {
        break;
      }
    }
    // append new nodes, if necessary
    while (i < n) {
      char ch = s.charAt(i);
      Tree tree = i == n-1 ? new Tree(ch, node, endBehavior) : new Tree(ch, node, EndBehavior.CannotEnd);
      node.leaves.add(tree);
      node = node.child(ch);
      i++;
    }    
  }

  protected Tree child(char c) {
    for (Tree leaf : leaves) {
      if (leaf.data == c) {
        return leaf;
      }
    }
    return null;
  }

  public boolean containsWord(String word) {
    Tree nodeOrNull = getNodeOrNull(word, this);
    if (nodeOrNull == null) {
      return false;
    }
    return nodeOrNull.leaves.size() == 0 || nodeOrNull.endBehavior == EndBehavior.CanEnd || nodeOrNull.endBehavior == EndBehavior.MustEnd;
  }

  public List<String> getSimilarWords(String word, int maxDist) {
    print(word + " =======================================");
    if (maxDist < 0) {
      throw new IllegalArgumentException("maxDist must be >= 0: " + maxDist);
    }
    List<String> result = new ArrayList<>();
    getSimilarWords(this, word, 0, maxDist, result);
    return result;
  }
  
  private void getSimilarWords(Tree node, String word, int dist, int maxDist, List<String> result) {
    depth++;
    if (dist > maxDist) {
      return;
    }
    print("*** " + word + " node " + node + ", dist: " + dist);
    for (int i = 0; i < word.length(); i++) {
      char ch = word.charAt(i);
      Tree child = node.child(ch);
      print(ch + " child: " + child);
      if (child != null) {
        node = child;
      } else {
        // Replacement: skip current char:
        for (Tree leaf : node.leaves) {
          print("leaf: " + leaf + " -- " + word.substring(i+1));
          getSimilarWords(leaf, word.substring(i+1), dist + 1, maxDist, result);
        }
        // Insertion - skip char in input:
        getSimilarWords(node, word.substring(i+1), dist + 1, maxDist, result);
        // Deletion:
        for (Tree leaf : node.leaves) {
          print("leaf: " + leaf + " -- " + word.substring(i+1) + " i=" + i);
          getSimilarWords(leaf, word.substring(i), dist + 1, maxDist, result);
        }
        // Transposition:
        // TODO?
        depth--;
        return;
      }
    }
    boolean b = (node.leaves.size() == 0 || node.endBehavior == EndBehavior.CanEnd || node.endBehavior == EndBehavior.MustEnd) && dist <= maxDist;
    print("b = " + b  + " dist: " + dist + ", node.leaves.size(): " + node.leaves);
    if (!b) {
      // special case for deletion of last character:
      for (Tree leaf : node.leaves) {
        getSimilarWords(leaf, word.substring(word.length()), dist + 1, maxDist, result);
      }
    } else {
      result.add(getPathToRoot(node));
    }
    depth--;
  }

  String getPathToRoot(Tree node) {
    Tree currentNode = node;
    StringBuilder sb = new StringBuilder();
    while (currentNode != null) {
      if (currentNode.data != NULL_CHAR) {
        sb.append(currentNode.data);
      }
      currentNode = currentNode.parent;
    }
    return sb.reverse().toString();
  }

  protected Tree getNodeOrNull(String word, Tree node) {
    for (int i = 0; i < word.length(); i++) {
      char ch = word.charAt(i);
      Tree child = node.child(ch);
      if (child != null) {
        node = child;
      } else {
        return null;
      }
    }
    return node;
  }

  @Override
  public String toString() {
    return toString(0);
  }
  
  public String toString(int level) {
    StringBuilder sb = new StringBuilder();
    sb.append(data);
    if (endBehavior == EndBehavior.CanEnd) {
      sb.append(".");
    } else if (endBehavior == EndBehavior.CannotEnd) {
      sb.append("+");
    } else if (endBehavior == EndBehavior.MustEnd) {
      sb.append("#");
    }
    for (Tree leaf : leaves) {
      sb.append("{");
      sb.append(leaf.toString(level+1));
      sb.append("}");
    }
    return sb.toString();
  }

  private void print(String s) {
    if (DEBUG) {
      for (int i = 0; i < depth; i++) {
        System.out.print("    ");
      }
      System.out.println(s);
    }
  }
  
}
