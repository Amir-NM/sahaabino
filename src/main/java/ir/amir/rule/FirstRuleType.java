package ir.amir.rule;

import ir.amir.rest.database.Alert;
import ir.amir.rest.database.AlertSaver;
import ir.amir.log.Log;

import java.sql.SQLException;

public class FirstRuleType extends Rule {
    private final String logsType;

    public FirstRuleType(String ruleName, String ruleDescription, String logsType, AlertSaver alertSaver) {
        super(ruleName, ruleDescription, alertSaver);
        this.logsType = logsType;
    }

    @Override
    public void processLog(Log log) {
        if(log.isOfType(this.logsType)) {
            Alert alert = new Alert(addTotalCreatedAlertsCount(), this.ruleName, log.getComponentName(), this.ruleDescription + " | log message: " + log.getMsg());
            try {
                this.alertSaver.saveAlert(alert);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
