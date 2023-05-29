package logFilesHandler;

import java.time.LocalDateTime;

public class Log {
    private LocalDateTime dateTime;
    private String type;
    private String msg;

    public Log(LocalDateTime dateTime, String type, String msg) {
        this.dateTime = dateTime;
        this.type = type;
        this.msg = msg;
    }
}
