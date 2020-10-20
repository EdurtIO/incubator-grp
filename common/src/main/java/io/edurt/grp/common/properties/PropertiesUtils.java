package io.edurt.grp.common.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    public static Properties loadProperties(String... resourcesPaths) {
        Properties props = new Properties();
        for (String location : resourcesPaths) {
            LOGGER.debug("加载配置文件路径地址 {}", location);
            File propertiesFile = new File(location);
            try (InputStream inputStream = new FileInputStream(propertiesFile)) {
                props.load(inputStream);
            } catch (IOException ex) {
                LOGGER.info("无法加载Properties文件路径:" + location + ": " + ex.getMessage());
            }
        }
        return props;
    }

}
