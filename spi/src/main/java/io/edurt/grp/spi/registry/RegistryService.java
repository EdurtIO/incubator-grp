package io.edurt.grp.spi.registry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class RegistryService {

    private String id; // 唯一标志
    private String hostname; // 主机名称
    private Integer port; // 主机端口
    private List<String> services = new ArrayList<>(); // 主机服务列

}
