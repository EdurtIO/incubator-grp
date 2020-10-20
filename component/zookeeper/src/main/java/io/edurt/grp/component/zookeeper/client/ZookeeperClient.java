package io.edurt.grp.component.zookeeper.client;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.curator.framework.CuratorFramework;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZookeeperClient {

    private final static Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);

    private CuratorFramework client;

    public ZookeeperClient(CuratorFramework client) {
        if (ObjectUtils.isEmpty(client)) {
            LOGGER.error("无法初始化Zookeeper客户端，请检查配置是否正确!");
            throw new RuntimeException("Unable to initialize zookeeper client instance!");
        }
        this.client = client;
    }

    /**
     * 创建新节点
     *
     * @param nodeName 节点名称
     * @return 创建结果，成功后返回路径，失败返回null
     */
    public String createNode(String nodeName) {
        try {
            startClient();
            String node = client.create().forPath(nodeName);
            return node;
        } catch (Exception ex) {
            LOGGER.error("创建节点{}失败，异常信息 {}", ex);
        } finally {
            closeClient();
        }
        return null;
    }

    /**
     * 启动Zookeeper客户端
     */
    private void startClient() {
        if (ObjectUtils.isNotEmpty(this.client)) {
            this.client.start();
        }
    }

    /**
     * 关闭Zookeeper客户端
     */
    private void closeClient() {
        if (ObjectUtils.isNotEmpty(this.client)) {
            this.client.close();
        }
    }

}
