package io.makepad.hicx;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

class FileSystemMonitor {
  private static final String[] SUPPORTED_FILE_TYPES = new String[]{"text/plain"};

  private final Path sourcePath;
  private final Path destinationPath;
  private final ThreadPoolExecutor threadPoolExecutor;



  /**
   * Creates a new instance of FileSystemMonitor with one or two arguments
   * @param arguments An array of arguments used to create FileSystemMonitor instance
   * @throws IOException if an I/O error occurs or the temporary-file directory does not exist
   */
  FileSystemMonitor(String[] arguments) throws IOException {
    this(
        Path.of(arguments[0]),
        Path.of(arguments.length == 1 ? FileSystemMonitor.getTmpDirectory() : arguments[1]),
        (ThreadPoolExecutor)Executors.newCachedThreadPool());
  }

  /**
   * Creates a new instance of FileSystemMonitor with two parameters
   * @param sourcePath The path of source folder to monitor
   * @param destinationPath The path of the folder where all processed files will be moved
   * @param executor The thread pool executor
   */
  private FileSystemMonitor(Path sourcePath, Path destinationPath, ThreadPoolExecutor executor) {
    this.sourcePath = sourcePath;
    this.destinationPath = destinationPath;
    this.threadPoolExecutor = executor;
  }


  /**
   * Start the monitoring and processing of each new file
   * @throws IOException – If an I/O error occurs
   */
  void start() throws IOException {
    WatchKey key;
    try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
      this.sourcePath.register(watcher, ENTRY_CREATE, ENTRY_MODIFY);
      boolean valid = true;
      do {
        try {
          key = watcher.take();
        } catch(InterruptedException ignore) {
          // Once a SIGINT is received, stop for waiting new watch events
          break;
        }
        for (WatchEvent<?> event: key.pollEvents()) {
          WatchEvent.Kind<?> kind = event.kind();
          if (kind == OVERFLOW) {
            // The OVERFLOW events will we watched even though they are not registered
            continue;
          }

          @SuppressWarnings("unchecked")
          WatchEvent<Path> e = (WatchEvent<Path>)event;

          final Path fileName = e.context();
          // Get the filename and resolve it against to the source folder path
          final Path filePath = this.sourcePath.resolve(fileName);
          // Check if the affected file is one of the supported file types

          if (Arrays.stream(SUPPORTED_FILE_TYPES).noneMatch(Files.probeContentType(filePath)::equals)) {
            // If the affected file type is not supported, do nothing
            System.out.println("Not a supported file type");
            continue;
          }
          this.threadPoolExecutor.execute(() -> {
            File file = filePath.toFile();
            FileProcessor processor = new FileProcessor(file);
            try {
              processor.process();
            } catch (IOException ignore) {
              System.err.printf("Error while processing file %s\n", filePath);
              return;
            }
            processor.printStats();
            boolean isMoved = this.moveFolderToDestination(file, fileName);
            if (!isMoved) {
              System.err.printf("Can't move the file %s after processing\n", fileName);
            }
          });
        }
        valid = key.reset();
      } while(valid);
      // Wait for everything to end properly
      threadPoolExecutor.shutdown();
    }
  }


  /**
   * Move the given file to the destination path
   * @param file The file to move
   * @param fileName The name of the file to move
   * @return True if the file is moved, false if not
   */
  private boolean moveFolderToDestination(File file, Path fileName) {
    return file.renameTo((destinationPath.resolve(fileName).toFile()));
  }


  /**
   * Get the path of the temporary directory
   * @return The path of the temporary directory
   * @throws IOException if an I/O error occurs or the temporary-file directory does not exist
   */
  private static String getTmpDirectory() throws IOException{
    return Files.createTempDirectory(UUID.randomUUID().toString()).toFile().getAbsolutePath();
  }
}
