package kafkaHandler;

import logFilesHandler.Log;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Properties;

public class KafkaLogProducer {
    private Properties properties;
    private String topic;
    private int producedCount;
    public KafkaLogProducer(String propsDir, String topic) throws IOException {
        this.properties = KafkaConfigLoader.getInstance().loadProps(propsDir);
        this.topic = topic;
        this.producedCount = 0;
    }

    public void produce(Log log) throws IOException {
        try (final Producer<Integer, Log> producer = new KafkaProducer<>(this.properties)) {
            producer.send(
                    new ProducerRecord<>(this.topic, this.producedCount, log),
                    (event, ex) -> {
                        if (ex != null)
                            ex.printStackTrace();
                        else
                            System.out.println("Produced log to topic: log " + this.producedCount);
                    });
        }
    }
}
