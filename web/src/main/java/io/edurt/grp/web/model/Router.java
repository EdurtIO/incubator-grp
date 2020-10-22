package io.edurt.grp.web.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.lang.reflect.Method;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Router {

    private List<Param> params; // 客户端请求参数
    private Class clazz; // 请求使用的类信息
    private String methodType; // 请求方法类型
    private Method method; // 请求方法
    private String url; // 请求路径

}
