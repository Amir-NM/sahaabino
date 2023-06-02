package ir.amir.ingestor.config;

import org.apache.kafka.common.protocol.types.Field;

public class RecordExtractorConfig {
    private String separator;
    private String dateTimePattern;
    private String logFormat;

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public String getDateTimePattern() {
        return dateTimePattern;
    }

    public void setDateTimePattern(String dateTimePattern) {
        this.dateTimePattern = dateTimePattern;
    }

    public String getLogFormat() {
        return logFormat;
    }

    public void setLogFormat(String logFormat) {
        this.logFormat = logFormat;
    }
}
