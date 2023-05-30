package kafkaHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.kafka.clients.consumer.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Properties;

public class KafkaLogConsumer {
    private final Consumer<Integer, String> consumer;
    private final String topic;
    private Gson gson;

    public KafkaLogConsumer(String propsDir, String topic) throws IOException {
        Properties props = KafkaConfigLoader.getInstance().loadProps(propsDir);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "kafka-java-getting-started");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        this.consumer = new KafkaConsumer<>(props);
        this.topic = topic;
    }

    public void createGson(String pattern) {
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(pattern)).create();
    }

    public void consume() {
        this.consumer.subscribe(Collections.singletonList(this.topic));
        while (true) {
            ConsumerRecords<Integer, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<Integer, String> record : records) {
                //TODO
            }
        }
    }
}
