package ir.amir.log;


import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class Log {
    private final String componentName;
    private final LocalDateTime dateTime;
    private final String type;
    private final String msg;

    public Log(String componentName, LocalDateTime dateTime, String type, String msg) {
        this.componentName = componentName;
        this.dateTime = dateTime;
        this.type = type;
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public String getComponentName() {
        return this.componentName;
    }

    public boolean isOfType(String type) {
        return Objects.equals(this.type, type);
    }

    public double getTimeDifference(Log log) {
        return this.dateTime.until(log.dateTime, ChronoUnit.SECONDS) / 60.;
    }

    public String toString() {
        return "component: " + this.componentName + ", type: " + this.type + ", message: " + this.msg;
    }
}
