package ir.amir.evaluator.rule;

import ir.amir.evaluator.config.rules.FirstRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.Alert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FirstTypeRule extends Rule {
    private static final Logger logger = LoggerFactory.getLogger(FirstTypeRule.class);
    private final String logsType;

    public FirstTypeRule(FirstRuleTypeConfig config) {
        super(config.getName(), config.getDescription());
        this.logsType = config.getLogsType();
    }

    @Override
    public Alert processLog(Log log) {
        if(log.isOfType(this.logsType)) {
            logger.info("First type alert created.");
            return new Alert(addTotalCreatedAlertsCount(), this.ruleName, log.getComponentName(),
                    this.ruleDescription + " | log type: " + this.logsType + " | log message: " + log.getMsg());
        }
        return null;
    }
}
