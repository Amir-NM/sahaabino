package ir.amir.evaluator.rule;

import ir.amir.evaluator.config.rules.SecondRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

public class SecondTypeRule extends DurationBasedRule {
    private static final Logger logger = LoggerFactory.getLogger(SecondTypeRule.class);
    private final String logsType;
    private final int logCountLimit;

    public SecondTypeRule(SecondRuleTypeConfig config) {
        super(config.getName(), config.getDescription(), config.getDuration());
        this.logsType = config.getLogsType();
        this.logCountLimit = config.getLogCountLimit();
    }

    @Override
    public Alert processLog(Log log) {
        if(log.isOfType(this.logsType)) {

            this.addLogToComponentsRecentLogs(log);

            return this.createAlert(log);
        }
        return null;
    }

    public Alert createAlert(Log log) {
        ArrayList<Log> logs = this.componentsRecentLogs.get(log.getComponentName());

        if(logs.size() >= this.logCountLimit) {
            logger.info("Second type alert created.");
            return new Alert(addTotalCreatedAlertsCount(), this.ruleName, log.getComponentName(),
                    this.ruleDescription + " | log type: " + this.logsType + " | last log message: " + log.getMsg() +
                            " | second last log message: " + logs.get(logs.size() - 2).getMsg());
        }
        return null;
    }
}