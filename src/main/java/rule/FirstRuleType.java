package rule;

import api.database.Alert;
import api.database.AlertSaver;
import log.Log;

import java.sql.SQLException;

public class FirstRuleType {
    private final String ruleName;
    private final String ruleDescription;
    private final String logsType;
    private final AlertSaver alertSaver;
    private static int totalCreatedAlertsCount = 0;

    public FirstRuleType(String ruleName, String ruleDescription, String logsType, AlertSaver alertSaver) {
        this.ruleName = ruleName;
        this.ruleDescription = ruleDescription;
        this.logsType = logsType;
        this.alertSaver = alertSaver;
    }

    public void processLog(Log log) {
        if(log.isOfType(this.logsType)) {
            totalCreatedAlertsCount += 1;
            Alert alert = new Alert(totalCreatedAlertsCount, this.ruleName, log.getComponentName(), ruleDescription + " | log message: " + log.getMsg());
            try {
                alertSaver.saveAlert(alert);
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
