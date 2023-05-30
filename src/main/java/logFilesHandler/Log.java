package logFilesHandler;

import java.time.LocalDateTime;

public class Log {
    private String componentName;
    private LocalDateTime dateTime;
    private String type;
    private String msg;

    public Log(String componentName, LocalDateTime dateTime, String type, String msg) {
        this.componentName = componentName;
        this.dateTime = dateTime;
        this.type = type;
        this.msg = msg;
    }
}
