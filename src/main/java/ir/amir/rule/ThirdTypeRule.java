package ir.amir.rule;

import ir.amir.evaluator.config.rules.ThirdRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.database.Alert;
import ir.amir.rest.database.AlertSaver;

import java.sql.SQLException;
import java.util.ArrayList;

public class ThirdTypeRule extends DurationBasedRule {
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
            return new Alert(addTotalCreatedAlertsCount(), this.ruleName, log.getComponentName(),
                    this.ruleDescription + " | current log creation rate: " + logs.size() / logs.get(0).getTimeDifference(log) +
                            " | last log message: " + log.getMsg());
        }
        return null;
    }
}
