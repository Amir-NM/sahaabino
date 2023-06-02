package ir.amir.ingestor;

import ir.amir.kafka.KafkaLogProducer;
import ir.amir.log.Log;
import ir.amir.log.LogFormat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * this is the main class of the file ingestor component.
 * it instantiates the three services FileWatcherService, RecordExtractorService and KafkaProducerServices and run them concurrently.
 */
public class FileIngestorMain {
    private static final String ingestorConfDir = "src/main/resources/file-ingester.conf";
    public static void main(String[] args) {
        BlockingQueue<String> shareFilesPath = new ArrayBlockingQueue<>(10_000);
        BlockingQueue<Log> shareLog = new ArrayBlockingQueue<>(10_000);

        Scanner sc;
        try {
            sc = new Scanner(new File(ingestorConfDir));
        } catch (FileNotFoundException e) {
            // todo: log
            throw new RuntimeException(e);
        }

        String logsDir = sc.nextLine();
        String kafkaConfDir = sc.nextLine();
        String topic = sc.nextLine();
        String separator = sc.nextLine();
        String datetimePattern = sc.nextLine();
        String logFormatString = sc.nextLine();

        LogFormat logFormat;
        try {
            logFormat = new LogFormat(separator, datetimePattern, logFormatString);
        } catch (FileNotFoundException e) {
            // todo: log
            throw new RuntimeException(e);
        }

        KafkaLogProducer kafkaLogProducer;
        try {
            kafkaLogProducer = new KafkaLogProducer(kafkaConfDir, topic);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }

        FileWatcherService fileWatcherService;
        try {
            fileWatcherService = new FileWatcherService(logsDir, shareFilesPath);
        } catch (FileNotFoundException e) {
            // todo: log
            throw new RuntimeException(e);
        }

        RecordExtractorService recordExtractorService = new RecordExtractorService(logFormat, shareFilesPath, shareLog);
        KafkaProducerService kafkaProducerService = new KafkaProducerService(shareLog, kafkaLogProducer);

        fileWatcherService.start();
        recordExtractorService.start();
        kafkaProducerService.start();
    }
}
