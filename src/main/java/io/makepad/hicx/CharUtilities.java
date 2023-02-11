package io.makepad.hicx;

import java.util.List;
import java.util.stream.Stream;

interface CharUtilities {

  /**
   * Check if the given character is a word delimiter or not
   * @param c The character to verify
   * @return True if the given character is a word delimiter or not. A word delimiter is either a punctuation which ends a sentence, an end of line, a tabulation or a white space
   */
  static boolean isAWordDelimiter(char c) {
    return Stream.of('.', '?', '!', ';', ':', ' ', '"', '\'', '(', ')', '[', ']', '-', '/', '\t', '\n', '\r').anyMatch((ch) -> ch == c);
  }

  /**
   * Check if the given character is a white space or not
   * @param c The character to check
   * @return True if the given character is a white space character or not
   */
  static boolean isAWhiteSpace(char c) {
      return Stream.of('\t', '\n', '\r', ' ').anyMatch(ch -> ch == c);
  }
}
