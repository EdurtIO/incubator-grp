package io.edurt.grp.spi.registry;

import com.google.common.collect.ArrayListMultimap;
import io.edurt.grp.common.utils.NetWorksUtils;
import io.edurt.grp.component.zookeeper.client.ZookeeperClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class RegistryServiceDiscovery
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryServiceDiscovery.class);

    /**
     * RPC服务列表
     */
    private volatile ArrayListMultimap services = ArrayListMultimap.create();

    private ZookeeperClient zookeeperClient;

    public RegistryServiceDiscovery(ZookeeperClient zookeeperClient)
    {
        this.zookeeperClient = zookeeperClient;
    }

    /**
     * 同步RPC服务节点信息
     */
    public void syncService()
    {
        String serviceAddress = NetWorksUtils.getHostName();
        LOGGER.info("同步节点 {} RPC服务列表", serviceAddress);
        Arrays.stream(this.zookeeperClient.getNode(serviceAddress).split(","))
                .parallel()
                .forEach(v -> services.put(v, v));
        LOGGER.info("同步节点 {} RPC服务列表完成，共同步{}个服务", serviceAddress, services.size());
    }

    public ArrayListMultimap getServices()
    {
        return services;
    }
}
