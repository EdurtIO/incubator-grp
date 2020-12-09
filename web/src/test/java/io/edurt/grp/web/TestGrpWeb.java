package io.edurt.grp.web;

import io.edurt.grp.web.controller.TestController;

public class TestGrpWeb
{

    public static void main(String[] args)
    {
        GrpModule grpServer = new GrpModule();
        grpServer.builder()
                .setPort(9090)
                .setController(TestController.class)
                .create().start();
    }
}
