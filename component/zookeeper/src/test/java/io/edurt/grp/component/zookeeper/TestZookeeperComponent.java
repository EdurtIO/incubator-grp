package io.edurt.grp.component.zookeeper;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.grp.common.utils.NetWorksUtils;
import io.edurt.grp.component.zookeeper.client.ZookeeperClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestZookeeperComponent
{

    private Injector injector;
    private ZookeeperClient zookeeperClient;
    private String node1 = "test";
    private String node2 = "test1";

    @Before
    public void before()
    {
        injector = Guice.createInjector(new ZookeeperModule());
        zookeeperClient = injector.getInstance(ZookeeperClient.class);
    }

    @Test
    public void testCreateNode()
    {
        System.out.printf(zookeeperClient.createNode(node1));
    }

    @Test
    public void testExistsNode()
    {
        Assert.assertEquals(Boolean.TRUE, zookeeperClient.existsNode(node1));
        Assert.assertEquals(Boolean.FALSE, zookeeperClient.existsNode(node2));
    }

    @Test
    public void testDeleteNode()
    {
        zookeeperClient.deleteNode(node1);
    }

    @Test
    public void testUpdateNode()
    {
        zookeeperClient.updateNode(node1, "node2");
    }

    @Test
    public void testGetNode()
    {
        System.out.println(zookeeperClient.getNode(node1));
    }

    @Test
    public void testCreateEphemeralNode()
    {
        System.out.println(zookeeperClient.createEphemeralNode(node1));
        System.out.println(zookeeperClient.createEphemeralNode(node2, "test"));
    }

    @Test
    public void test()
    {
        System.out.println(this.zookeeperClient.getNode(NetWorksUtils.getHostName()));
    }
}