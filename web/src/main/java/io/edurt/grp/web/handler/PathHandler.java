package io.edurt.grp.web.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathHandler {

    private static final String pattern = "(\\$\\{[^}]*})";

    /**
     * 校验客户端URL是否匹配路由URL
     * <p>例如：客户端传递<code>/path/123</code>匹配路由中的<code>/path/${id}</code></p>
     *
     * @param requestUrl 客户端请求URL
     * @param routerUrl  路由URL
     * @return 匹配状态，True匹配，False不匹配
     */
    public static boolean verify(String requestUrl, String routerUrl) {
        Matcher keyMatcher = Pattern.compile(pattern).matcher(routerUrl);
        String replacePattern = keyMatcher.replaceAll("(.*)");
        Matcher valueMatcher = Pattern.compile(replacePattern).matcher(requestUrl);
        return valueMatcher.matches();
    }

    /**
     * 获取客户端请求参数
     * <p>例如：客户端请求<code>/path/123</code>路由中为<code>/path/${id}</code></p>
     * <p>获取到的参数为<code>{"id":"123"}</code></p>
     *
     * @param requestUrl 客户端请求URL
     * @param routerUrl  路由URL
     * @return 客户端请求参数
     */
    public static Map<String, String> getParams(String requestUrl, String routerUrl) {
        Map<String, String> params = new ConcurrentHashMap<>(16);
        Matcher keyMatcher = Pattern.compile(pattern).matcher(routerUrl);
        List<String> keys = new ArrayList<>(16);
        List<String> values = new ArrayList<>(16);
        while (keyMatcher.find()) {
            keys.add(keyMatcher.group(1).replace("{", "").replace("}", ""));
        }
        String replacePattern = keyMatcher.replaceAll("(.*)");
        Matcher valueMatcher = Pattern.compile(replacePattern).matcher(requestUrl);
        if (valueMatcher.find()) {
            int count = valueMatcher.groupCount();
            for (int i = 1; i <= count; i++) {
                values.add(valueMatcher.group(i));
            }
        }
        int valueSize = values.size();
        for (int i = 0; i < keys.size(); i++) {
            String value = i < valueSize ? values.get(i) : "";
            params.put(keys.get(i), value);
        }
        return params;
    }

}
