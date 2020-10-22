package io.edurt.grp.web.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.edurt.grp.web.model.Param;
import io.edurt.grp.web.model.Router;
import io.edurt.grp.web.type.RequestMethod;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.AsciiString;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class GrpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    private final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();
    private final String DEFAULT_CHARSET = "UTF-8";

    private Map<String, Router> routers;
    private AsciiString contentType = HttpHeaderValues.APPLICATION_JSON;

    public GrpServerHandler(Map<String, Router> routers) {
        this.routers = routers;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest request) {
        LOGGER.info("Grp Handler请求: {}", request);
        Object result;
        HttpResponseStatus status = null;
        try {
            List<Object> params = new ArrayList<>(16);
            LOGGER.debug("解析客户端URL中的用户传递参数");
            QueryStringDecoder decoder = new QueryStringDecoder(request.uri(), Charset.forName(DEFAULT_CHARSET));
            String url = decoder.path();
            String method = request.method().toString();
            Map<String, List<String>> urlParams = decoder.parameters();
            LOGGER.debug("客户端请求路径{}, 请求方法{}, 请求参数{}", url, method, urlParams);
            Router router = getRoute(url);
            if (ObjectUtils.isEmpty(router)) {
                LOGGER.error("URL不存在请检查URL设置是否正确, URL路径 {}", url);
                throw new RuntimeException(String.format("URL <%s> does not exist!", url));
            }
            MethodHandler.getMethod(router.getMethods(), method, url);
            LOGGER.debug("解析controller请求参数信息");
            List<Param> paramInfo = router.getParams();
            if (ObjectUtils.isNotEmpty(paramInfo)) {
                Map<String, String> pathParams = PathHandler.getParams(url, router.getUrls());
                Gson initJson = null;
                if (method.equalsIgnoreCase(RequestMethod.POST.name())) {
                    String contentType = request.headers().get("Content-Type");
                    if (StringUtils.isNotEmpty(contentType) && contentType.contains("application/json")) {
                        initJson = GSON.fromJson(request.content().toString(Charset.forName(DEFAULT_CHARSET)), Gson.class);
                    }
                }
                Gson finalJson = initJson;
                paramInfo.forEach(param -> {
                    String type = param.getType();
                    String value = param.getValue();
                    if (type.equals("path")) {
                        params.add(pathParams.get(value));
                    }
                    if (type.equals("request")) {
                        params.add(urlParams.get(value).get(0));
                    }
                    if (type.equals("json")) {
                        params.add(finalJson);
                    }
                });
                result = executeMethod(router, params.toArray());
                status = HttpResponseStatus.OK;
            } else {
                result = executeMethod(router);
            }
        } catch (Exception e) {
            result = e.getMessage();
        } finally {
            if (status == null) {
                status = HttpResponseStatus.INTERNAL_SERVER_ERROR;
            }
        }
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                status,
                Unpooled.wrappedBuffer(GSON.toJson(result).getBytes()));
        HttpHeaders heads = response.headers();
        heads.add(HttpHeaderNames.CONTENT_TYPE, contentType + "; charset=UTF-8");
        heads.add(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        heads.add(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        channelHandlerContext.write(response);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        super.channelReadComplete(ctx);
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        if (null != cause) cause.printStackTrace();
        if (null != ctx) ctx.close();
    }

    private Router getRoute(String url) {
        AtomicReference<Router> router = new AtomicReference<>();
        routers.keySet().forEach(routerKey -> {
            if (PathHandler.verify(url, routerKey)) {
                router.set(routers.get(routerKey));
            }
        });
        return router.get();
    }

    private Object executeMethod(Router router, Object... params) throws Exception {
        Class<?> cls = router.getClazz();
        Object obj = cls.newInstance();
        Method method = router.getMethod();
        return method.invoke(obj, params);
    }

}