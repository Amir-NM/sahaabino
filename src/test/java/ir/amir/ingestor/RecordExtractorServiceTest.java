package ir.amir.ingestor;


import ir.amir.ingestor.config.FileIngestorConfig;
import ir.amir.log.Log;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RecordExtractorServiceTest {
    private static final String ingestorConfPath = "file-ingestor.yaml";

    @Test
    public void extractLogsTest() throws FileNotFoundException, InterruptedException {
        Yaml yaml = new Yaml(new Constructor(FileIngestorConfig.class));
        InputStream inputStream = FileIngestorMain.class.getClassLoader().getResourceAsStream(ingestorConfPath);
        FileIngestorConfig config = yaml.load(inputStream);

        BlockingQueue<String> shareFilesPath = new ArrayBlockingQueue<>(10_000);
        BlockingQueue<Log> shareLog = new ArrayBlockingQueue<>(10_000);

        RecordExtractorService recordExtractorService = new RecordExtractorService(config.getRecordExtractorConfig(), shareFilesPath, shareLog);

        File logFile = new File("src/test/resources/test-log-dir/x-2023_9_5-12:35:18.log");
        Scanner sc = new Scanner(logFile);

        recordExtractorService.extractLogs(sc, logFile);

        Log log = shareLog.take();

        Log expected = new Log("x", LocalDateTime.parse("2023-10-10 18:20:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "ERROR", "massage 1");

        Assertions.assertEquals(expected.toString(), log.toString());
        Assertions.assertEquals(0, log.getTimeDifference(expected));
    }
}
