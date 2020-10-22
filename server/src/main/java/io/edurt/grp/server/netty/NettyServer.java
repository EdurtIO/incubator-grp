package io.edurt.grp.server.netty;

import io.edurt.grp.server.channel.heartbeat.HeartbeatInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServer.class);

    private static NettyServer nettyServer;
    private Integer port;

    private NettyServer() {
    }

    /**
     * 构建服务实例
     *
     * @return 服务实例
     */
    public static NettyServer build() {
        if (ObjectUtils.isEmpty(nettyServer)) {
            nettyServer = new NettyServer();
        }
        return nettyServer;
    }

    /**
     * 绑定端口号
     *
     * @param port 端口号
     * @return 服务实例
     */
    public static NettyServer bindPort(Integer port) {
        if (ObjectUtils.isEmpty(nettyServer)) {
            LOGGER.error("无法初始化实例，请先构建它!");
            throw new RuntimeException("Unable to initialize instance, please build it first!");
        }
        nettyServer.port = port;
        return nettyServer;
    }

    /**
     * 构建启动Netty服务线程
     *
     * @return 当前运行Netty服务的线程
     */
    public Thread start() {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new ChannelInitializer() {
                    // 配置子Channel与ChannelHandler之间的关系
                    @Override
                    protected void initChannel(Channel socketChannel) {
                        // 往ChannelPipeline中添加ChannelHandler
                        socketChannel.pipeline().addLast(
                                new HttpRequestDecoder(),
                                new HttpObjectAggregator(65535),
                                new HttpResponseEncoder()
                        );
                    }
                });
        try {
            ChannelFuture future = bootstrap.bind(port).sync();
            Thread thread = new Thread(() -> {
                try {
                    future.channel().closeFuture().sync();
                } catch (InterruptedException e) {
                    LOGGER.error("服务启动内部出现异常，异常信息 {}", e);
                } finally {
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                }
            });
            thread.start();
            LOGGER.info("服务启动成功，监听端口 {}, 线程ID {}", port, thread.getId());
            return thread;
        } catch (InterruptedException e) {
            LOGGER.error("服务启动线程出现异常，异常信息 {}", e);
            throw new RuntimeException(e);
        }
    }

}
