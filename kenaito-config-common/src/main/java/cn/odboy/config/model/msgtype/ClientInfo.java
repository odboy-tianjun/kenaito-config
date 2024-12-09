package cn.odboy.config.model.msgtype;

import lombok.Data;

import java.io.Serializable;

/**
 * 客户端 信息
 *
 * @author odboy
 * @date 2024-12-09
 */
@Data
public class ClientInfo implements Serializable {
    private String server;
    private Integer port;
    private String env;
    private String dataId;
    private String cacheDir;
}
