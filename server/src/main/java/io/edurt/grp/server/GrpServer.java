package io.edurt.grp.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.edurt.grp.common.utils.NetWorksUtils;
import io.edurt.grp.common.utils.PropertiesUtils;
import io.edurt.grp.component.zookeeper.ZookeeperModule;
import io.edurt.grp.component.zookeeper.client.ZookeeperClient;
import io.edurt.grp.server.module.GrpServiceModule;
import io.edurt.grp.server.netty.NettyServer;
import io.edurt.grp.spi.module.ConfigurationModule;
import io.edurt.grp.spi.registry.RegistryService;
import io.edurt.grp.spi.registry.RegistryServiceFactory;
import io.edurt.grp.spi.rpc.RpcContext;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class GrpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(GrpServer.class);

    public static void main(String[] args) {
        LOGGER.info("Grp服务启动，开始时间 {}", LocalDateTime.now());
        List<Module> modules = getModules();
        LOGGER.info("开始加载模块");
        LOGGER.info("当前加载配置信息模块");
        String configPath = String.join(File.separator, System.getProperty("user.dir"), "conf");
        Properties configuration = PropertiesUtils.loadProperties(String.join(File.separator, configPath, "application.properties"));
        modules.add(new ConfigurationModule(configuration));
        modules.add(new GrpServiceModule());
        modules.add(new ZookeeperModule());
        if (ObjectUtils.isNotEmpty(modules)) {
            modules.stream().forEach(v -> LOGGER.info("当前加载模块名 <{}>", v.toString()));
        }
        LOGGER.info("加载模块完成，共加载{}个模块", modules.size());
        Injector injector = Guice.createInjector(modules);
        RpcContext rpcContext = RpcContext.build(injector);
        Integer port = PropertiesUtils.getIntValue(configuration,
                GrpConfiguration.SERVER_PORT,
                GrpConfigurationDefault.SERVER_PORT);
        Thread thread = NettyServer.build().bindPort(port).start();
        LOGGER.info("Grp服务启动成功！");
        LOGGER.info("扫描节点绑定的RPC服务");
        RegistryService service = new RegistryService();
        List<String> services = rpcContext.getServices()
                .keySet()
                .stream()
                .map(clazz -> clazz.getName())
                .collect(Collectors.toList());
        service.setServices(services);
        service.setHostname(NetWorksUtils.getHostName());
        service.setPort(port);
        service.setId(service.getHostname());
        RegistryServiceFactory.build(service, injector.getInstance(ZookeeperClient.class)).register();
        try {
            thread.join();
            LOGGER.info("Grp服务启动成功！");
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error("Grp服务启动失败，错误信息 {}", e);
        }
    }

    private static List<Module> getModules() {
        return new ArrayList<>();
    }

}
