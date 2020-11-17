package io.edurt.grp.proto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalTime;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class GrpRequest
{
    private String requestId; // 请求唯一标志
    private String className; // 请求的类名称
    private String methodName; // 请求的方法
    private LocalTime time; // 请求时间
    private Object[] parameters; // 序列化的参数
    private Class<?>[] parameterTypes; // 序列化的参数类型
}
