package ir.amir.evaluator;

import ir.amir.evaluator.config.AlertExtractorConfig;
import ir.amir.evaluator.config.rules.FirstRuleTypeConfig;
import ir.amir.evaluator.config.rules.SecondRuleTypeConfig;
import ir.amir.evaluator.config.rules.ThirdRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.database.Alert;
import ir.amir.rule.FirstTypeRule;
import ir.amir.rule.Rule;
import ir.amir.rule.SecondTypeRule;
import ir.amir.rule.ThirdTypeRule;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AlertExtractorService extends Thread {
    private boolean shouldEnd;
    private List<Rule> rules;
    private BlockingQueue<Log> shareLog;
    private BlockingQueue<Alert> shareAlert;

    public AlertExtractorService(AlertExtractorConfig config, BlockingQueue<Log> shareLog, BlockingQueue<Alert> shareAlert) {
        this.shareLog = shareLog;
        this.shareAlert = shareAlert;
        for(FirstRuleTypeConfig ruleConfig : config.getFirstRuleTypeConfigs()) {
            rules.add(new FirstTypeRule(ruleConfig));
        }
        for(SecondRuleTypeConfig ruleConfig : config.getSecondRuleTypeConfigs()) {
            rules.add(new SecondTypeRule(ruleConfig));
        }
        for(ThirdRuleTypeConfig ruleConfig : config.getThirdRuleTypeConfigs()) {
            rules.add(new ThirdTypeRule(ruleConfig));
        }
        this.shouldEnd = false;
    }

    public void run() {
        while (!this.shouldEnd || !this.shareLog.isEmpty()) {
            Log log = null;
            try {
                log = this.shareLog.take();
            } catch (InterruptedException e) {
                // todo: log
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }

            for (Rule rule : this.rules) {
                Alert alert = rule.processLog(log);

                if (alert != null) {
                    try {
                        shareAlert.put(alert);
                    } catch (InterruptedException e) {
                        // todo: log
                        this.shouldEnd = true;
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
