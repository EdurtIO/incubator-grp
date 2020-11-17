package io.edurt.grp.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.grp.client.module.GrpClientModule;
import io.edurt.grp.component.zookeeper.ZookeeperModule;
import io.edurt.grp.component.zookeeper.client.ZookeeperClient;
import org.junit.Before;
import org.junit.Test;

public class TestGrpClient
{
    private Injector injector;

    @Before
    public void before()
    {
        injector = Guice.createInjector(new ZookeeperModule());
        injector.createChildInjector(new GrpClientModule(injector.getInstance(ZookeeperClient.class)));
    }

    @Test
    public void test()
    {
        injector.getBindings();
    }
}
