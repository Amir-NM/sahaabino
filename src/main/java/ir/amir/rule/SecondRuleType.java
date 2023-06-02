package ir.amir.rule;

import ir.amir.log.Log;
import ir.amir.rest.database.Alert;
import ir.amir.rest.database.AlertSaver;

import java.sql.SQLException;
import java.util.ArrayList;

public class SecondRuleType extends DurationBasedRule {
    private final String logsType;
    private final int logCountLimit;

    public SecondRuleType(String ruleName, String ruleDescription, String logsType, float duration, int logCountLimit, AlertSaver alertSaver) {
        super(ruleName, ruleDescription, alertSaver, duration);
        this.logsType = logsType;
        this.logCountLimit = logCountLimit;
    }

    @Override
    public void processLog(Log log) {
        if(log.isOfType(this.logsType)) {

            this.addLogToComponentsRecentLogs(log);

            this.checkForAlert(log);
        }
    }

    public void checkForAlert(Log log) {
        ArrayList<Log> logs = this.componentsRecentLogs.get(log.getComponentName());

        if(logs.size() >= this.logCountLimit) {
            Alert alert = new Alert(addTotalCreatedAlertsCount(), this.ruleName, log.getComponentName(),
                    this.ruleDescription + " | log type: " + this.logsType + " | last log message: " + log.getMsg() +
                            " | second last log message: " + logs.get(logs.size() - 2).getMsg());
            try {
                this.alertSaver.saveAlert(alert);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}