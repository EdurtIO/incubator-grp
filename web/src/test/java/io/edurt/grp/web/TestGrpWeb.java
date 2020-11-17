package io.edurt.grp.web;

public class TestGrpWeb
{

    public static void main(String[] args)
    {
        GrpServer grpServer = new GrpServer();
        grpServer.builder()
                .setPort(9090)
                .setController(TestController.class)
                .create().start();
    }
}
