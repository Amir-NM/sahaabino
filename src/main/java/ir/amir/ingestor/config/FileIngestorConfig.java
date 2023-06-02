package ir.amir.ingestor.config;

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

    public KafkaConfig getKafkaProducerConfig() {
        return kafkaProducerConfig;
    }

    public void setKafkaProducerConfig(KafkaConfig kafkaProducerConfig) {
        this.kafkaProducerConfig = kafkaProducerConfig;
    }

    private KafkaConfig kafkaProducerConfig;
}
