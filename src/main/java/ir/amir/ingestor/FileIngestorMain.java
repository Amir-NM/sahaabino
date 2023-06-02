package ir.amir.ingestor;

import ir.amir.kafka.KafkaLogProducer;
import ir.amir.log.DirectoryMonitorer;
import ir.amir.log.LogFormat;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class FileIngestorMain {
    private static final String ingestorConfDir = "src/main/resources/file-ingester.conf";
    public static void main(String[] args) {
        BlockingQueue<String> shareFilesPath = new ArrayBlockingQueue<>(10_000);
        Scanner sc;
        try {
            sc = new Scanner(new File(ingestorConfDir));
            String logsDir = sc.nextLine();
            String kafkaConfDir = sc.nextLine();
            String topic = sc.nextLine();
            String separator = sc.nextLine();
            String datetimePattern = sc.nextLine();
            String logFormat = sc.nextLine();
            KafkaLogProducer kafkaLogProducer = new KafkaLogProducer(kafkaConfDir, topic);
            DirectoryMonitorer monitorer = new DirectoryMonitorer(separator, datetimePattern, logFormat, logsDir, kafkaLogProducer);
            monitorer.monitor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
