package io.makepad.hicx;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class FileProcessor {

  private final List<IProcessor> processors;
  private final File file;

  FileProcessor(File file) {
    this.file = file;
    // The list should be immutable as we do not want that the processors changes once its initialised
    this.processors = List.of(
        new MostUsedWordProcessor(),
        new NumberOfDotsProcessor(),
        new NumberOfWordsProcessor()
    );
  }

  void process() throws IOException {
    FileReader reader =new FileReader(file);   //Creation of File Reader object
    BufferedReader bufferedReader =new BufferedReader(reader);  //Creation of BufferedReader object
    // Initialize c with 0 to remove the warning
    int c = 0;

    // Read by character
    do {
      c = bufferedReader.read();
      // Display integer to char
      char character = (char) c;
      /*
       * For the given use case as we have only 3 processors, parallelizing the process slows down
       * the execution as the thread pool creation and thread pool shutdown takes also some time.
       * Also, ParallelStream uses an UnManagedThreadPool which makes its performance unpredictable
       */
      this.processors.forEach(processor -> {
        processor.process(character);
      });

    } while(c != -1);
  }


  /**
   * Print the processing result of each processor to stdout
   */
  void printStats() {
    System.out.printf("Results of %s\n%s\n",file.getName(),
    this.processors.stream().map(IProcessor::toString).collect(Collectors.joining("\n")));
  }

  /**
   * Returns the map of result for each processor. This method used principally for unit testing
   * @return A HashMap with each processor and its result
   */
  Map<String, Object> result() {
    Map<String, Object> resultMap = new HashMap<>();
    this.processors.forEach(processor -> {
      resultMap.put(processor.getClass().getName(), processor.result());
    });
    return resultMap;
  }
}
