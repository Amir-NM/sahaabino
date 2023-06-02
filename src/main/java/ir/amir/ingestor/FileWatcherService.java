package ir.amir.ingestor;

import com.mysql.cj.exceptions.ClosedOnExpiredPasswordException;
import ir.amir.ingestor.config.FileWatcherConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;

/**
 * this service watches the specified directory to send new files path in its queue.
 */
public class FileWatcherService extends Thread {
    private final Logger logger;
    private boolean shouldEnd;
    private final Path directory;
    private final BlockingQueue<String> shareFilePath;

    public FileWatcherService(FileWatcherConfig config, BlockingQueue<String> shareFilePath) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.shouldEnd = false;
        this.directory = Path.of(config.getDirectoryPath());
        this.shareFilePath = shareFilePath;
    }

    public void run() {
        logger.info("FileWatcherService running...");
        for (String logFileDir : Objects.requireNonNull(this.directory.toFile().list())) {
            try {
                this.shareFilePath.put(logFileDir);
                logger.trace("File path sent: " + logFileDir);
            } catch (InterruptedException e) {
                logger.error("thread is interrupted.", e);
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
            logger.error("Could not set watch key for directory");
            throw new RuntimeException(e);
        }

        logger.info("Watching for new files...");

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
