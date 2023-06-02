package ir.amir.ingestor;

import ir.amir.ingestor.config.KafkaConfig;
import ir.amir.kafka.KafkaLogProducer;
import ir.amir.log.Log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * this service receives records from queue and produces them to kafka topic.
 */
public class KafkaProducerService extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private boolean shouldEnd;
    private final BlockingQueue<Log> shareLog;
    private final KafkaLogProducer kafkaLogProducer;

    public KafkaProducerService(KafkaConfig config, BlockingQueue<Log> shareLog) throws IOException {
        this.shareLog = shareLog;
        try {
            this.kafkaLogProducer = new KafkaLogProducer(config.getKafkaConfPath(), config.getKafkaTopic());
        } catch (IOException e) {
            logger.error("Could not create instantiate KafkaLogProducer.");
            throw e;
        }
        this.shouldEnd = false;
    }

    public void run() {
        logger.info("KafkaProducerService running...");
        while (!this.shouldEnd || !this.shareLog.isEmpty()) {
            Log log = null;
            try {
                log = this.shareLog.take();
            } catch (InterruptedException e) {
                logger.warn("Thread is interrupted.");
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }
            this.kafkaLogProducer.produce(log);
        }
    }
}
