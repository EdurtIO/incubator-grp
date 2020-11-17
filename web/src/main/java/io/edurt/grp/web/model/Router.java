package io.edurt.grp.web.model;

import io.edurt.grp.web.type.RequestMethod;
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
public class Router
{
    private List<Param> params; // 客户端请求参数
    private Class clazz; // 请求使用的类信息
    private RequestMethod[] methods; // 请求方法类型
    private Method method; // 请求方法
    private String[] urls; // 请求路径
}
