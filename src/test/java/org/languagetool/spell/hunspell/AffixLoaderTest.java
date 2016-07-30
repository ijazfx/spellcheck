package org.languagetool.spell.hunspell;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class AffixLoaderTest {

  @Test
  public void testLoadAffixFile() {
    AffixLoader loader = new AffixLoader();
    Map<Character, List<AffixCommand>> map = loader.load(AffixLoaderTest.class.getResourceAsStream("/affix1.aff"), "utf-8");
    assertThat(map.size(), is(2));
    assertThat(map.get('F').size(), is(4));
    assertTrue(map.get('F').toString().contains("SFX F 0 nen .*in"));
    assertTrue(map.get('F').toString().contains("SFX F e in .*e"));
    assertTrue(map.get('F').toString().contains("SFX F e innen .*e"));
    assertTrue(map.get('F').toString().contains("SFX F 0 in .*[^i]n"));

    assertThat(map.get('U').size(), is(1));
    assertTrue(map.get('U').toString().contains("PFX U 0 un ..*"));
  }

}