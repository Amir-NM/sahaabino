package logFilesHandler;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class LogFormat {
    private final String separator;
    private final String datetimePattern;
    private int datetimeIndex, typeIndex, msgIndex;

    public LogFormat(String confDir) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(confDir));
        this.separator = sc.nextLine();
        this.datetimePattern = sc.nextLine();
        String[] format = sc.nextLine().split(this.separator);
        for(int i = 0; i < format.length; i++) {
            switch (format[i]) {
                case "DATETIME" -> this.datetimeIndex = i;
                case "TYPE" -> this.typeIndex = i;
                case "MSG" -> this.msgIndex = i;
            }
        }
        sc.close();
    }

    public Log formLog(String logString) {
        String[] logArray = logString.split(this.separator);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.datetimePattern);
        LocalDateTime dateTime = LocalDateTime.parse(logArray[this.datetimeIndex], formatter);
        return new Log(dateTime, logArray[this.typeIndex], logArray[this.msgIndex]);
    }
}
