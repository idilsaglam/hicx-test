package io.makepad.hicx;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

class MostUsedWordProcessor implements IProcessor<SimpleImmutableEntry<String, Long>>{

  private final Map<String, Long> countingMap = new HashMap<>();
  private String currentWord = "";

  @Override
  public synchronized void process(char c) {
    if (CharUtilities.isAWordDelimiter(c)) {
      this.addCurrentWordToTheMapIfNecessary();
      return;
    }
    // If the current word is not empty, concatenate the character to the end of the word
    currentWord += c;
  }

  @Override
  public SimpleImmutableEntry<String, Long> result() {
    this.addCurrentWordToTheMapIfNecessary();
    // Compare each word by their number of occurrence and get the maximum
    final Optional<Entry<String, Long>> maxOccurredWord = this.countingMap.entrySet().parallelStream().max(Entry.comparingByValue());
    return maxOccurredWord
        .map(maxOccuredWordEntry -> new SimpleImmutableEntry<String, Long>(maxOccuredWordEntry.getKey(), maxOccuredWordEntry.getValue()))
        .orElse(new SimpleImmutableEntry<>("",0L));
  }


  public String toString() {
    SimpleImmutableEntry<String, Long> result = this.result();
    // If the optional is empty this means that the map was empty otherwise it contains the most used word with the number of occurrence
    if (result.getValue() == 0) {
      return "No word read";
    }
    return "Most used word is the word \"%s\". It used %d times.".formatted(result.getKey(), result.getValue());
  }


  /**
   * Check if the current word should be added to the counting map. If so add it to the counting map and reset it
   */
  private void addCurrentWordToTheMapIfNecessary() {
    if (currentWord.isBlank() || currentWord.isEmpty()) {
      // If the current word is empty or blank, do nothing
      return;
    }
    // if the current word is not empty or blank, add the current word to the countingMap and reset the current word
    final String currentWordInLowerCase = currentWord.trim().toLowerCase();
    final Long numberOfOccurrences = countingMap.get(currentWordInLowerCase);
    countingMap.put(currentWordInLowerCase,numberOfOccurrences == null ? 0L : numberOfOccurrences+1);
    currentWord = "";
  }
}
