package ir.amir.rule;

import ir.amir.log.Log;
import ir.amir.rest.database.Alert;
import ir.amir.rest.database.AlertSaver;

import java.sql.SQLException;
import java.util.ArrayList;

public class ThirdRuleType extends DurationBasedRule {
    private final int logCreationRateLimit;

    public ThirdRuleType(String ruleName, String ruleDescription, double duration, int logCreationRateLimit, AlertSaver alertSaver) {
        super(ruleName, ruleDescription, alertSaver, duration);
        this.logCreationRateLimit = logCreationRateLimit;
    }

    @Override
    public void processLog(Log log) {
        this.addLogToComponentsRecentLogs(log);

        this.checkForAlert(log);
    }

    public void checkForAlert(Log log) {
        ArrayList<Log> logs = this.componentsRecentLogs.get(log.getComponentName());

        if(logs.size() / this.duration >= this.logCreationRateLimit) {
            Alert alert = new Alert(addTotalCreatedAlertsCount(), this.ruleName, log.getComponentName(),
                    this.ruleDescription + " | current log creation rate: " + logs.size() / logs.get(0).getTimeDifference(log) +
                            " | last log message: " + log.getMsg());
            try {
                this.alertSaver.saveAlert(alert);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
