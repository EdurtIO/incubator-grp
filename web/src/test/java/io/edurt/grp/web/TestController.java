package io.edurt.grp.web;

import io.edurt.grp.web.annotation.RequestMapping;
import io.edurt.grp.web.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

public class TestController {

    @RequestMapping(api = "/test/path/get/${id}", method = "GET")
    public Map<String, Object> testGet(@RequestParam("name") String name) {
        Map<String, Object> map = new HashMap<>(16);
        map.put("name", name);
        return map;
    }

}
