package io.makepad.hicx;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileProcessorTest {

  private static final String NUMBER_OF_DOTS_PROCESSOR_KEY = NumberOfDotsProcessor.class.getName();
  private static final String MOST_USED_WORD_PROCESSOR_KEY = MostUsedWordProcessor.class.getName();
  private static final String NUMBER_OF_WORDS_PROCESSOR_KEY = NumberOfWordsProcessor.class.getName();


  @Test
  @DisplayName("Testing the result of each processor")
 void testProcessingResult() throws IOException {
    URL url = this.getClass().getResource("/alice_in_wonderland.txt");
    File file = new File(url.getFile());
    FileProcessor processor = new FileProcessor(file);
    processor.process();

    Map<String, Object> result = processor.result();
    assertEquals(3, result.size());
    assertEquals(977L, result.get(NUMBER_OF_DOTS_PROCESSOR_KEY));
    assertEquals(26457L, result.get(NUMBER_OF_WORDS_PROCESSOR_KEY));
    assertEquals(new SimpleImmutableEntry<String, Long>("the", 1620L), result.get(MOST_USED_WORD_PROCESSOR_KEY));
  }
}
