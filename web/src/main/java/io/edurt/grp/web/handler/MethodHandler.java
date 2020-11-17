package io.edurt.grp.web.handler;

import io.edurt.grp.web.type.RequestMethod;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Optional;

public class MethodHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandler.class);

    private MethodHandler()
    {}

    public static String getMethod(RequestMethod[] methods, String method, String url)
    {
        if (ObjectUtils.isEmpty(methods) || methods.length <= 0) {
            LOGGER.error("客户端请求方式<{}>尚未设置，请求路径<{}>", method, url);
            throw new RuntimeException(String.format("Method <%s> is empty url <%s>!", method, url));
        }
        Optional<RequestMethod> methodOptional = Arrays.stream(methods)
                .filter(v -> v.name().equalsIgnoreCase(method))
                .findFirst();
        if (!methodOptional.isPresent()) {
            LOGGER.error("客户端请求方式<{}>尚未匹配，请求路径<{}>", method, url);
            throw new RuntimeException(String.format("Method <%s> does not match url <%s>!", method, url));
        }
        return methodOptional.get().name();
    }
}
