package ir.amir.ingestor.config;

public class FileIngestorConfig {
    private FileWatcherConfig fileWatcherConfig;
    private RecordExtractorConfig recordExtractorConfig;
    private KafkaConfig kafkaConfig;

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

    public KafkaConfig getKafkaConfig() {
        return kafkaConfig;
    }

    public void setKafkaConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }
}
