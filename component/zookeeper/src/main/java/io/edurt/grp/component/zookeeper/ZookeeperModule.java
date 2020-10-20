package io.edurt.grp.component.zookeeper;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import io.edurt.grp.component.zookeeper.client.ZookeeperClient;
import io.edurt.grp.component.zookeeper.provider.ZookeeperProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperModule extends AbstractModule {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZookeeperModule.class);

    @Override
    protected void configure() {
        // TODO: 后续支持独立读取配置信息
        LOGGER.debug("绑定Zookeeper组件信息");
        bind(ZookeeperClient.class).toProvider(ZookeeperProvider.class).in(Scopes.SINGLETON);
    }

}
