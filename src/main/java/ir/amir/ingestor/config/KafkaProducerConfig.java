package ir.amir.ingestor.config;

public class KafkaProducerConfig {
    private String kafkaConfPath;
    private String kafkaTopic;

    public String getKafkaConfPath() {
        return kafkaConfPath;
    }

    public void setKafkaConfPath(String kafkaConfPath) {
        this.kafkaConfPath = kafkaConfPath;
    }

    public String getKafkaTopic() {
        return kafkaTopic;
    }

    public void setKafkaTopic(String kafkaTopic) {
        this.kafkaTopic = kafkaTopic;
    }
}
