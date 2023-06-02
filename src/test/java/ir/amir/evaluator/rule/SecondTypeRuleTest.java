package ir.amir.evaluator.rule;

import ir.amir.evaluator.config.rules.SecondRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.Alert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class SecondTypeRuleTest {
    @Test
    public void processLogTest() {
        SecondRuleTypeConfig secondRuleTypeConfig = new SecondRuleTypeConfig();
        secondRuleTypeConfig.setName("rule name");
        secondRuleTypeConfig.setDescription("rule description");
        secondRuleTypeConfig.setLogsType("ERROR");
        secondRuleTypeConfig.setDuration(1);
        secondRuleTypeConfig.setLogCountLimit(3);

        SecondTypeRule secondTypeRule = new SecondTypeRule(secondRuleTypeConfig);

        Log log1 = new Log("x", LocalDateTime.of(2023, 10, 10, 22, 15, 0), "ERROR", "message 1");
        Log log2 = new Log("x", LocalDateTime.of(2023, 10, 10, 22, 15, 20), "ERROR", "message 2");
        Log log3 = new Log("x", LocalDateTime.of(2023, 10, 10, 22, 15, 40), "ERROR", "message 3");
        Log log4 = new Log("y", LocalDateTime.of(2023, 10, 10, 22, 16, 1), "ERROR", "message 4");
        Log log5 = new Log("x", LocalDateTime.of(2023, 10, 10, 22, 16, 2), "WARNING", "message 5");

        Alert alert = secondTypeRule.processLog(log1);
        Assertions.assertNull(alert);

        alert = secondTypeRule.processLog(log2);
        Assertions.assertNull(alert);

        alert = secondTypeRule.processLog(log3);
        Assertions.assertEquals("rule name", alert.getRuleName());
        Assertions.assertEquals("x", alert.getComponentName());
        Assertions.assertEquals("rule description | log type: ERROR | last log message: message 3 | second last log message: message 2", alert.getDescription());

        alert = secondTypeRule.processLog(log4);
        Assertions.assertNull(alert);

        alert = secondTypeRule.processLog(log5);
        Assertions.assertNull(alert);
    }
}
