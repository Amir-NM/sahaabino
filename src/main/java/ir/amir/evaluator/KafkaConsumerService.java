package ir.amir.evaluator;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ir.amir.ingestor.config.KafkaConfig;
import ir.amir.kafka.KafkaConfigLoader;
import ir.amir.kafka.LocalDateTimeTypeAdapter;
import ir.amir.log.Log;
import org.apache.kafka.clients.consumer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

public class KafkaConsumerService extends Thread {
    private final Logger logger;
    private boolean shouldEnd;
    private final Consumer<Integer, String> consumer;
    private final String topic;
    private final Gson gson;
    private final BlockingQueue<Log> shareLog;

    public KafkaConsumerService(KafkaConfig config, BlockingQueue<Log> shareLog) {
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.shareLog = shareLog;
        Properties props;
        try {
            props = KafkaConfigLoader.getInstance().loadProps(config.getKafkaConfPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-java-getting-started");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.consumer = new KafkaConsumer<>(props);
        this.topic = config.getKafkaTopic();
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter("yyyy-MM-dd HH:mm:ss")).create();
    }

    public void run() {

        this.consumer.subscribe(Collections.singletonList(this.topic));
        while (!this.shouldEnd) {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(100));
            try {
                for (ConsumerRecord<Integer, String> record : records) {
                    logger.info("Consumed log: " + record.value());
                    Log log = this.gson.fromJson(record.value(), Log.class);
                    this.shareLog.put(log);
                }
            } catch (InterruptedException e) {
                this.logger.warn("Thread is interrupted.", e);
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }
        }
    }
}
