package cn.odboy.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message implements Serializable {
    /**
     * 消息类型：<br/>
     * register 注册<br/>
     * pullConfig 拉取配置<br/>
     * updateConfig 更新配置<br/>
     */
    private String type;
    private Object data;
}
