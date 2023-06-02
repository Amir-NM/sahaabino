package ir.amir.rule;

import ir.amir.evaluator.config.rules.FirstRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.database.Alert;

public class FirstTypeRule extends Rule {
    private final String logsType;

    public FirstTypeRule(FirstRuleTypeConfig config) {
        super(config.getName(), config.getDescription());
        this.logsType = config.getLogsType();
    }

    @Override
    public Alert processLog(Log log) {
        if(log.isOfType(this.logsType)) {
            return new Alert(addTotalCreatedAlertsCount(), this.ruleName, log.getComponentName(),
                    this.ruleDescription + " | log type: " + this.logsType + " | log message: " + log.getMsg());
        }
        return null;
    }
}
