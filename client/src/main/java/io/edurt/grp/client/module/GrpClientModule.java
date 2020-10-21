package io.edurt.grp.client.module;

import com.google.inject.Provides;
import io.edurt.grp.client.GrpClient;
import io.edurt.grp.component.zookeeper.client.ZookeeperClient;
import io.edurt.grp.spi.GrpModule;
import io.edurt.grp.spi.registry.RegistryServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GrpClientModule extends GrpModule {

    private final static Logger LOGGER = LoggerFactory.getLogger(GrpClientModule.class);

    private ZookeeperClient zookeeperClient;

    public GrpClientModule(ZookeeperClient zookeeperClient) {
        this.zookeeperClient = zookeeperClient;
    }

    @Override
    protected void configure() {
        GrpClient grpClient = new GrpClient(getServiceDiscovery());
        bind(GrpClient.class).toInstance(grpClient);
        LOGGER.info("绑定RPC客户端信息");
//        if (ObjectUtils.isNotEmpty(serviceInterfaceList) && serviceInterfaceList.size() > 0) {
//            serviceInterfaceList.forEach(clazz -> bind(clazz).toInstance(grpClient.create(clazz)));
//        }
    }

    @Provides
    private RegistryServiceDiscovery getServiceDiscovery() {
        return new RegistryServiceDiscovery(this.zookeeperClient);
    }

}
