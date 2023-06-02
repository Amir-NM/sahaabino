package ir.amir.ingestor;

import ir.amir.ingestor.config.FileIngestorConfig;
import ir.amir.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * this is the main class of the file ingestor component.
 * it instantiates the three services FileWatcherService, RecordExtractorService and KafkaProducerServices and run them concurrently.
 */
public class FileIngestorMain {
    private static final String ingestorConfPath = "file-ingestor.yaml";
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(FileIngestorMain.class);

        Yaml yaml = new Yaml(new Constructor(FileIngestorConfig.class));
        InputStream inputStream = FileIngestorMain.class.getClassLoader().getResourceAsStream(ingestorConfPath);
        FileIngestorConfig config = yaml.load(inputStream);

        BlockingQueue<String> shareFilesPath = new ArrayBlockingQueue<>(10_000);
        BlockingQueue<Log> shareLog = new ArrayBlockingQueue<>(10_000);

        FileWatcherService fileWatcherService;

        fileWatcherService = new FileWatcherService(config.getFileWatcherConfig(), shareFilesPath);
        RecordExtractorService recordExtractorService = new RecordExtractorService(config.getRecordExtractorConfig(), shareFilesPath, shareLog);
        KafkaProducerService kafkaProducerService;
        try {
            kafkaProducerService = new KafkaProducerService(config.getKafkaProducerConfig(), shareLog);
        } catch (IOException e) {
            logger.error("Could not instantiate KafkaProducerService.");
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            fileWatcherService.interrupt();
            recordExtractorService.interrupt();
            kafkaProducerService.interrupt();
        }));

        fileWatcherService.start();
        recordExtractorService.start();
        kafkaProducerService.start();
    }
}
