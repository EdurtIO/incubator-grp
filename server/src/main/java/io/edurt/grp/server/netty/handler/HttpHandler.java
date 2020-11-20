package io.edurt.grp.server.netty.handler;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.AsciiString;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * 自定义的路由 既可以实现http又可以实现socket
 *
 * @author songyan
 */
public class HttpHandler
        extends SimpleChannelInboundHandler<Object>
{
    private static final AsciiString CONTENT_TYPE = AsciiString.cached("Content-Type");
    private static final AsciiString TEXT_PLAIN = AsciiString.cached("text/plain; charset=utf-8");
    private static final AsciiString CONTENT_LENGTH = AsciiString.cached("Content-Length");
    private static final ChannelGroup CLIENTS = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 关闭释放channel
     *
     * @param ch
     */
    static void closeOnFlush(Channel ch)
    {
        if (ch != null && ch.isActive()) {
            ch.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
        }
    }

    /**
     * 打开链接
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception
    {
        System.out.println("websocket::::::::::: active");
        super.channelActive(ctx);
    }

    /**
     * 获取客户端的channle，添加到ChannelGroup中
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx)
            throws Exception
    {
        System.out.println("websocket::::::::::: add");
        CLIENTS.add(ctx.channel());
    }

    /**
     * 从ChannelGroup中移除channel
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx)
            throws Exception
    {
        System.out.println("websocket::::::::::: Removed");
    }

    /**
     * 销毁channel
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx)
            throws Exception
    {
        System.out.println("websocket::::::::::: destroyed");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)
    {
        ctx.flush();
    }

    /**
     * 异常捕获
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
    {
        System.err.println("出错了");
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 路由
     * 对http，websocket单独处理
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg)
            throws Exception
    {
        if (msg instanceof FullHttpRequest) { // 如果是HTTP请求，进行HTTP操作
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        }
        else if (msg instanceof WebSocketFrame) { // 如果是Websocket请求，则进行websocket操作
            handleWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    /**
     * 对http请求的处理
     */
    private void handleHttpRequest(ChannelHandlerContext ctx, final FullHttpRequest fullHttpRequest)
    {
        final String uri = fullHttpRequest.uri();
        final QueryStringDecoder queryStringDecoder = new QueryStringDecoder(uri);
        final String requestPath = queryStringDecoder.path();
        String body = "";
        switch (requestPath) {
            case "/hello":
                //请自行检测参数, 这里假设  /hello 是会带上 ?name=world 类似这参数值的
                body = "Hello " + queryStringDecoder.parameters().get("name").get(0);
                break;
            case "/netty":
                body = "Hello Netty.";
                break;
            default:
                break;
        }
        final DefaultFullHttpResponse defaultFullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                HttpResponseStatus.OK,
                Unpooled.copiedBuffer(body.getBytes(Charset.defaultCharset())));
        defaultFullHttpResponse.headers().set(CONTENT_TYPE, TEXT_PLAIN);
        defaultFullHttpResponse.headers().set(CONTENT_LENGTH, defaultFullHttpResponse.content().readableBytes());
        ctx.write(defaultFullHttpResponse);
    }

    /**
     * 对socket请求的处理
     */
    private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame msg)
    {
        // 获取客户端传输过来的消息
        String content = msg.toString();
        System.out.println("websocket：：：  接受到的数据：" + content);
        CLIENTS.writeAndFlush(new TextWebSocketFrame("[服务器在]" + LocalDateTime.now() + "接受到消息, 消息为：" + content));
    }
}
