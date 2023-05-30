package logFilesHandler;

import kafkaHandler.KafkaLogProducer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

public class LogFileProcessor {

    private final KafkaLogProducer kafkaLogProducer;
    public LogFileProcessor(KafkaLogProducer kafkaLogProducer) {
        this.kafkaLogProducer = kafkaLogProducer;
    }

    public void processLogFile(String logFileDir, LogFormat logFormat) throws IOException {
        File logFile = new File(logFileDir);
        Scanner sc = new Scanner(logFile);
        while (sc.hasNext()) {
            Log log = logFormat.formLog(logFile.getName().split("-")[0], sc.nextLine());
            this.kafkaLogProducer.produce(log);
        }
        logFile.delete();
    }

    public void processExistingFiles(Path directory, LogFormat logFormat) throws IOException {
        for (String logFileDir : directory.toFile().list()) {
            this.processLogFile(directory + "/" + logFileDir, logFormat);
        }
    }
}
