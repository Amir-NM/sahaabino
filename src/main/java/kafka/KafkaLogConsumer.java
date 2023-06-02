package kafka;

import rule.FirstRuleType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import log.Log;
import org.apache.kafka.clients.consumer.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;

public class KafkaLogConsumer {
    private final Consumer<Integer, String> consumer;
    private final String topic;
    private final Gson gson;

    private final FirstRuleType[] alertRules;

    public KafkaLogConsumer(String propsDir, String topic, FirstRuleType[] alertRules) throws IOException {
        Properties props = KafkaConfigLoader.getInstance().loadProps(propsDir);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-java-getting-started");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.consumer = new KafkaConsumer<>(props);
        this.topic = topic;
        this.alertRules = alertRules;
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter("yyyy-MM-dd HH:mm:ss")).create();
    }

    public void consume() {
        this.consumer.subscribe(Collections.singletonList(this.topic));
        while (true) {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<Integer, String> record : records) {
                System.out.println("Consumed log: " + record.value());
                Log log = this.gson.fromJson(record.value(), Log.class);
                for(FirstRuleType alertRule : this.alertRules) {
                    alertRule.processLog(log);
                }
            }
        }
    }
}