package org.languagetool.spell;

import java.util.List;
import java.util.Random;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

final class TestTools {

  private TestTools() {
  }

  static Tree makeTree(List<String> lines, int limit) {
    Tree root = new Tree();
    long start = System.currentTimeMillis();
    int i = 0;
    int count = 0;
    Random rnd = new Random(1234);
    for (String line : lines) {
      if (rnd.nextFloat() < 0.95) {
        // skip some entries so similarity search below has something to do:
        root.add(line.trim());
        count++;
      }
      //System.out.println("Adding '" + line.trim() + "'");
      if (++i >= limit) {
        break;
      }
    }
    long end = System.currentTimeMillis();
    System.out.println("Inserting " + count + " elements into tree took " + (end-start) + "ms");
    return root;
  }

  static void assertSim1(String s, String expected, Tree root) {
    String sims = root.getSimilarWords(s, 1).toString();
    assertThat(sims, is("[" + expected + "]"));
  }

  static void assertSim1(String s, String expected, CompoundTree root) {
    String sims = root.getSimilarWords(s, 1).toString();
    assertThat(sims, is("[" + expected + "]"));
  }

}
