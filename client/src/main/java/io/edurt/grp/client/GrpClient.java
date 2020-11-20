package io.edurt.grp.client;

import com.google.common.collect.ArrayListMultimap;
import io.edurt.grp.client.proxy.GrpInvocationProxy;
import io.edurt.grp.proto.GrpRequest;
import io.edurt.grp.proto.GrpResponse;
import io.edurt.grp.spi.registry.RegistryServiceDiscovery;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

public class GrpClient
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpClient.class);

    /**
     * 服务调用频率
     */
    private static final ConcurrentHashMap<String, AtomicLong> FREQUENCY = new ConcurrentHashMap();

    private RegistryServiceDiscovery serviceDiscovery;

    public GrpClient(RegistryServiceDiscovery serviceDiscovery)
    {
        this.serviceDiscovery = serviceDiscovery;
        serviceDiscovery.syncService();
    }

    private RegistryServiceDiscovery getServiceDiscovery()
    {
        return this.serviceDiscovery;
    }

    /**
     * 创建对应服务代理
     *
     * @param clazz 需要创建的服务类
     * @param <T> 服务类
     * @return 服务代理
     */
    public <T> T create(Class<T> clazz)
    {
        LOGGER.debug("创建服务{}代理信息", clazz.toGenericString());
        T proxyInstance = (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[] {clazz}, new GrpInvocationProxy(clazz, this));
        return proxyInstance;
    }

    public GrpResponse callRemoteService(GrpRequest request)
    {
        AtomicLong invokeFrequency = FREQUENCY.get(request.getClassName());
        if (ObjectUtils.isEmpty(invokeFrequency)) {
            invokeFrequency = new AtomicLong(0);
            FREQUENCY.put(request.getClassName(), invokeFrequency);
        }
//        long totalInvokeTimes = invokeFrequency.getAndAdd(1);
        ArrayListMultimap map = this.getServiceDiscovery().getServices();
        List<String> remoteServices = map.get(request.getClassName());
        if (ObjectUtils.isEmpty(remoteServices) || remoteServices.size() <= 0) {
            LOGGER.error("远程服务中无法找到服务列表");
            throw new RuntimeException("remote service not found");
        }
//        Long index = totalInvokeTimes % remoteServices.size();
//        String remoteServiceAddress = remoteServices.get(index.intValue());
        return null;
    }
}
