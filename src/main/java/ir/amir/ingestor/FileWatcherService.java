package ir.amir.ingestor;

import ir.amir.ingestor.config.FileWatcherConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

/**
 * this service watches the specified directory to sends new files path in its queue.
 */
public class FileWatcherService extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(FileWatcherService.class);
    private boolean shouldEnd;
    private final Path directory;
    private final BlockingQueue<String> shareFilePath;

    public FileWatcherService(FileWatcherConfig config, BlockingQueue<String> shareFilePath) {
        this.shouldEnd = false;
        this.directory = Path.of(config.getDirectoryPath());
        this.shareFilePath = shareFilePath;
    }

    public void run() {
        logger.info("FileWatcherService running...");
        this.sendExistingFiles();

        WatchKey watchKey = this.getWatchkey();

        logger.info("Watching for new files...");

        this.watchDirectory(watchKey);
    }

    private void sendExistingFiles() {
        try {
            for (String logFileDir : Objects.requireNonNull(this.directory.toFile().list())) {
                this.shareFilePath.put(logFileDir);
                logger.trace("File path sent: " + logFileDir);
            }
        } catch (InterruptedException e) {
            logger.error("thread is interrupted.", e);
            this.shouldEnd = true;
            Thread.currentThread().interrupt();
        }
    }

    private WatchKey getWatchkey() {
        WatchKey watchKey;
        WatchService watchService;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            watchKey = this.directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            logger.error("Could not set watch key for directory");
            throw new RuntimeException(e);
        }
        return watchKey;
    }

    private void watchDirectory(WatchKey watchKey) {
        while(!this.shouldEnd) {
            try {
                for(WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path fileName = pathEvent.context();
                    this.shareFilePath.put(this.directory + "/" + fileName);
                    logger.trace("File path sent: " + this.directory + "/" + fileName);
                }
            } catch (InterruptedException e) {
                logger.warn("Thread is interrupted.", e);
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }
        }
    }
}
