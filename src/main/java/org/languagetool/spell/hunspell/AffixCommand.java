package org.languagetool.spell.hunspell;

import java.util.Objects;
import java.util.regex.Pattern;

// See https://sourceforge.net/projects/hunspell/files/Hunspell/Documentation/
class AffixCommand {

  // Example:
  //SFX F   0     nen        in

  protected final char flag;
  protected final String cutOff;
  protected final String append;
  protected final Pattern regex;
  
  AffixCommand(char flag, String cutOff, String append, Pattern regex) {
    this.flag = flag;
    this.cutOff = Objects.requireNonNull(cutOff);
    this.append = Objects.requireNonNull(append).equals("0") ? "" : append;
    this.regex = Objects.requireNonNull(regex);
  }

  AffixCommand(char flag, String cutOff, String append, String regex) {
    this(flag, cutOff, append, Pattern.compile(".*" + Objects.requireNonNull(regex)));
  }

  char getFlag() {
    return flag;
  }

  String getCutOff() {
    return cutOff;
  }

  // append or prepend
  String getAppend() {
    return append;
  }

  Pattern getRegex() {
    return regex;
  }

}
