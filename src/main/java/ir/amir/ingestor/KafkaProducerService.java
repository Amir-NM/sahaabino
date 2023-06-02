package ir.amir.ingestor;

import ir.amir.kafka.KafkaLogProducer;
import ir.amir.log.Log;

import java.util.concurrent.BlockingQueue;

/**
 * this service reads records from queue and produces them to kafka topic.
 */
public class KafkaProducerService extends Thread {
    private boolean shouldEnd;
    private final BlockingQueue<Log> shareLog;
    private final KafkaLogProducer kafkaLogProducer;

    public KafkaProducerService(BlockingQueue<Log> shareLog, KafkaLogProducer kafkaLogProducer) {
        this.shareLog = shareLog;
        this.kafkaLogProducer = kafkaLogProducer;
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
