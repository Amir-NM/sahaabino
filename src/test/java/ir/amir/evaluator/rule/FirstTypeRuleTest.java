package ir.amir.evaluator.rule;

import ir.amir.evaluator.config.rules.FirstRuleTypeConfig;
import ir.amir.log.Log;
import ir.amir.rest.Alert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class FirstTypeRuleTest {
    @Test
    public void processLogTest() {
        FirstRuleTypeConfig firstRuleTypeConfig = new FirstRuleTypeConfig();
        firstRuleTypeConfig.setName("rule name");
        firstRuleTypeConfig.setDescription("rule description");
        firstRuleTypeConfig.setLogsType("ERROR");

        FirstTypeRule firstTypeRule = new FirstTypeRule(firstRuleTypeConfig);

        Log log = new Log("x", LocalDateTime.now(), "ERROR", "message");

        Alert alert = firstTypeRule.processLog(log);

        Assertions.assertEquals("rule name", alert.getRuleName());
        Assertions.assertEquals("x", alert.getComponentName());
        Assertions.assertEquals("rule description | log type: ERROR | log message: message", alert.getDescription());

        log = new Log("x", LocalDateTime.now(), "WARNING", "message");

        alert = firstTypeRule.processLog(log);

        Assertions.assertNull(alert);
    }
}
