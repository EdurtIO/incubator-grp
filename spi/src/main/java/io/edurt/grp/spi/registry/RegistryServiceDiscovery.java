package io.edurt.grp.spi.registry;

import com.google.common.collect.ArrayListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegistryServiceDiscovery {

    private final static Logger LOGGER = LoggerFactory.getLogger(RegistryServiceDiscovery.class);

    private String serviceAddress;
    /**
     * RPC服务列表
     */
    private volatile ArrayListMultimap services = ArrayListMultimap.create();

    public RegistryServiceDiscovery(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public void syncService() {
        LOGGER.info("同步节点{} RPC服务列表", serviceAddress);
    }

    public ArrayListMultimap getServices() {
        return services;
    }

}
