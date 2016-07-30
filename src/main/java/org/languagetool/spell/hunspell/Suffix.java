package org.languagetool.spell.hunspell;

import java.util.regex.Pattern;

class Suffix extends AffixCommand {

  Suffix(char flag, String cutOff, String append, Pattern regex) {
    super(flag, cutOff, append, regex);
  }

  Suffix(char flag, String cutOff, String append, String regex) {
    super(flag, cutOff, append, regex);
  }

  @Override
  public String toString() {
    return "SFX " + String.valueOf(flag) + " " + (cutOff.isEmpty() ? "0" : cutOff) + " " + append + " " + regex;
  }
  
}
