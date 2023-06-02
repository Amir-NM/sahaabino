package ir.amir.evaluator.rule;

import ir.amir.log.Log;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class DurationBasedRule extends Rule {
    protected final double duration;
    protected final HashMap<String, ArrayList<Log>> componentsRecentLogs;

    public DurationBasedRule(String ruleName, String ruleDescription, double duration) {
        super(ruleName, ruleDescription);
        this.duration = duration;
        this.componentsRecentLogs = new HashMap<>();
    }

    public void addLogToComponentsRecentLogs(Log log) {
        if(!this.componentsRecentLogs.containsKey(log.getComponentName())) {
            this.componentsRecentLogs.put(log.getComponentName(), new ArrayList<>());
        }

        ArrayList<Log> logs = this.componentsRecentLogs.get(log.getComponentName());
        logs.add(log);

        while (logs.get(0).getTimeDifference(log) > this.duration) {
            logs.remove(0);
        }
    }
}
