package log;


import java.time.LocalDateTime;
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
}
