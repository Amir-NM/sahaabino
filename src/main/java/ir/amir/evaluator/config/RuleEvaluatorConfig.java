package ir.amir.evaluator.config;

import ir.amir.ingestor.config.KafkaConfig;

public class RuleEvaluatorConfig {
    private KafkaConfig kafkaConfig;
    private AlertExtractorConfig alertExtractorConfig;
    private DatabaseSaverConfig databaseSaverConfig;

    public DatabaseSaverConfig getDatabaseSaverConfig() {
        return databaseSaverConfig;
    }

    public void setDatabaseSaverConfig(DatabaseSaverConfig databaseSaverConfig) {
        this.databaseSaverConfig = databaseSaverConfig;
    }

    public KafkaConfig getKafkaConfig() {
        return kafkaConfig;
    }

    public void setKafkaConfig(KafkaConfig kafkaConfig) {
        this.kafkaConfig = kafkaConfig;
    }

    public AlertExtractorConfig getAlertExtractorConfig() {
        return alertExtractorConfig;
    }

    public void setAlertExtractorConfig(AlertExtractorConfig alertExtractorConfig) {
        this.alertExtractorConfig = alertExtractorConfig;
    }
}
