package io.makepad.hicx;

interface IProcessor<R> {

  /**
   * Process a single character
   * @param c the character to process
   */
  void process(char c);

  /**
   * Get the result of the processing
   * @return The result of the processing
   */
  R result();

  /**
   * Returns the string representation of the processing result
   * @return The string representation of the processing results
   */
  String toString();
}
