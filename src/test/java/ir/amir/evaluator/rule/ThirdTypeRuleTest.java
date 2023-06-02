package ir.amir.evaluator.rule;

import ir.amir.evaluator.config.rules.ThirdRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.Alert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ThirdTypeRuleTest {
    @Test
    public void processLogTest() {
        ThirdRuleTypeConfig thirdRuleTypeConfig = new ThirdRuleTypeConfig();
        thirdRuleTypeConfig.setName("rule name");
        thirdRuleTypeConfig.setDescription("rule description");
        thirdRuleTypeConfig.setDuration(1);
        thirdRuleTypeConfig.setLogCreationRateLimit(3);

        ThirdTypeRule thirdTypeRule = new ThirdTypeRule(thirdRuleTypeConfig);

        Log log1 = new Log("x", LocalDateTime.of(2023, 10, 10, 22, 15, 0), "ERROR", "message 1");
        Log log2 = new Log("x", LocalDateTime.of(2023, 10, 10, 22, 15, 30), "ERROR", "message 2");
        Log log3 = new Log("x", LocalDateTime.of(2023, 10, 10, 22, 15, 40), "ERROR", "message 3");
        Log log4 = new Log("y", LocalDateTime.of(2023, 10, 10, 22, 16, 1), "ERROR", "message 4");
        Log log5 = new Log("x", LocalDateTime.of(2023, 10, 10, 22, 16, 10), "WARNING", "message 5");

        Alert alert = thirdTypeRule.processLog(log1);
        Assertions.assertNull(alert);

        alert = thirdTypeRule.processLog(log2);
        Assertions.assertNull(alert);

        alert = thirdTypeRule.processLog(log3);
        Assertions.assertEquals("rule name", alert.getRuleName());
        Assertions.assertEquals("x", alert.getComponentName());
        Assertions.assertEquals("rule description | current log creation rate: 3.0 | last log message: message 3", alert.getDescription());

        alert = thirdTypeRule.processLog(log4);
        Assertions.assertNull(alert);

        alert = thirdTypeRule.processLog(log5);
        Assertions.assertEquals("rule name", alert.getRuleName());
        Assertions.assertEquals("x", alert.getComponentName());
        Assertions.assertEquals("rule description | current log creation rate: 3.0 | last log message: message 5", alert.getDescription());
    }
}
