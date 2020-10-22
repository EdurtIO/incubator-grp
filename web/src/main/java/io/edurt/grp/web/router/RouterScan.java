package io.edurt.grp.web.router;

import io.edurt.grp.web.annotation.PathParam;
import io.edurt.grp.web.annotation.RequestMapping;
import io.edurt.grp.web.annotation.RequestParam;
import io.edurt.grp.web.model.Param;
import io.edurt.grp.web.model.Router;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RouterScan {

    public static Map<String, Router> getRouters(Class<?>... classes) {
        Map<String, Router> routers = new ConcurrentHashMap<>(16);
        for (Class<?> clazz : classes) {
            for (Method method : clazz.getDeclaredMethods()) {
                RequestMapping routerMapping = method.getAnnotation(RequestMapping.class);
                // 如果注解不为空，扫描该方法内的参数注解
                if (ObjectUtils.isNotEmpty(routerMapping)) {
                    Router router = new Router();
                    List<Param> params = new ArrayList<>(16);
                    for (Parameter parameter : method.getParameters()) {
                        // 扫描参数注解，并设置道请求信息中
                        PathParam pathParam = parameter.getAnnotation(PathParam.class);
                        RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                        Param param = new Param();
                        // 设置request请求数据信息
                        if (ObjectUtils.isNotEmpty(pathParam)) {
                            param.setType("path");
                            param.setValue(pathParam.value());
                        }
                        // 设置路径请求数据信息
                        if (ObjectUtils.isNotEmpty(requestParam)) {
                            param.setType("request");
                            param.setValue(requestParam.value());
                        }
                        params.add(param);
                    }
                    router.setParams(params);
                    router.setClazz(clazz);
                    router.setMethodType(routerMapping.method());
                    router.setMethod(method);
                    router.setUrl(routerMapping.api());
                    routers.put(router.getUrl(), router);
                }
            }
        }
        return routers;
    }

}
