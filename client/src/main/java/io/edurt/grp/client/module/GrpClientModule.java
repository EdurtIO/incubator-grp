package io.edurt.grp.client.module;

import com.google.inject.Provides;
import io.edurt.grp.client.GrpClient;
import io.edurt.grp.spi.GrpModule;
import io.edurt.grp.spi.registry.RegistryServiceDiscovery;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GrpClientModule extends GrpModule {

    private final static Logger LOGGER = LoggerFactory.getLogger(GrpClientModule.class);

    private List<Class> serviceInterfaceList;
    private String registryAddress;

    public GrpClientModule(String registryAddress, List<Class> serviceInterfaceList) {
        this.registryAddress = registryAddress;
        this.serviceInterfaceList = serviceInterfaceList;
    }

    @Override
    protected void configure() {
        GrpClient grpClient = new GrpClient(getServiceDiscovery());
        bind(GrpClient.class).toInstance(grpClient);
        LOGGER.info("绑定RPC客户端信息");
        if (ObjectUtils.isNotEmpty(serviceInterfaceList) && serviceInterfaceList.size() > 0) {
            serviceInterfaceList.forEach(clazz -> bind(clazz).toInstance(grpClient.create(clazz)));
        }
    }

    @Provides
    private RegistryServiceDiscovery getServiceDiscovery() {
        return new RegistryServiceDiscovery(registryAddress);
    }

}
