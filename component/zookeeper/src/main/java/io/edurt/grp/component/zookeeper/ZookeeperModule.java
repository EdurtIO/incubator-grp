package io.edurt.grp.component.zookeeper;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import io.edurt.grp.common.utils.PropertiesUtils;
import io.edurt.grp.component.zookeeper.client.ZookeeperClient;
import io.edurt.grp.component.zookeeper.provider.ZookeeperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class ZookeeperModule
        extends AbstractModule
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperModule.class);

    @Override
    protected void configure()
    {
        // TODO: 后续支持独立读取配置信息
        LOGGER.debug("绑定Zookeeper组件信息");
        String configurationPath = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "component",
                "zookeeper.properties");
        Properties configuration = PropertiesUtils.loadProperties(configurationPath);
        LOGGER.info("绑定Zookeeper配置信息完成，共绑定{}个配置", configuration.stringPropertyNames().size());
        ZookeeperProvider zookeeperProvider = new ZookeeperProvider(configuration);
        bind(ZookeeperClient.class).toProvider(zookeeperProvider).in(Scopes.SINGLETON);
    }
}
