package ir.amir.evaluator.config.rules;

public class ThirdRuleTypeConfig {
    private String name;
    private String description;
    private double duration;
    private int logCreationRateLimit;

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

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getLogCreationRateLimit() {
        return logCreationRateLimit;
    }

    public void setLogCreationRateLimit(int logCreationRateLimit) {
        this.logCreationRateLimit = logCreationRateLimit;
    }
}
