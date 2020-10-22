package io.edurt.grp.web;

import org.junit.Test;

public class TestGrpWeb {

    public static void main(String[] args) {
        GrpServer grpServer = new GrpServer();
        grpServer.builder()
                .setPort(9090)
                .setController(TestController.class)
                .create().start();
    }

}
