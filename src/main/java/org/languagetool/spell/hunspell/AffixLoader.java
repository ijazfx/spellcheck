package org.languagetool.spell.hunspell;

import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

class AffixLoader {

  Map<Character, List<AffixCommand>> load(InputStream is, String encoding) {
    Map<Character, List<AffixCommand>> map = new HashMap<>();
    try (Scanner s = new Scanner(is, encoding)) {
      while (s.hasNextLine()) {
        String line = s.nextLine().trim();
        if (line.isEmpty() || line.startsWith("#")) {
          continue;
        }
        String[] parts = line.split("\\s+");
        if (line.startsWith("PFX")) {
          handlePrefix(map, line, parts);
        } else if (line.startsWith("SFX")){
          handleSuffix(map, line, parts);
        } else if (line.startsWith("...")){
          // TODO
        } else {
          System.out.println("Line not supported: " + line);
        }
      }
    }
    return map;
  }

  // TODO: interpret Y/N: Cross product (permission to combine prefixes and suffixes). Possible values: Y (yes) or N (no)
  // TODO: condition is optional
  
  private void handlePrefix(Map<Character, List<AffixCommand>> map, String line, String[] parts) {
    if (parts.length == 4) {
      // ..
    } else if (parts.length == 5) {
      //Example: SFX F   0     nen        in
      try {
        char ch = parts[1].charAt(0);
        String cutOff = parts[2].equals("0") ? "" : parts[2];
        String prepend = parts[3];
        Pattern regex = Pattern.compile(parts[4] + ".*");
        AffixCommand affixCommand = new Prefix(ch, cutOff, prepend, regex);
        map.putIfAbsent(ch, new ArrayList<>());
        map.get(ch).add(affixCommand);
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new RuntimeException("Could not load line: '" + line + "'", e);
      }
    } else {
      throw new RuntimeException("Could not load line: '" + line + "'");
    }
  }

  // TODO: avoid code duplication
  private void handleSuffix(Map<Character, List<AffixCommand>> map, String line, String[] parts) {
    if (parts.length == 4) {
      // ..
    } else if (parts.length == 5) {
      //Example: SFX F   0     nen        in
      try {
        char ch = parts[1].charAt(0);
        String cutOff = parts[2].equals("0") ? "" : parts[2];
        String append = parts[3];
        Pattern regex = Pattern.compile(".*" + parts[4]);
        AffixCommand affixCommand = new Suffix(ch, cutOff, append, regex);
        map.putIfAbsent(ch, new ArrayList<>());
        map.get(ch).add(affixCommand);
      } catch (ArrayIndexOutOfBoundsException e) {
        throw new RuntimeException("Could not load line: '" + line + "'", e);
      }
    } else {
      throw new RuntimeException("Could not load line: '" + line + "'");
    }
  }

}
