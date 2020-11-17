package io.edurt.grp.client.proxy;

import io.edurt.grp.client.GrpClient;
import io.edurt.grp.proto.GrpRequest;
import io.edurt.grp.proto.GrpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.time.LocalTime;
import java.util.UUID;

public class GrpInvocationProxy
        implements InvocationHandler
{
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpInvocationProxy.class);

    private Class clazz;
    private GrpClient grpClient;

    public GrpInvocationProxy(Class clazz, GrpClient grpClient)
    {
        this.clazz = clazz;
        this.grpClient = grpClient;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable
    {
        GrpRequest request = new GrpRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        request.setTime(LocalTime.now());
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("invoke class: {}", clazz);
            LOGGER.debug("invoke method: {}", method.getName());
            for (int i = 0; i < method.getParameterTypes().length; ++i) {
                LOGGER.debug(method.getParameterTypes()[i].getName());
            }
            for (int i = 0; i < args.length; ++i) {
                LOGGER.debug(args[i].toString());
            }
        }
        GrpResponse grpResponse = grpClient.callRemoteService(request);
        return grpResponse;
    }
}
