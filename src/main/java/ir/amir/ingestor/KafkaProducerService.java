package ir.amir.ingestor;

import ir.amir.ingestor.config.KafkaProducerConfig;
import ir.amir.kafka.KafkaLogProducer;
import ir.amir.log.Log;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * this service reads records from queue and produces them to kafka topic.
 */
public class KafkaProducerService extends Thread {
    private boolean shouldEnd;
    private final BlockingQueue<Log> shareLog;
    private final KafkaLogProducer kafkaLogProducer;

    public KafkaProducerService(KafkaProducerConfig config, BlockingQueue<Log> shareLog) throws IOException {
        this.shareLog = shareLog;
        try {
            this.kafkaLogProducer = new KafkaLogProducer(config.getKafkaConfPath(), config.getKafkaTopic());
        } catch (IOException e) {
            // todo: log
            throw e;
        }
        this.shouldEnd = false;
    }

    public void run() {
        while (!this.shouldEnd || !this.shareLog.isEmpty()) {
            Log log = null;
            try {
                log = this.shareLog.take();
            } catch (InterruptedException e) {
                // todo: log
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }
            this.kafkaLogProducer.produce(log);
        }
    }
}
