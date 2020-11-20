package io.edurt.grp.spi.registry;

import io.edurt.grp.component.zookeeper.client.ZookeeperClient;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class RegistryServiceFactory
{
    private static final Logger LOGGER = LoggerFactory.getLogger(RegistryServiceFactory.class);

    /**
     * 用于缓冲上报的服务
     */
    private static final ConcurrentHashMap<String, RegistryService> registrys = new ConcurrentHashMap<>();
    private static RegistryServiceFactory factory;
    private RegistryService service;

    private ZookeeperClient zookeeperClient;

    private RegistryServiceFactory()
    {}

    /**
     * 构建新的服务服务注册中心
     *
     * @param service 需要注册的服务配置
     * @return 服务注册中心
     */
    public static RegistryServiceFactory build(RegistryService service, ZookeeperClient zookeeperClient)
    {
        if (ObjectUtils.isEmpty(factory)) {
            factory = new RegistryServiceFactory();
        }
        factory.service = service;
        factory.zookeeperClient = zookeeperClient;
        return factory;
    }

    /**
     * 注册新的实例信息
     */
    public synchronized void register()
    {
        if (ObjectUtils.isEmpty(factory)) {
            LOGGER.error("无法初始化实例，请先构建它!");
            throw new RuntimeException("Unable to initialize instance, please build it first!");
        }
        LOGGER.info("插入节点{}数据信息，插入时间为 {}", factory.service.getHostname(), LocalTime.now());
        if (ObjectUtils.isNotEmpty(registrys.get(factory.service.getHostname()))) {
            LOGGER.info("当前节点{}数据信息已经在存储中缓冲，跳过此步操作", factory.service.getHostname());
        }
        else {
            this.zookeeperClient.createEphemeralNode(factory.service.getHostname(),
                    factory.service.getServices().stream().collect(Collectors.joining(",")));
        }
    }
}
