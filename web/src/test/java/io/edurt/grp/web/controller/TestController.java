package io.edurt.grp.web.controller;

import com.google.inject.Inject;
import io.edurt.grp.web.annotation.RequestMapping;
import io.edurt.grp.web.annotation.RequestParam;
import io.edurt.grp.web.annotation.RestController;
import io.edurt.grp.web.service.TestService;
import io.edurt.grp.web.type.RequestMethod;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController
{
    @Inject
    private TestService testService;

    @RequestMapping(value = {"/path", "/test/path"}, method = {RequestMethod.GET, RequestMethod.POST})
    public Map<String, Object> testGet(@RequestParam("name") String name)
    {
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", name);
        System.out.println(map);
        testService.println(name);
        return map;
    }
}
