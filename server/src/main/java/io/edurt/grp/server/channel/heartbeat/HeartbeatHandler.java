package io.edurt.grp.server.channel.heartbeat;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

import java.nio.charset.Charset;
import java.util.Date;

public class HeartbeatHandler
        extends ChannelInboundHandlerAdapter
{
    int readIdleTimes = 0;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
    {
        ByteBuf byteBuf = (ByteBuf) msg;
        String message = byteBuf.toString(Charset.forName("utf-8"));
        System.out.println(new Date() + ": 服务端读到数据 -> " + message);
        /** 心跳数据是不发送数据 **/
        if (!message.contains("heartbeat")) {
            ByteBuf out = getByteBuf(ctx);
            ctx.channel().writeAndFlush(out);
        }
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx)
    {
        byte[] bytes = "我是发送给客户端的数据：请重启冰箱!".getBytes(Charset.forName("utf-8"));
        ByteBuf buffer = ctx.alloc().buffer();
        buffer.writeBytes(bytes);
        return buffer;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
            throws Exception
    {
        IdleStateEvent event = (IdleStateEvent) evt;
        String eventType = null;
        switch (event.state()) {
            case READER_IDLE:
                eventType = "读空闲";
                readIdleTimes++; // 读空闲的计数加1
                break;
            case WRITER_IDLE:
                eventType = "写空闲";
                // 不处理
                break;
            case ALL_IDLE:
                eventType = "读写空闲";
                // 不处理
                break;
        }
        System.out.println(ctx.channel().remoteAddress() + "超时事件：" + eventType);
        if (readIdleTimes > 3) {
            System.out.println(" [server]读空闲超过3次，关闭连接");
            ctx.channel().writeAndFlush("you are out");
            ctx.channel().close();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception
    {
        System.err.println("=== " + ctx.channel().remoteAddress() + " is active ===");
    }
}
