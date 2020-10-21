package io.edurt.grp.component.zookeeper.client;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

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
            String node = client.create().forPath(formatNodePath(nodeName));
            return node;
        } catch (Exception ex) {
            LOGGER.error("创建节点{}失败，异常信息 {}", nodeName, ex);
        } finally {
            closeClient();
        }
        return null;
    }

    /**
     * 创建临时节点
     *
     * @param nodeName 节点名称
     * @return 创建结果，成功后返回路径，失败返回null
     */
    public String createEphemeralNode(String nodeName) {
        return createEphemeralNode(nodeName, null);
    }

    public String createEphemeralNode(String nodeName, String value) {
        try {
            startClient();
            String node;
            if (ObjectUtils.isNotEmpty(value)) {
                node = client.create()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(formatNodePath(nodeName), value.getBytes());
            } else {
                node = client.create()
                        .withMode(CreateMode.EPHEMERAL)
                        .forPath(formatNodePath(nodeName));
            }
            return node;
        } catch (Exception ex) {
            LOGGER.error("创建节点{}失败，异常信息 {}", nodeName, ex);
        } finally {
            closeClient();
        }
        return null;
    }

    /**
     * 检查节点是否存在
     *
     * @param nodeName 节点名称
     * @return 检查结果，True标志已经存在，False标志尚未存在
     */
    public Boolean existsNode(String nodeName) {
        try {
            startClient();
            Stat stat = client.checkExists().forPath(formatNodePath(nodeName));
            if (ObjectUtils.isNotEmpty(stat)) {
                return Boolean.TRUE;
            }
        } catch (Exception ex) {
            LOGGER.error("检查节点{}失败，异常信息 {}", nodeName, ex);
        } finally {
            closeClient();
        }
        return Boolean.FALSE;
    }

    /**
     * 删除节点
     *
     * @param nodeName 节点名称
     * @return 删除节点状态，True标志删除成功，False标志删除失败
     */
    public Boolean deleteNode(String nodeName) {
        try {
            startClient();
            client.delete()
                    .guaranteed() // 删除失败，则客户端持续删除，直到节点删除为止
                    .deletingChildrenIfNeeded()   // 删除相关子节点
                    .withVersion(-1)    // 无视版本，直接删除
                    .forPath(formatNodePath(nodeName));
            return Boolean.TRUE;
        } catch (Exception ex) {
            LOGGER.error("删除节点{}失败，异常信息 {}", nodeName, ex);
        } finally {
            closeClient();
        }
        return Boolean.FALSE;
    }

    /**
     * 更新节点
     *
     * @param nodeName 节点名称
     * @return 更新节点状态，True标志删除成功，False标志删除失败
     */
    public Boolean updateNode(String nodeName, String value) {
        try {
            startClient();
            client.setData()
                    .withVersion(-1)
                    .forPath(formatNodePath(nodeName), value.getBytes());
            return Boolean.TRUE;
        } catch (Exception ex) {
            LOGGER.error("更新节点{}失败，异常信息 {}", nodeName, ex);
        } finally {
            closeClient();
        }
        return Boolean.FALSE;
    }

    /**
     * 获取节点信息
     *
     * @param nodeName 节点名称
     * @return 节点信息
     */
    public String getNode(String nodeName) {
        try {
            startClient();
            byte[] bytes = client.getData().forPath(formatNodePath(nodeName));
            if (ObjectUtils.isNotEmpty(bytes) && bytes.length > 0) {
                return new String(bytes, Charset.forName("UTF-8"));
            }
        } catch (Exception ex) {
            LOGGER.error("更新节点{}失败，异常信息 {}", nodeName, ex);
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
            CloseableUtils.closeQuietly(this.client);
        }
    }

    /**
     * 格式化节点名称
     * <p>Zookeeper节点名称必须以/开头，未按照改格式的节点名称，系统自动添加</p>
     *
     * @param nodeName 节点名称
     * @return 节点名称
     */
    private String formatNodePath(String nodeName) {
        if (!nodeName.startsWith("/")) {
            nodeName = "/" + nodeName;
        }
        return nodeName;
    }

}
