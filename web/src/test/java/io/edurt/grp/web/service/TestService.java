package io.edurt.grp.web.service;

import com.google.inject.ImplementedBy;

@ImplementedBy(value = TestServiceImpl.class)
public interface TestService
{
    void println(String message);
}
