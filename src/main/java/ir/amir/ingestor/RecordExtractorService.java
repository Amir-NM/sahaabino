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
 * this service reads files path from queue and process files to extract records and send records to another queue.
 */
public class RecordExtractorService extends Thread {
    private Logger logger;
    private boolean shouldEnd;
    private final LogFormat logFormat;
    private final BlockingQueue<String> shareFilePath;
    private final BlockingQueue<Log> shareLog;

    public RecordExtractorService(RecordExtractorConfig config, BlockingQueue<String> shareFilePath, BlockingQueue<Log> shareLog) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.logFormat = new LogFormat(config.getSeparator(), config.getDateTimePattern(), config.getLogFormat());
        this.shareFilePath = shareFilePath;
        this.shareLog = shareLog;
        this.shouldEnd = false;
    }

    public void run() {
        logger.info("RecordExtractorService running...");
        while (!this.shouldEnd || !this.shareFilePath.isEmpty()) {
            String filePath = "";
            try {
                filePath = this.shareFilePath.take();
                logger.info("File received: " + filePath);
            } catch (InterruptedException e) {
                logger.warn("Thread is interrupted.");
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }
            File logFile = new File(filePath);
            Scanner sc;

            try {
                sc = new Scanner(logFile);
            } catch (FileNotFoundException e) {
                logger.error("Could not open file.");
                throw new RuntimeException(e);
            }

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
            logFile.delete();
        }
    }
}
