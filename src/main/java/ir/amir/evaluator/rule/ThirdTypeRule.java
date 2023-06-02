package ir.amir.evaluator.rule;

import ir.amir.evaluator.config.rules.ThirdRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class ThirdTypeRule extends DurationBasedRule {
    private static final Logger logger = LoggerFactory.getLogger(ThirdTypeRule.class);
    private final int logCreationRateLimit;

    public ThirdTypeRule(ThirdRuleTypeConfig config) {
        super(config.getName(), config.getDescription(), config.getDuration());
        this.logCreationRateLimit = config.getLogCreationRateLimit();
    }

    @Override
    public Alert processLog(Log log) {
        this.addLogToComponentsRecentLogs(log);

        return this.createAlert(log);
    }

    public Alert createAlert(Log log) {
        ArrayList<Log> logs = this.componentsRecentLogs.get(log.getComponentName());

        if(logs.size() / this.duration >= this.logCreationRateLimit) {
            logger.info("Third type log created.");
            return new Alert(addTotalCreatedAlertsCount(), this.ruleName, log.getComponentName(),
                    this.ruleDescription + " | current log creation rate: " + logs.size() / this.duration +
                            " | last log message: " + log.getMsg());
        }
        return null;
    }
}
