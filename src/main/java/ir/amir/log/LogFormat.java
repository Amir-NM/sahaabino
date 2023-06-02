package ir.amir.log;

import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogFormat {
    private final String separator;
    private final String datetimePattern;
    private int datetimeIndex, typeIndex, msgIndex;

    public LogFormat(String separator, String datetimePattern, String logFormat) throws FileNotFoundException {
        this.separator = separator;
        this.datetimePattern = datetimePattern;
        String[] format = logFormat.split(this.separator);
        for(int i = 0; i < format.length; i++) {
            switch (format[i]) {
                case "DATETIME" -> this.datetimeIndex = i;
                case "TYPE" -> this.typeIndex = i;
                case "MSG" -> this.msgIndex = i;
            }
        }
    }

    public Log formLog(String componentName, String logString) {
        String[] logArray = logString.split(this.separator);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.datetimePattern);
        LocalDateTime dateTime = LocalDateTime.parse(logArray[this.datetimeIndex], formatter);
        return new Log(componentName, dateTime, logArray[this.typeIndex], logArray[this.msgIndex]);
    }
}
