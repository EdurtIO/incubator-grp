package io.edurt.grp.server.controller;

import io.edurt.grp.web.annotation.RestController;

@RestController
public class TestController
{
    public void println(String name)
    {
        System.out.println(name);
    }
}
