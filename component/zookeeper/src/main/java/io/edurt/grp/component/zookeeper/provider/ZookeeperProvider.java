package io.edurt.grp.component.zookeeper.provider;

import com.google.inject.Provider;
import io.edurt.grp.common.utils.PropertiesUtils;
import io.edurt.grp.component.zookeeper.ZookeeperConfiguration;
import io.edurt.grp.component.zookeeper.ZookeeperConfigurationDefault;
import io.edurt.grp.component.zookeeper.client.ZookeeperClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

public class ZookeeperProvider implements Provider<ZookeeperClient> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZookeeperProvider.class);

    private Properties configuration;

    public ZookeeperProvider() {
        String configurationPath = String.join(File.separator, System.getProperty("user.dir"),
                "conf",
                "component",
                "zookeeper.properties");
        configuration = PropertiesUtils.loadProperties(configurationPath);
        LOGGER.info("绑定Zookeeper配置信息完成，共绑定{}个配置", configuration.stringPropertyNames().size());
    }

    private CuratorFramework getZookeeperClient() {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(
                PropertiesUtils.getIntValue(configuration,
                        ZookeeperConfiguration.CONNECTION_TIMEOUT,
                        ZookeeperConfigurationDefault.CONNECTION_TIMEOUT),
                PropertiesUtils.getIntValue(configuration,
                        ZookeeperConfiguration.RETRY,
                        ZookeeperConfigurationDefault.RETRY));
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(PropertiesUtils.getStringValue(configuration,
                        ZookeeperConfiguration.SERVICE,
                        ZookeeperConfigurationDefault.SERVICE))
                .sessionTimeoutMs(PropertiesUtils.getIntValue(configuration,
                        ZookeeperConfiguration.SESSION_TIMEOUT,
                        ZookeeperConfigurationDefault.SESSION_TIMEOUT))
                .connectionTimeoutMs(PropertiesUtils.getIntValue(configuration,
                        ZookeeperConfiguration.CONNECTION_TIMEOUT,
                        ZookeeperConfigurationDefault.CONNECTION_TIMEOUT))
                .retryPolicy(retryPolicy)
                .namespace(PropertiesUtils.getStringValue(configuration,
                        ZookeeperConfiguration.NAMESPACE,
                        ZookeeperConfigurationDefault.NAMESPACE))
                .build();
        return client;
    }

    @Override
    public ZookeeperClient get() {
        return new ZookeeperClient(getZookeeperClient());
    }

}
