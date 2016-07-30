package org.languagetool.spell.hunspell;

import org.junit.Ignore;
import org.junit.Test;
import org.languagetool.spell.CompoundTree;
import org.languagetool.spell.Tree;

import java.io.IOException;

import static org.junit.Assert.*;

public class TreeDictionaryLoaderTest {
  
  @Test
  @Ignore("needs local file")
  public void testLoad() throws IOException {
    TreeDictionaryLoader loader = new TreeDictionaryLoader();
    
    CompoundTree tree = loader.load("/lt/de_DE");
    System.out.println(tree.getTree().containsWord("Abend"));
    System.out.println(tree.getSuffixTree().containsWord("test"));

    /*
    Tree tmpTree = new Tree();
    //TODO: order of insertion plays a role (alph. seems okay?)
    tmpTree.add("Abends");
    tmpTree.add("Abend");
    Tree suffixTree = new Tree();
    suffixTree.add("test");
    CompoundTree tree = new CompoundTree(tmpTree, suffixTree);
    */
    
    //assertTrue(tree.containsWord("Abende"));
    assertTrue(tree.containsWord("Abendtest"));
    //assertTrue(tree.containsWord("Abendkühlschrank"));
    //assertTrue(tree.containsWord("Abend"));
  }

}