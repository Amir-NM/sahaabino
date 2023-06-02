package ir.amir.ingestor.config;

import ir.amir.ingestor.FileWatcherService;

public class FileIngestorConfig {
    private FileWatcherConfig fileWatcherConfig;
    private RecordExtractorConfig recordExtractorConfig;

    public FileWatcherConfig getFileWatcherConfig() {
        return fileWatcherConfig;
    }

    public void setFileWatcherConfig(FileWatcherConfig fileWatcherConfig) {
        this.fileWatcherConfig = fileWatcherConfig;
    }

    public RecordExtractorConfig getRecordExtractorConfig() {
        return recordExtractorConfig;
    }

    public void setRecordExtractorConfig(RecordExtractorConfig recordExtractorConfig) {
        this.recordExtractorConfig = recordExtractorConfig;
    }

    public KafkaProducerConfig getKafkaProducerConfig() {
        return kafkaProducerConfig;
    }

    public void setKafkaProducerConfig(KafkaProducerConfig kafkaProducerConfig) {
        this.kafkaProducerConfig = kafkaProducerConfig;
    }

    private KafkaProducerConfig kafkaProducerConfig;
}
