package ir.amir.evaluator;

import ir.amir.evaluator.config.AlertExtractorConfig;
import ir.amir.evaluator.config.rules.FirstRuleTypeConfig;
import ir.amir.evaluator.config.rules.SecondRuleTypeConfig;
import ir.amir.evaluator.config.rules.ThirdRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.Alert;
import ir.amir.evaluator.rule.FirstTypeRule;
import ir.amir.evaluator.rule.Rule;
import ir.amir.evaluator.rule.SecondTypeRule;
import ir.amir.evaluator.rule.ThirdTypeRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 * this service receives logs from queue and extract alerts from them. then sends alerts to another queue.
 */
public class AlertExtractorService extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(AlertExtractorService.class);
    private boolean shouldEnd;
    private final List<Rule> rules;
    private final BlockingQueue<Log> shareLog;
    private final BlockingQueue<Alert> shareAlert;

    public AlertExtractorService(AlertExtractorConfig config, BlockingQueue<Log> shareLog, BlockingQueue<Alert> shareAlert) {
        this.shareLog = shareLog;
        this.shareAlert = shareAlert;
        this.rules = new ArrayList<>();
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
        logger.info("AlertExtractorService running...");
        while (!this.shouldEnd || !this.shareLog.isEmpty()) {
            Log log = null;
            try {
                log = this.shareLog.take();
            } catch (InterruptedException e) {
                logger.warn("Thread is interrupted.", e);
                this.shouldEnd = true;
                Thread.currentThread().interrupt();
            }

            for (Rule rule : this.rules) {
                Alert alert = rule.processLog(log);

                if (alert != null) {
                    try {
                        this.shareAlert.put(alert);
                    } catch (InterruptedException e) {
                        logger.warn("Thread is interrupted.", e);
                        this.shouldEnd = true;
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
    }
}
