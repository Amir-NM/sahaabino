package ir.amir.evaluator.config.rules;

public class SecondRuleTypeConfig {
    private String name;
    private String description;
    private String logsType;
    private double duration;
    private int logCountLimit;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLogsType() {
        return logsType;
    }

    public void setLogsType(String logsType) {
        this.logsType = logsType;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getLogCountLimit() {
        return logCountLimit;
    }

    public void setLogCountLimit(int logCountLimit) {
        this.logCountLimit = logCountLimit;
    }
}
