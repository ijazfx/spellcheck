package org.languagetool.spell.hunspell;

import java.util.regex.Pattern;

class Prefix extends AffixCommand {

  Prefix(char flag, String cutOff, String prepend, Pattern regex) {
    super(flag, cutOff, prepend, regex);
  }

  @Override
  public String toString() {
    return "PFX " + String.valueOf(flag) + " " + (cutOff.isEmpty() ? "0" : cutOff) + " " + append + " " + regex;
  }

}
