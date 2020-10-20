package io.edurt.grp.common.utils;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesUtils {

    private final static Logger LOGGER = LoggerFactory.getLogger(PropertiesUtils.class);

    /**
     * 加载配置信息
     *
     * @param resourcesPaths 配置文件路径
     * @return 装配的配置信息
     */
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

    /**
     * 读取int类型数据
     *
     * @param properties   读取的配置文件信息
     * @param key          读取数据的key
     * @param defaultValue 默认值
     * @return 读取到的数据，读取不到则返回默认值
     */
    public static Integer getIntValue(Properties properties, String key, Integer defaultValue) {
        if (ObjectUtils.isEmpty(properties)) {
            LOGGER.debug("传递的配置文件为空，将使用默认值");
            return defaultValue;
        }
        if (!properties.containsKey(key)) {
            LOGGER.debug("传递的配置尚未包含需要解析的主键，将使用默认值");
            return defaultValue;
        }
        return Integer.valueOf(String.valueOf(properties.getOrDefault(key, defaultValue)));
    }

}
