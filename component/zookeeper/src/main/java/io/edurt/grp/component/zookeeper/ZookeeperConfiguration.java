package io.edurt.grp.component.zookeeper;

public class ZookeeperConfiguration {

    /**
     * Zookeeper连接地址，例如 127.0.0.1:2181,127.0.0.1:2182
     */
    public final static String SERVICE = "component.zookeeper.service";

    /**
     * Zookeeper Session连接超时
     */
    public final static String SESSION_TIMEOUT = "component.zookeeper.session-timeout";

    /**
     * Zookeeper连接超时
     */
    public final static String CONNECTION_TIMEOUT = "component.zookeeper.connection-timeout";

    /**
     * Zookeeper连接失败重试次数
     */
    public final static String RETRY = "component.zookeeper.retry";

    /**
     * Zookeeper创建节点默认位置
     */
    public final static String NAMESPACE = "component.zookeeper.namespace";

}
