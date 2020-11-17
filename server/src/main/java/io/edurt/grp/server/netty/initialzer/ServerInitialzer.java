package io.edurt.grp.server.netty.initialzer;

import io.edurt.grp.server.netty.handler.HttpHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class ServerInitialzer
        extends ChannelInitializer<SocketChannel>
{
    @Override
    protected void initChannel(SocketChannel ch)
            throws Exception
    {
        ChannelPipeline pipeline = ch.pipeline();
        // netty是基于http的，所以要添加http编码器
        pipeline.addLast(new HttpServerCodec());
        // 对写大数据流的支持
        pipeline.addLast(new ChunkedWriteHandler());
        // 设置单次请求的文件大小上限
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024 * 10));
        // websocket 服务器处理的协议，用于指定给客户端连接访问的路由 : /ws
        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
        //自定义的路由
        pipeline.addLast(new HttpHandler());
    }
}
