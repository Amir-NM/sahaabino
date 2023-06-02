package kafka;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaConfigLoader {
    private static KafkaConfigLoader configLoader;

    private KafkaConfigLoader() {}

    public static KafkaConfigLoader getInstance() {
        if(configLoader == null)
            configLoader = new KafkaConfigLoader();
        return configLoader;
    }

    public Properties loadProps(String propsDir) throws IOException {
        Properties props = new Properties();
        InputStream inputStream = new FileInputStream(propsDir);
        props.load(inputStream);
        return props;
    }
}
