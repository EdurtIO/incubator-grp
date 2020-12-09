package io.edurt.grp.web.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.edurt.grp.web.controller.TestController;
import org.junit.Test;

public class TestGuiceApplication
{
    public static void main(String[] args)
    {
        Injector injector = Guice.createInjector(new ControllerModule(""));
        TestController controller = injector.getInstance(TestController.class);
        controller.testGet("test");
    }
}
