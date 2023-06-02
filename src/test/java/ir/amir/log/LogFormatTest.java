package ir.amir.log;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogFormatTest {
    @Test
    public void formLogTest() {
        LogFormat logFormat = new LogFormat("--", "yyyy-MM-dd HH:mm:ss", "DATETIME--TYPE--MSG");
        Log log = logFormat.formLog("x", "2023-10-10 18:20:30--ERROR--massage 1");
        Log expected = new Log("x", LocalDateTime.parse("2023-10-10 18:20:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                "ERROR", "massage 1");
        Assertions.assertEquals(expected.toString(), log.toString());
        Assertions.assertEquals(0, log.getTimeDifference(expected));
    }
}
