package io.edurt.grp.web;

import io.edurt.grp.web.router.RouterScan;
import org.apache.commons.lang3.ObjectUtils;

import java.util.concurrent.ConcurrentHashMap;

public class GrpConfiguration
{
    private static final String SERVER_CONTROLLERS = "io.edurt.grp.web.controller";
    private static final String SERVER_PORT = "io.edurt.grp.web.port";
    private ConcurrentHashMap<String, Object> configuration = new ConcurrentHashMap<>();
    private GrpServer httpServer;

    public GrpConfiguration(GrpServer httpServer)
    {
        this.httpServer = httpServer;
    }

    public GrpConfiguration set(String name, Object value)
    {
        configuration.put(name, value);
        return this;
    }

    public GrpConfiguration setPort(int port)
    {
        configuration.put(SERVER_PORT, port);
        return this;
    }

    public GrpConfiguration setControllers(Class<?>... className)
    {
        Class<?>[] oldClasses = getClasses();
        Class<?>[] newClasses = insertClasses(oldClasses, className);
        configuration.put(SERVER_CONTROLLERS, newClasses);
        return this;
    }

    public GrpConfiguration setController(Class<?> className)
    {
        Class<?>[] oldClasses = getClasses();
        Class<?>[] newClasses = insertClass(oldClasses, className);
        configuration.put(SERVER_CONTROLLERS, newClasses);
        return this;
    }

    public String getString(String key)
    {
        return configuration.get(key).toString();
    }

    public int getInt(String key)
    {
        return (int) configuration.get(key);
    }

    public Class<?>[] getClasses()
    {
        return (Class<?>[]) configuration.get(SERVER_CONTROLLERS);
    }

    private Class<?>[] insertClass(Class<?>[] oldClasses, Class<?> addClass)
    {
        Class<?>[] newClasses;
        if (ObjectUtils.isNotEmpty(oldClasses)) {
            int length = oldClasses.length;
            newClasses = new Class<?>[length + 1];
            System.arraycopy(oldClasses, 0, newClasses, 0, length);
            newClasses[length + 1] = addClass;
        }
        else {
            newClasses = new Class<?>[] {addClass};
        }
        return newClasses;
    }

    private Class<?>[] insertClasses(Class<?>[] oldClasses, Class<?>[] addClasses)
    {
        Class<?>[] newClasses;
        if (oldClasses != null) {
            int oldLength = oldClasses.length;
            int addLength = addClasses.length;
            newClasses = new Class<?>[oldLength + addLength];
            System.arraycopy(oldClasses, 0, newClasses, 0, oldLength);
            System.arraycopy(addClasses, 0, newClasses, oldLength, addLength);
        }
        else {
            newClasses = addClasses;
        }
        return newClasses;
    }

    public GrpServer create()
    {
        httpServer.setPort(this.getInt(SERVER_PORT));
        Class<?>[] classes = this.getClasses();
        httpServer.setRouters(RouterScan.getRouters(classes));
        return this.httpServer;
    }

    public ConcurrentHashMap<String, Object> getConfiguration()
    {
        return this.configuration;
    }
}
