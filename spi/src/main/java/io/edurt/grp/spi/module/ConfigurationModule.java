package io.edurt.grp.spi.module;

import com.google.inject.name.Names;
import io.edurt.grp.common.utils.PropertiesUtils;
import io.edurt.grp.spi.GrpModule;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ConfigurationModule extends GrpModule {

    private final static Logger LOGGER = LoggerFactory.getLogger(ConfigurationModule.class);

    private Properties bootstrapConfiguration;

    public ConfigurationModule(Properties bootstrapConfiguration) {
        this.bootstrapConfiguration = bootstrapConfiguration;
    }

    public ConfigurationModule(String configurationFilePath) {
        LOGGER.info("加载本地配置文件，路径地址为 {}", configurationFilePath);
        if (ObjectUtils.isNotEmpty(configurationFilePath)) {
            this.bootstrapConfiguration = PropertiesUtils.loadProperties(configurationFilePath);
        }
    }

    @Override
    protected void configure() {
        LOGGER.info("开始绑定配置文件相关配置信息");
        this.bootstrapConfiguration
                .stringPropertyNames()
                .forEach(v -> LOGGER.info("绑定的配置信息主键为{}，值为{}", v, bootstrapConfiguration.get(v)));
        Names.bindProperties(binder(), bootstrapConfiguration);
        LOGGER.info("绑定配置文件相关配置信息结束，共绑定{}个配置信息", bootstrapConfiguration.stringPropertyNames().size());
    }

}
