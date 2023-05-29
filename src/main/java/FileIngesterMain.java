import com.google.gson.Gson;
import kafkaHandler.KafkaLogProducer;
import logFilesHandler.DirectoryMonitorer;
import org.apache.kafka.common.protocol.types.Field;

import java.io.IOException;

public class FileIngesterMain {
    private static final String resourcesDir = "src/main/resources/";
    private static final String kafkaConfName = "kafka.conf";
    private static final String ingesterConfName = "file-ingester.conf";
    private static final String logsDir = "logs-dir";
    private static final String topic = "logs";
    public static void main(String[] args) {
        try {
            KafkaLogProducer kafkaLogProducer = new KafkaLogProducer(resourcesDir + kafkaConfName, topic);
            DirectoryMonitorer monitorer = new DirectoryMonitorer(resourcesDir + ingesterConfName, resourcesDir + logsDir, kafkaLogProducer);
            monitorer.monitor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
