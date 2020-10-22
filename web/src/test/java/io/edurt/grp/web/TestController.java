package io.edurt.grp.web;

import io.edurt.grp.web.annotation.RequestMapping;
import io.edurt.grp.web.annotation.RequestParam;
import io.edurt.grp.web.type.RequestMethod;

import java.util.HashMap;
import java.util.Map;

public class TestController {

    @RequestMapping(value = {"/path", "/test/path"}, method = { RequestMethod.GET, RequestMethod.POST })
    public Map<String, Object> testGet(@RequestParam("name") String name) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", name);
        return map;
    }

}
