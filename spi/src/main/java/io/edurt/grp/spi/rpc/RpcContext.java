package io.edurt.grp.spi.rpc;

import com.google.inject.Injector;
import io.edurt.grp.spi.annotation.RpcService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RpcContext
{
    private  static final Logger LOGGER = LoggerFactory.getLogger(RpcContext.class);
    private static RpcContext context;
    private final Injector injector;
    // 用于存放所有的RPC服务列表
    private Map<Class, Object> services;

    private RpcContext(Injector injector)
    {
        this.injector = injector;
        this.initService();
    }

    public static RpcContext build(Injector injector)
    {
        LOGGER.info("初始化构建Grp Context上下文");
        RpcContext.context = new RpcContext(injector);
        return context;
    }

    /**
     * 初始化所有RPC服务，默认方法基于GrpRpcService注解
     *
     * @see RpcService
     */
    private void initService()
    {
        LOGGER.info("初始化加载RPC服务列表");
        if (ObjectUtils.isEmpty(services) || services.size() <= 0) {
            LOGGER.info("RPC服务列表为空即将创建一个新的存放容器");
            services = new ConcurrentHashMap<>();
        }
        LOGGER.info("获取Guice容器中所有绑定的RPC服务");
        this.injector.getBindings().forEach((key, binding) -> {
            Class serviceClass = binding.getKey().getTypeLiteral().getRawType();
            if (serviceClass.isInterface()) {
                if (serviceClass.isAnnotationPresent(RpcService.class)) {
                    Object serviceImplClass = injector.getInstance(serviceClass);
                    services.put(serviceClass, serviceImplClass);
                    LOGGER.info("当前加载的RPC服务 <{}>", serviceClass.getSimpleName());
                }
            }
        });
        LOGGER.info("共加载{}个绑定的RPC服务", services.size());
    }

    public Map<Class, Object> getServices()
    {
        return services;
    }
}
