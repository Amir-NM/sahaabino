package ir.amir.evaluator.config;

import ir.amir.evaluator.config.rules.FirstRuleTypeConfig;
import ir.amir.evaluator.config.rules.SecondRuleTypeConfig;
import ir.amir.evaluator.config.rules.ThirdRuleTypeConfig;

import java.util.List;

public class AlertExtractorConfig {
    private List<FirstRuleTypeConfig> firstRuleTypeConfigs;
    private List<SecondRuleTypeConfig> secondRuleTypeConfigs;
    private List<ThirdRuleTypeConfig> thirdRuleTypeConfigs;

    public List<FirstRuleTypeConfig> getFirstRuleTypeConfigs() {
        return firstRuleTypeConfigs;
    }

    public void setFirstRuleTypeConfigs(List<FirstRuleTypeConfig> firstRuleTypeConfigs) {
        this.firstRuleTypeConfigs = firstRuleTypeConfigs;
    }

    public List<SecondRuleTypeConfig> getSecondRuleTypeConfigs() {
        return secondRuleTypeConfigs;
    }

    public void setSecondRuleTypeConfigs(List<SecondRuleTypeConfig> secondRuleTypeConfigs) {
        this.secondRuleTypeConfigs = secondRuleTypeConfigs;
    }

    public List<ThirdRuleTypeConfig> getThirdRuleTypeConfigs() {
        return thirdRuleTypeConfigs;
    }

    public void setThirdRuleTypeConfigs(List<ThirdRuleTypeConfig> thirdRuleTypeConfigs) {
        this.thirdRuleTypeConfigs = thirdRuleTypeConfigs;
    }
}
