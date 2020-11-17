package io.edurt.grp.web.handler;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathHandler
{
    private static final String pattern = "(\\$\\{[^}]*})";

    private PathHandler()
    {}

    /**
     * 校验客户端URL是否匹配路由URL
     * <p>例如：客户端传递<code>/path/123</code>匹配路由中的<code>/path/${id}</code></p>
     *
     * @param requestUrl 客户端请求URL
     * @param routerUrl 路由URL
     * @return 匹配状态，True匹配，False不匹配
     */
    public static boolean verify(String requestUrl, String routerUrl)
    {
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
     * @param routerUrl 路由URL
     * @return 客户端请求参数
     */
    public static Map<String, String> getParams(String requestUrl, String[] routerUrl)
    {
        Map<String, String> params = new ConcurrentHashMap<>(16);
        Matcher keyMatcher = matcherPath(routerUrl);
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

    /**
     * 匹配路由中传递的地址数组，匹配到多个时，默认选择第一个
     *
     * @param routerUrl 路由中的地址
     * @return 匹配到的路由，匹配不到返回null
     */
    private static Matcher matcherPath(String[] routerUrl)
    {
        Matcher matcher = null;
        if (ObjectUtils.isNotEmpty(routerUrl) && routerUrl.length > 0) {
            Optional<Matcher> matcherOptional = Arrays.stream(routerUrl)
                    .map(v -> Pattern.compile(pattern).matcher(v))
                    .findFirst();
            if (matcherOptional.isPresent()) {
                matcher = matcherOptional.get();
            }
        }
        return matcher;
    }
}
