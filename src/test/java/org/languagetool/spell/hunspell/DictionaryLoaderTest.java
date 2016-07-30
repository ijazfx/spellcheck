package org.languagetool.spell.hunspell;

import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DictionaryLoaderTest {

  @Test
  public void testLoadAffixFile() {
    DictionaryLoader loader = new DictionaryLoader();
    Map<Character, List<AffixCommand>> affixMap = new HashMap<>();
    affixMap.put('F', Arrays.asList(
            new Suffix('F', "", "new", "ied"),
            new Suffix('F', "x", "appended", "[^z]x"))
    );
    Dictionary words = loader.loadDict(DictionaryLoaderTest.class.getResourceAsStream("/dict1.dic"), affixMap, "utf-8");
    List<String> w = words.getNonCompoundWords();
    assertThat(w.size(), is(3));
    assertThat(w.get(0), is("Foo"));
    assertThat(w.get(1), is("Fooappended"));
    assertThat(w.get(2), is("Fooiednew"));
    // TODO: words.getSuffixWords()
  }
  
  @Test
  public void testApplyAffixCommand() {
    DictionaryLoader loader = new DictionaryLoader();
    Map<Character, List<AffixCommand>> affixMap = new HashMap<>();
    affixMap.put('y', Collections.singletonList(new Suffix('y', "", "-yy", ".")));
    assertThat(loader.applyAffixCommand("foo", new Suffix('x', "", "-xx", "."), affixMap).toString(), is("[foo-xx]"));
    assertThat(loader.applyAffixCommand("foo", new Suffix('x', "", "-xx/y", "."), affixMap).toString(), is("[foo-xx-yy]"));  // recursion
    //System.out.println(strings);
  }
 
  @Ignore("needs local file")
  @Test
  public void testLoadHunspellFile() throws IOException {
    DictionaryLoader loader = new DictionaryLoader();
    //String basePath = "/lt/git/languagetool/languagetool-language-modules/de/src/main/resources/org/languagetool/resource/de/hunspell/de_DE";
    String basePath = "/lt/de_DE";
    Dictionary dict = loader.load(basePath);
    printStats(dict);
  }
 
  @Ignore("needs local file")
  @Test
  public void testLoadHunspellAffFile() throws IOException {
    DictionaryLoader loader = new DictionaryLoader();
    String affPath = "/lt/de_DE.aff";
    // COMPOUNDBEGIN: Ähren/hij
    // COMPOUNDEND: äcker/Nozm
    String dic = "1\n" +
                 //"äcker/Nozm";
                 "Abend/j";
                 //"äcker/m";
                 //"Äbte/m";   //ok
    Dictionary dict = loader.load(new FileInputStream(affPath), new ByteArrayInputStream(dic.getBytes("utf-8")), "utf-8");
    printStats(dict);
  }

  private void printStats(Dictionary words) {
    System.out.println(words.getNonCompoundWords().size() + " non-compound words loaded");
    System.out.println(words.getCompoundBeginWords().size() + " compound begin words loaded");
    System.out.println(words.getCompoundEndWords().size() + " compound end words loaded");
  }

}
