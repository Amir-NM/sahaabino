package ir.amir.evaluator;

import ir.amir.rule.FirstRuleType;
import ir.amir.kafka.KafkaLogConsumer;
import ir.amir.rest.database.AlertSaver;
import ir.amir.rule.Rule;
import ir.amir.rule.SecondRuleType;
import ir.amir.rule.ThirdRuleType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RuleEvaluatorMain {
    private static final String ruleEvaluatorConfDir = "src/main/resources/rule-evaluator.conf";

    public static void main(String[] args) {
        AlertSaver alertSaver = new AlertSaver("jdbc:mysql://localhost:3306/alerts_db", "root", "123456789");
        try {
            Scanner sc = new Scanner(new File(ruleEvaluatorConfDir));
            String kafkaConfDir = sc.nextLine();
            String topic = sc.nextLine();
            ArrayList<Rule> rules = new ArrayList<>();
            while (sc.hasNext()){
                String[] ruleParams = sc.nextLine().split("-");
                switch (ruleParams[0]) {
                    case "1" -> rules.add(new FirstRuleType(ruleParams[1], ruleParams[2], ruleParams[3], alertSaver));
                    case "2" -> rules.add(new SecondRuleType(ruleParams[1], ruleParams[2], ruleParams[3],
                            Float.parseFloat(ruleParams[4]), Integer.parseInt(ruleParams[5]), alertSaver));
                    case "3" -> rules.add(new ThirdRuleType(ruleParams[1], ruleParams[2], Float.parseFloat(ruleParams[3]),
                            Integer.parseInt(ruleParams[4]), alertSaver));
                }
            }
            KafkaLogConsumer kafkaLogConsumer = new KafkaLogConsumer(kafkaConfDir, topic, rules.toArray(new Rule[0]));
            kafkaLogConsumer.consume();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
