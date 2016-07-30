package org.languagetool.spell.hunspell;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Dictionary {

  private final List<String> nonCompoundWords;
  private final List<String> compoundBeginWords;  // at begin or middle of compound
  private final List<String> compoundEndWords;

  Dictionary(List<String> nonCompoundWords, List<String> compoundBeginWords, List<String> compoundEndWords) {
    this.nonCompoundWords = Objects.requireNonNull(nonCompoundWords);
    this.compoundBeginWords = Objects.requireNonNull(compoundBeginWords);
    this.compoundEndWords = Objects.requireNonNull(compoundEndWords);
  }

  public List<String> getNonCompoundWords() {
    return Collections.unmodifiableList(nonCompoundWords);
  }

  public List<String> getCompoundBeginWords() {
    return Collections.unmodifiableList(compoundBeginWords);
  }

  public List<String> getCompoundEndWords() {
    return Collections.unmodifiableList(compoundEndWords);
  }

  @Override
  public String toString() {
    return nonCompoundWords +
           " begin=" + compoundBeginWords +
           " end=" + compoundEndWords;
  }
}
