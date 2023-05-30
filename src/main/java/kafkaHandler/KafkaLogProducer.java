package kafkaHandler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import logFilesHandler.Log;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Properties;

public class KafkaLogProducer {
    private final Producer<Integer, String> producer;
    private final String topic;
    private int producedCount;
    private Gson gson;

    public KafkaLogProducer(String propsDir, String topic) throws IOException {
        this.producer = new KafkaProducer<>(KafkaConfigLoader.getInstance().loadProps(propsDir));
        this.topic = topic;
        this.producedCount = 0;
    }

    public void createGson(String pattern) {
        this.gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter(pattern)).create();
    }

    public void produce(Log log) {
        this.producer.send(
                new ProducerRecord<>(this.topic, this.producedCount, this.gson.toJson(log)),
                (event, ex) -> {
                    if (ex != null)
                        ex.printStackTrace();
                    else
                        System.out.println("Produced log to topic: log " + this.producedCount);
                });
            this.producedCount++;
    }
}
