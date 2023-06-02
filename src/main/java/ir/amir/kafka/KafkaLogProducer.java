package ir.amir.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ir.amir.log.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.time.LocalDateTime;

public class KafkaLogProducer {
    private static final Logger logger = LoggerFactory.getLogger(KafkaLogProducer.class);
    private final Producer<Integer, String> producer;
    private final String topic;
    private int producedCount;
    private final Gson gson;

    public KafkaLogProducer(String propsDir, String topic) throws IOException {
        try {
            this.producer = new KafkaProducer<>(KafkaConfigLoader.getInstance().loadProps(propsDir));
        } catch (IOException e) {
            logger.error("Could not load properties file.");
            throw e;
        }
        this.topic = topic;
        this.producedCount = 0;
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter("yyyy-MM-dd HH:mm:ss")).create();

    }

    public void produce(Log log) {
        String logString = this.gson.toJson(log);
        this.producer.send(
                new ProducerRecord<>(this.topic, this.producedCount, logString),
                (event, ex) -> {
                    if (ex != null)
                        ex.printStackTrace();
                    else
                        logger.info("Produced log: " + logString);
                });
        this.producedCount++;
        logger.trace("log produced to kafka: " + log);
    }
}
