package ir.amir;

import ir.amir.rule.FirstRuleType;
import ir.amir.kafka.KafkaLogConsumer;
import ir.amir.rest.database.AlertSaver;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RuleEvaluator {
    private static final String ruleEvaluatorConfDir = "src/main/resources/rule-evaluator.conf";

    public static void main(String[] args) {
        AlertSaver alertSaver = new AlertSaver("jdbc:mysql://localhost:3306/alerts_db", "root", "123456789");
        try {
            Scanner sc = new Scanner(new File(ruleEvaluatorConfDir));
            String kafkaConfDir = sc.nextLine();
            String topic = sc.nextLine();
            ArrayList<FirstRuleType> rules = new ArrayList<>();
            while (sc.hasNext()){
                String[] ruleParams = sc.nextLine().split("-");
                if(ruleParams[0].equals("1")) {
                    rules.add(new FirstRuleType(ruleParams[1], ruleParams[2], ruleParams[3], alertSaver));
                }
            }
            KafkaLogConsumer kafkaLogConsumer = new KafkaLogConsumer(kafkaConfDir, topic, rules.toArray(new FirstRuleType[0]));
            kafkaLogConsumer.consume();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
