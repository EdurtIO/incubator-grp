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
public class GrpResponse {

    private String requestId; // 请求唯一标志
    private String responseId; // 数据返回唯一标志
    private String error; // 数据返回的错误信息
    private Object result; // 数据返回结果
    private LocalTime time; // 返回时间

}
