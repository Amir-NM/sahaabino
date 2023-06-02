package log;

import kafka.KafkaLogProducer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

public class DirectoryMonitorer {
    private final LogFormat logFormat;
    private final Path directory;
    private final LogFileProcessor fileProcessor;
    public DirectoryMonitorer(String separator, String datetimePattern, String logFormat, String directory, KafkaLogProducer kafkaLogProducer) throws FileNotFoundException {
        this.logFormat = new LogFormat(separator, datetimePattern, logFormat);
        this.directory = Path.of(directory);
        this.fileProcessor = new LogFileProcessor(kafkaLogProducer);
    }

    public void monitor() throws IOException {
        this.fileProcessor.processExistingFiles(this.directory, this.logFormat);
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            WatchKey watchKey = this.directory.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            while(true) {
                for(WatchEvent<?> event : watchKey.pollEvents()) {
                    WatchEvent<Path> pathEvent = (WatchEvent<Path>) event;
                    Path fileName = pathEvent.context();
                    this.fileProcessor.processLogFile(this.directory + "/" + fileName, this.logFormat);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
