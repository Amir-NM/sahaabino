package ir.amir.evaluator.rule;

import ir.amir.rest.Alert;
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

    public static void setTotalCreatedAlertsCount(int totalCreatedAlertsCount) {
        Rule.totalCreatedAlertsCount = totalCreatedAlertsCount;
    }
}
