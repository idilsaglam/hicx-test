package io.makepad.hicx;

class NumberOfWordsProcessor implements IProcessor<Long>{

  private boolean hasWord = false;
  private long counter = 0L;

  @Override
  public synchronized void process(char c) {
    if (CharUtilities.isAWhiteSpace(c)) {
      // If the current character is a word delimiter (ie. either a punctuation or a space) check if the current word is blank or empty
      if (hasWord) {
        // If there was already a beginning of the word ie, if there's already characters other than delimiter are read increment the counter and reset the word
        counter++;
        hasWord=false;
      }
      return;
    }
    // If the current character is not a delimiter: set the has word to true
    hasWord=true;
  }

  @Override
  public Long result() {
    if (hasWord) {
      return counter++;
    }
    return counter;
  }

  public String toString() {
    return "Number of words: %d".formatted(this.result());
  }


}
