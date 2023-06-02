package ir.amir.ingestor;

import ir.amir.ingestor.config.RecordExtractorConfig;
import ir.amir.log.LogFormat;
import ir.amir.log.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;

/**
 * this service receives files path from queue and processes files to extract records and sends records to another queue.
 */
public class RecordExtractorService extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(RecordExtractorService.class);
    private boolean shouldEnd;
    private final LogFormat logFormat;
    private final BlockingQueue<String> shareFilePath;
    private final BlockingQueue<Log> shareLog;

    public RecordExtractorService(RecordExtractorConfig config, BlockingQueue<String> shareFilePath, BlockingQueue<Log> shareLog) {
        this.logFormat = new LogFormat(config.getSeparator(), config.getDateTimePattern(), config.getLogFormat());
        this.shareFilePath = shareFilePath;
        this.shareLog = shareLog;
        this.shouldEnd = false;
    }

    public void run() {
        logger.info("RecordExtractorService running...");
        while (!this.shouldEnd || !this.shareFilePath.isEmpty()) {
            File logFile = this.getLogFile();

            Scanner sc = this.getScanner(logFile);

            this.extractLogs(sc, logFile);

            if (!logFile.delete()) {
                logger.warn("Could not delete file: " + logFile.getName());
            }
        }
    }

    public File getLogFile() {
        String filePath = "";
        try {
            filePath = this.shareFilePath.take();
            logger.info("File received: " + filePath);
        } catch (InterruptedException e) {
            logger.warn("Thread is interrupted.");
            this.shouldEnd = true;
            Thread.currentThread().interrupt();
        }
        return new File(filePath);
    }

    public Scanner getScanner(File logFile) {
        Scanner sc;
        try {
            sc = new Scanner(logFile);
        } catch (FileNotFoundException e) {
            logger.error("Could not open file.");
            throw new RuntimeException(e);
        }
        return sc;
    }

    public void extractLogs(Scanner sc, File logFile) {
        while (sc.hasNext()) {
            Log log = this.logFormat.formLog(logFile.getName().split("-")[0], sc.nextLine());
            logger.trace("Log created: " + log);
            try {
                this.shareLog.put(log);
            } catch (InterruptedException e) {
                logger.warn("Thread is interrupted.");
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }
        }
    }
}
