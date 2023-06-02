package ir.amir.evaluator;

import ir.amir.evaluator.config.RuleEvaluatorConfig;
import ir.amir.ingestor.FileIngestorMain;
import ir.amir.log.Log;
import ir.amir.rest.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RuleEvaluatorMain {
    private static final String ruleEvaluatorConfDir = "rule-evaluator.yaml";

    public static void main(String[] args) {
        Yaml yaml = new Yaml(new Constructor(RuleEvaluatorConfig.class));
        InputStream inputStream = FileIngestorMain.class.getClassLoader().getResourceAsStream(ruleEvaluatorConfDir);
        RuleEvaluatorConfig config = yaml.load(inputStream);

        BlockingQueue<Log> shareLog = new ArrayBlockingQueue<>(10_000);
        BlockingQueue<Alert> shareAlert = new ArrayBlockingQueue<>(10_000);

        KafkaConsumerService kafkaConsumerService = new KafkaConsumerService(config.getKafkaConfig(), shareLog);
        AlertExtractorService alertExtractorService = new AlertExtractorService(config.getAlertExtractorConfig(), shareLog, shareAlert);
        DatabaseSaverService databaseSaverService = new DatabaseSaverService(config.getDatabaseSaverConfig(), shareAlert);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            kafkaConsumerService.interrupt();
            alertExtractorService.interrupt();
            databaseSaverService.interrupt();
        }));

        kafkaConsumerService.start();
        alertExtractorService.start();
        databaseSaverService.start();
    }
}
