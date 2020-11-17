package io.edurt.grp.component.zookeeper;

public class ZookeeperConfiguration
{
    /**
     * Zookeeper连接地址，例如 127.0.0.1:2181,127.0.0.1:2182
     */
    public static final String SERVICE = "component.zookeeper.service";

    /**
     * Zookeeper Session连接超时
     */
    public static final String SESSION_TIMEOUT = "component.zookeeper.session-timeout";

    /**
     * Zookeeper连接超时
     */
    public static final String CONNECTION_TIMEOUT = "component.zookeeper.connection-timeout";

    /**
     * Zookeeper连接失败重试次数
     */
    public static final String RETRY = "component.zookeeper.retry";

    /**
     * Zookeeper创建节点默认位置
     */
    public static final String NAMESPACE = "component.zookeeper.namespace";

    private ZookeeperConfiguration() {}
}
