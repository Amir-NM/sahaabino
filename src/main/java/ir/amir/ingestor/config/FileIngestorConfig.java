package ir.amir.ingestor.config;

import ir.amir.ingestor.FileWatcherService;

public class FileIngestorConfig {
    private FileWatcherConfig fileWatcherConfig;
    private RecordExtractorConfig recordExtractorConfig;
    private KafkaProducerConfig kafkaProducerConfig;
}
