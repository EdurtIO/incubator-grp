package io.edurt.grp.client;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.grp.client.module.GrpClientModule;
import io.edurt.grp.spi.service.ServicePrint;
import org.junit.Test;

import java.util.Arrays;

public class TestGrpClient {

    @Test
    public void test() {
//        Injector injector = Guice.createInjector(new GrpClientModule("localhost:2181",
//                Arrays.asList(ServicePrint.class)));
//        ServicePrint print = injector.getInstance(ServicePrint.class);
//        System.out.println(print.toString());
    }

}
