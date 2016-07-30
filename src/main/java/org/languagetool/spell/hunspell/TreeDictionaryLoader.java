package org.languagetool.spell.hunspell;

import org.languagetool.spell.CompoundTree;
import org.languagetool.spell.Tree;

import java.io.IOException;

public class TreeDictionaryLoader {

  public CompoundTree load(String basePath) throws IOException {
    DictionaryLoader loader = new DictionaryLoader();
    Dictionary dict = loader.load(basePath);
    Tree tree = new Tree();
    for (String s : dict.getNonCompoundWords()) {
      //if (s.startsWith("Abend")) {
      //  System.out.println("1)" + s);
      //}
      tree.add(s);
    }
    // TODO: we need a third tree I guess? or the info must be stored in the tree
    for (String s : dict.getCompoundBeginWords()) {
      //if (s.startsWith("Abend")) {
      //  System.out.println("2)" + s);
      //}
      tree.add(s);
    }
    Tree suffixTree = new Tree();
    for (String s : dict.getCompoundEndWords()) {
      suffixTree.add(s);
    }
    return new CompoundTree(tree, suffixTree);
  }
}
