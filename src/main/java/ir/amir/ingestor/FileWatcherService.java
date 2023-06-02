package ir.amir.ingestor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

/**
 * this service watches the specified directory to send new files path in its queue.
 */
public class FileWatcherService extends Thread {
    private boolean shouldEnd;
    private final Path directory;
    private final BlockingQueue<String> shareFilePath;

    public FileWatcherService(String directory, BlockingQueue<String> shareFilePath) throws FileNotFoundException {
        this.shouldEnd = false;
        this.directory = Path.of(directory);
        this.shareFilePath = shareFilePath;
    }

    public void run() {
        for (String logFileDir : Objects.requireNonNull(this.directory.toFile().list())) {
            try {
                this.shareFilePath.put(logFileDir);
            } catch (InterruptedException e) {
                // todo: log
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }
        }

        WatchKey watchKey;
        WatchService watchService;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            watchKey = this.directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(!this.shouldEnd) {
            for(WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                Path fileName = pathEvent.context();
                try {
                    this.shareFilePath.put(this.directory + "/" + fileName);
                } catch (InterruptedException e) {
                    // todo: log
                    this.shouldEnd = true;
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}
