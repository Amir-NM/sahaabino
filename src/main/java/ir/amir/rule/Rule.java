package ir.amir.rule;

import ir.amir.rest.database.AlertSaver;
import ir.amir.log.Log;

public abstract class Rule {
    protected final String ruleName;
    protected final String ruleDescription;
    protected final AlertSaver alertSaver;
    private static int totalCreatedAlertsCount = 0;

    public Rule(String ruleName, String ruleDescription, AlertSaver alertSaver) {
        this.ruleName = ruleName;
        this.ruleDescription = ruleDescription;
        this.alertSaver = alertSaver;
    }

    public abstract void processLog(Log log);

    public static int addTotalCreatedAlertsCount() {
        totalCreatedAlertsCount++;
        return totalCreatedAlertsCount;
    }
}
