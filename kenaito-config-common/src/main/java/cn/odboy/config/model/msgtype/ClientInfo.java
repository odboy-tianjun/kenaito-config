package cn.odboy.config.model.msgtype;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientInfo implements Serializable {
    private String server;
    private Integer port;
    private String env;
    private String dataId;
    private String cacheDir;
}
