package io.makepad.hicx;

import java.io.IOException;

public class Main {

  private static final String PATH_TO_FOLDER_TO_MONITOR_INDICATOR = "<path_to_folder_to_monitor>";
  private static final String[][] HELP_PAGE_OPTIONS = new String[][]{
      new String[]{PATH_TO_FOLDER_TO_MONITOR_INDICATOR,"Monitor changes on the given folder. Process changed files and put each processed file into a temporary directory."},
      new String[]{"%s <path_to_copy_processed_files>".formatted(PATH_TO_FOLDER_TO_MONITOR_INDICATOR) ,"Monitor changes on the given folder. Process changed files and put each processed file into the second directory."}
  };

  /**
   * Show the help page on the stdout
   */
  private static void helpPage() {
    for (String[] option: HELP_PAGE_OPTIONS) {
      System.out.format("%s%150s%n", option);
    }
  }

  public static void main(String[] args) {
    if (args.length < 1 || args.length > 2) {
      System.err.println("Invalid number of arguments");
      Main.helpPage();
      // If there's an error end the execution with exit code 1 to show the error
      System.exit(1);
    }
     try {
       // We only need a single instance of file monitor as we'll only monitor a single folder
       final FileSystemMonitor monitor = new FileSystemMonitor(args);
       monitor.start();
    } catch(IOException e) {
       System.err.println(e);
      // Exit with the error code (anything different than 0 will work)
      System.exit(1);
    }
  }
}
