package io.edurt.grp.web.service;

public class TestServiceImpl
        implements TestService
{
    @Override
    public void println(String message)
    {
        System.out.println(message);
    }
}
