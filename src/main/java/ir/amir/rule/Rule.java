package ir.amir.rule;

import ir.amir.rest.database.Alert;
import ir.amir.rest.database.AlertSaver;
import ir.amir.log.Log;

public abstract class Rule {
    protected final String ruleName;
    protected final String ruleDescription;
    private static int totalCreatedAlertsCount = 0;

    public Rule(String ruleName, String ruleDescription) {
        this.ruleName = ruleName;
        this.ruleDescription = ruleDescription;
    }

    public abstract Alert processLog(Log log);

    public static int addTotalCreatedAlertsCount() {
        totalCreatedAlertsCount++;
        return totalCreatedAlertsCount;
    }
}
