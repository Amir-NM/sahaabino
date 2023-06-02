import kafka.KafkaLogProducer;
import log.DirectoryMonitorer;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class FileIngesterMain {
    private static final String ingesterConfDir = "src/main/resources/file-ingester.conf";
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(new File(ingesterConfDir));
            String logsDir = sc.nextLine();
            String kafkaConfDir = sc.nextLine();
            String topic = sc.nextLine();
            String separator = sc.nextLine();
            String datetimePattern = sc.nextLine();
            String logFormat = sc.nextLine();
            KafkaLogProducer kafkaLogProducer = new KafkaLogProducer(kafkaConfDir, topic);
            DirectoryMonitorer monitorer = new DirectoryMonitorer(separator, datetimePattern, logFormat, logsDir, kafkaLogProducer);
            monitorer.monitor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
