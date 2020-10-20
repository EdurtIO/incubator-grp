package io.edurt.grp.server;

import com.google.inject.Guice;
import com.google.inject.Module;
import io.edurt.grp.common.properties.PropertiesUtils;
import io.edurt.grp.server.netty.NettyServer;
import io.edurt.grp.spi.ConfigurationModule;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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
        if (ObjectUtils.isNotEmpty(modules)) {
            modules.stream().forEach(v -> LOGGER.info("当前加载模块名 <{}>", v.toString()));
        }
        LOGGER.info("加载模块完成，共加载{}个模块", modules.size());
        Guice.createInjector(modules);
        Thread thread = NettyServer.build()
                .bindPort(PropertiesUtils.getIntValue(configuration,
                        GrpConfiguration.SERVER_PORT,
                        GrpConfigurationDefault.SERVER_PORT))
                .start();
        LOGGER.info("Grp服务启动成功！");
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
