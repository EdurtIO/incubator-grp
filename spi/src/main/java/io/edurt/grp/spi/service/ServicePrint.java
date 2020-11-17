package io.edurt.grp.spi.service;

import io.edurt.grp.spi.annotation.RpcService;

@RpcService
public interface ServicePrint
{
    void println(String string);
}
