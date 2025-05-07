package cn.odboy.config.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 传输的消息类型
 *
 * @author odboy
 * @date 2024-12-05
 */
@Getter
@AllArgsConstructor
public enum TransferMessageType {
    /**
     * 客户端注册
     */
    REGISTER,
    /**
     * 客户端主动拉取配置文件
     */
    PULL_CONFIG,
    /**
     * 服务端主动推送配置文件
     */
    PUSH_CONFIG;
}
