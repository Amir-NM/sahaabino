package ir.amir.evaluator;

import ir.amir.evaluator.config.RuleEvaluatorConfig;
import ir.amir.evaluator.rule.Rule;
import ir.amir.ingestor.FileIngestorMain;
import ir.amir.log.Log;
import ir.amir.rest.Alert;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * this is the main class of the file evaluator component.
 * it instantiates the three services KafkaConsumerService, AlertExtractorService and DatabaseSaverService and runs them concurrently.
 */
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
        DatabaseService databaseSaverService = new DatabaseService(config.getDatabaseSaverConfig(), shareAlert);

        Rule.setTotalCreatedAlertsCount(databaseSaverService.getAlertsCount());

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
