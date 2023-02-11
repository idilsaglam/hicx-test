package io.makepad.hicx;

class NumberOfDotsProcessor implements IProcessor<Long> {

  // the count of the dot characters
  private long counter = 0L;

  @Override
  public synchronized void process(char c) {
      if (c == '.') {
        counter++;
      }
  }

  @Override
  public Long result() {
    return counter;
  }

  public String toString() {
    return "Number of dots: %d".formatted(this.result());
  }
}
